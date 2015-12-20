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

public class LeaderElectionTest {
  private static final int ZK_PORT = 29980;

  private ZkServer zkServer;

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

    // Delete existing node if any and create the service node
    ZkClient client = new ZkClient("localhost:" + ZK_PORT);
    client.deleteRecursive("/echo");
    client.create("/echo", new byte[0], CreateMode.PERSISTENT);
    client.close();
  }

  @After
  public void tearDown() throws InterruptedException {
    zkServer.shutdown();
  }

  @Test
  public void simpleLeadership() throws InterruptedException {

    EchoClient client = new EchoClient(ZK_PORT);
    
    EchoServer serverA = new EchoServer(5555, ZK_PORT);
    serverA.start();
    EchoServer serverB = new EchoServer(5556, ZK_PORT);
    serverB.start();

    for (int i = 0; i < 10; i++) {
      System.out.println("Client Sending:Hello-" + i);
      System.out.println(client.echo("Hello-" + i));
    }
    
    if (serverA.isLeader()) {
      serverA.shutdown();
    } else {
      serverB.shutdown();
    }

    for (int i = 0; i < 10; i++) {
      System.out.println("Client Sending:Hello-" + i);
      System.out.println(client.echo("Hello-" + i));
    }

    serverA.shutdown();
    serverB.shutdown();
  }

  @Test
  public void longRunning() throws Exception {
    EchoServer serverA = new EchoServer(5555, ZK_PORT);
    serverA.start();
    EchoServer serverB = new EchoServer(5556, ZK_PORT);
    serverB.start();
    final EchoClient client = new EchoClient(ZK_PORT);
    Thread clientThread = new Thread() {
      public void run() {
        for (int i = 0; i < 200; i++) {
          try {
            System.out.println("Client sending [" + "Hello-" + i + "]");
            System.out.println(client.echo("Hello-" + i));
            Thread.sleep(50);
          }
          catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    };

    clientThread.start();
    Thread.sleep(2000);
    serverA.shutdown();
    Thread.sleep(2000);
    serverB.shutdown();
    Thread.sleep(2000);
    serverA = new EchoServer(5555, ZK_PORT);
    serverA.start();

    clientThread.join();
    client.shutdown();
  }
}
