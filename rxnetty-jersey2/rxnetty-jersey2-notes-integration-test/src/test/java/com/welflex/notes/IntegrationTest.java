package com.welflex.notes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import rx.Observable;

import com.google.common.collect.Sets;
import com.welflex.notes.jaxb.Note;
import com.welflex.notes.jaxrs.client.NotesClient;
import com.welflex.notes.jaxrs.client.NotesClientImpl;
import com.welflex.notes.rest.HealthResource;
import com.welflex.notes.rest.NotesApplication;
import com.welflex.rxnetty.jersey2.RxNettyHttpServerFactory;

public class IntegrationTest {

  private static AnnotationConfigApplicationContext ctx;

  private static HttpServer<ByteBuf, ByteBuf> nettyHttpServer;

  private static NotesClient notesClient;

  @BeforeClass
  public static void beforeClass() {
    ResourceConfig resourceConfig = ResourceConfig.forApplication(new NotesApplication());

    ctx = new AnnotationConfigApplicationContext();
    ctx.register(NotesSpringConfiguration.class);
    ctx.refresh();
    resourceConfig.property("contextConfig", ctx);
    nettyHttpServer = RxNettyHttpServerFactory.createHttpServer(9090, resourceConfig, true);
    notesClient = new NotesClientImpl("http://localhost:9090");
  }

  @AfterClass
  public static void afterClass() throws InterruptedException {
    IOUtils.closeQuietly(ctx);
    nettyHttpServer.shutdown();
  }
  
  @Test
  public void health() {
    Response response = notesClient.isHealthy().toBlocking().single();
    assertTrue(response.getStatus() == Status.OK.getStatusCode());
    String result = response.readEntity(String.class);
    Set<String> items = Sets.newHashSet();
    for (String s : result.split("\n")) {
      items.add(s);
    }
    assertTrue(items.containsAll(Sets.<String>newHashSet("STATUS:OK", HealthResource.INJECTED_RX_NETTY_REQUEST + "true", HealthResource.INJECTED_RX_NETTY_RESPONSE + "true")));
    response.close();
  }

  @Test
  public void noteLifeCycle() {
    Note note = new Note();
    note.setUserId("sacharya");
    note.setContent("Something to say");

    // Create
    Long noteId = null;
    noteId = notesClient.create(note).toBlocking().single().getNoteId();

    System.out.println("Created Note with ID:" + noteId);

    // Read
    Note fetched  = notesClient.get(noteId).toBlocking().single();
      assertEquals(note, fetched);

    // Read All

    final CopyOnWriteArrayList<Note> allNotes = new CopyOnWriteArrayList<Note>();
    notesClient.allNotes().filter(t1 -> t1.getUserId().equals("sacharya")).toBlocking()
        .forEach(note1 -> allNotes.add(note1));

    assertTrue(allNotes.size() == 1);

    assertEquals(note, allNotes.get(0));

    // Update
    note.setContent("Something to say updated");
    notesClient.update(noteId, note).toBlocking().single().close();

    // Fetch and verify update
    fetched = notesClient.get(noteId).toBlocking().single();
    assertEquals(note, fetched);

    Response response = notesClient.delete(noteId).toBlocking().single();
    // Delete
    assertEquals(204, response.getStatus());
    response.close();

    // Verify deleted
    AtomicBoolean errorAsExpected = new AtomicBoolean(false);
    notesClient.get(noteId).onErrorResumeNext(t1 -> {
      errorAsExpected.set(true);
      return Observable.<Note>just(new Note("NOT_FOUND", "NOT_FOUND"));
    }).toBlocking().single();
    assertTrue(errorAsExpected.get());
  }
}
