package com.welflex.notes.client;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.welflex.spring.web.notes.Note;
import netflix.ocelli.InstanceCollector;
import netflix.ocelli.eureka.EurekaInterestManager;
import netflix.ocelli.loadbalancer.RoundRobinLoadBalancer;
import netflix.ocelli.util.RxUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import rx.Subscription;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class NotesClientImpl implements NotesClient, Closeable {
  private final RestTemplate restTemplate;
  private final Subscription subscription;
  private final AtomicReference<List<InstanceInfo>> result;
  private final RoundRobinLoadBalancer<InstanceInfo> instanceInfoLoadBalancer;

  public NotesClientImpl(RestTemplate restTemplate, DiscoveryClient discoveryClient,
                         long refreshInterval, TimeUnit refreshIntervalTimeUnit) {
    this.restTemplate = restTemplate;
    this.result = new AtomicReference<List<InstanceInfo>>(new ArrayList<InstanceInfo>());
    this.subscription = new EurekaInterestManager(discoveryClient).newInterest()
            .forApplication("Notes").withRefreshInterval(refreshInterval, refreshIntervalTimeUnit)
            .asObservable()
            .compose(InstanceCollector.<InstanceInfo>create())
            .subscribe(RxUtil.set(result));
    instanceInfoLoadBalancer = RoundRobinLoadBalancer.<InstanceInfo>create();
  }

  private String getServiceInstanceUrl() {

    InstanceInfo instanceInfo = instanceInfoLoadBalancer.choose(result.get());
    if (instanceInfo != null) {
      return "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort();
    }
    throw new RuntimeException("Service Not available");
  }

  @Override
  public Note getNote(Long noteId) {
    ResponseEntity<Note> response =  restTemplate.getForEntity(getServiceInstanceUrl() + "/notes/{id}", Note.class, noteId);
    return response.getBody();
  }

  @Override
  public Long createNote(Note note) {
    ResponseEntity<Long> longResponseEntity = restTemplate.postForEntity(getServiceInstanceUrl() + "/notes", note, Long.class);
    return longResponseEntity.getBody();
  }

  @Override
  public void close() throws IOException {
    if (subscription != null) {
      subscription.unsubscribe();
    }
  }
}
