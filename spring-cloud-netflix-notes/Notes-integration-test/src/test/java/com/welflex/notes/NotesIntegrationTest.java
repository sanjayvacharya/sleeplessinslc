package com.welflex.notes;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.DiscoveryManager;
import com.welflex.notes.client.NotesClient;
import com.welflex.notes.client.NotesClientConfig;
import com.welflex.spring.web.notes.Application;
import com.welflex.spring.web.notes.Note;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * This integration test exercises Service Client to Service communication
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, NotesClientConfig.class})
@WebIntegrationTest(value = {"eureka.instance.leaseRenewalIntervalInSeconds=10", // Lease Interval
        "eureka.client.instanceInfoReplicationIntervalSeconds=1",
        "eureka.client.initialInstanceInfoReplicationIntervalSeconds=1",
        "eureka.client.registryFetchIntervalSeconds=1",
        "eureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka/v2/"},
        randomPort =  true)
public class NotesIntegrationTest {
  private static final Logger LOGGER = Logger.getLogger(NotesIntegrationTest.class);

  @Inject
  private NotesClient notesClient;

  @Value("${ocelliRefreshIntervalSeconds}")
  private Long ocelliRefreshIntervalSeconds; // Ocelli refresh interval


  @After
  public void shutdown() {
    discoveryClient.shutdown();
    DiscoveryManager.getInstance().shutdownComponent();
  }

  @Test
  public void createAndGetNote() throws InterruptedException {
    // Wait for Notes App to register
    waitForNotesRegistration();
    Thread.sleep((ocelliRefreshIntervalSeconds * 1000) + 1000L);
    Note note = new Note("Test");
    Long noteId = notesClient.createNote(note);
    assertEquals(note, notesClient.getNote(noteId));
  }

  @Inject
  private DiscoveryClient discoveryClient;

  private void waitForNotesRegistration() throws InterruptedException {
    int count = 0;
    while (!Thread.currentThread().isInterrupted()) {
      LOGGER.info("Checking where Discovery Client is reporting Notes Service as having registered. Count :" + (count++));
      com.netflix.discovery.shared.Application notesApplications = discoveryClient.getApplication("Notes");

      if (notesApplications != null && notesApplications.getInstances().size() > 0) {
        LOGGER.info("Notes Service has been registered with Eureka");
        return;
      }
      LOGGER.info("Discovery client is not reporting as having information about Notes Service. Sleeping for a bit...");
      Thread.sleep(1000);
    }
  }
}
