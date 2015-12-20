package com.welflex.zookeeper;

import java.io.IOException;

import org.I0Itec.zkclient.IDefaultNameSpace;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkServer;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AgentRendezvousTest {
  private static final int ZK_PORT = 29980;
  private ZkServer zkServer;

  private static final int AGENT_COUNT = 8;

  private static final String MEETING_ID = "/M-debrief";
  
  @Before
  public void setup() throws QuorumPeerConfig.ConfigException,
    IOException,
    KeeperException,
    InterruptedException {
    zkServer = new ZkServer("/tmp/zkExample/data", "/tmp/zkExample/log", new IDefaultNameSpace() {
      @Override
      public void createDefaultNameSpace(ZkClient zkClient) {}
    }, ZK_PORT);
    zkServer.start();
    ZkClient zkClient = new ZkClient("localhost:" + ZK_PORT);
    zkClient.deleteRecursive(MEETING_ID);
    zkClient.create(MEETING_ID, new byte[0], CreateMode.PERSISTENT);
    zkClient.close();
  }

  @After
  public void tearDown() throws InterruptedException {
    zkServer.shutdown();
  }
  
  @Test
  public void agentRendezvous() throws InterruptedException {
    Agent agent[] = new Agent[AGENT_COUNT];
    
    for (int i = 0 ; i < AGENT_COUNT; i++) {
      agent[i] = new Agent("00" + i, MEETING_ID, AGENT_COUNT, ZK_PORT);
      agent[i].start();
    }

    for (int i = 0 ; i < AGENT_COUNT; i++) {
      agent[i].join();
    }    
  }
}
