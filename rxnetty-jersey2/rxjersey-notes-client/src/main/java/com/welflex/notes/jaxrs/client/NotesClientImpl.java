package com.welflex.notes.jaxrs.client;

import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.rx.Rx;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;

import rx.Observable;

import com.welflex.notes.exception.NoteNotFoundException;
import com.welflex.notes.jaxb.Note;
import com.welflex.notes.jaxb.NoteResult;

/**
 * Implementation of the Notes Client
 */
public class NotesClientImpl implements NotesClient {
  private final String baseUri;

  private final RxClient<RxObservableInvoker> rxClient;

  /**
   * @param baseUri Server URI
   */
  public NotesClientImpl(String baseUri) {
    this.baseUri = baseUri;
    ClientConfig config = new ClientConfig();

    config.property(ApacheClientProperties.CONNECTION_MANAGER, new PoolingHttpClientConnectionManager());
    config.register(MoxyXmlFeature.class);
    config.register(MoxyJsonFeature.class);
    
    rxClient = Rx.from(ClientBuilder.newClient(config), RxObservableInvoker.class);
  }

  @Override
  public Observable<NoteResult> create(Note note) {
    return rxClient.target(UriBuilder.fromUri(baseUri).path("/notes").build())
        .request(MediaType.APPLICATION_JSON).rx()
        .post(Entity.entity(note, MediaType.APPLICATION_XML_TYPE), NoteResult.class);
  }

  @Override
  public Observable<Note> allNotes() {
    return rxClient.target(UriBuilder.fromUri(baseUri).path("/notes").build())
        .request(MediaType.APPLICATION_XML).rx().get(new GenericType<List<Note>>() {})
        .flatMap(notes -> Observable.from(notes));
  }

  @Override
  public Observable<Response> update(Long noteId, Note note) {
    return rxClient.target(UriBuilder.fromUri(baseUri).path("/notes/" + noteId).build()).request()
        .rx().put(Entity.entity(note, MediaType.APPLICATION_XML));
  }

  @Override
  public Observable<Note> get(Long noteId) throws NoteNotFoundException {
    return rxClient.target(UriBuilder.fromUri(baseUri).path("/notes/" + noteId).build())
        .request(MediaType.APPLICATION_JSON).rx().get().map(response -> {
         
          try {
            if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
              throw new NoteNotFoundException(response.readEntity(String.class));
            }
            return response.readEntity(Note.class);
          }
          finally {
            response.close();
          }
        });
  }

  @Override
  public Observable<Response> delete(Long noteId) {
    return rxClient.target(UriBuilder.fromUri(baseUri).path("/notes/" + noteId).build()).request()
        .rx().delete();
  }

  @Override
  public Observable<String> create(Form form) {
    return rxClient.target(UriBuilder.fromUri(baseUri).path("/notes").build())
        .request(MediaType.TEXT_HTML).rx()
        .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
  }

  @Override
  public Observable<Response> isHealthy() {
    return rxClient.target(baseUri).path("/health").request(MediaType.TEXT_PLAIN)
          .rx().get();
  }
}
