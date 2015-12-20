package com.welflex.zookeeper;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;

import org.apache.log4j.Logger;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class EchoServer extends Thread implements IZkDataListener, IZkStateListener {
  private static final Logger LOG = Logger.getLogger(EchoServer.class);

  private ZkClient zkClient;

  private SequentialZkNode zkNode;

  private SequentialZkNode zkLeaderNode;

  private ServerSocket serverSocket;

  private volatile boolean shutdown;

  private final Integer port;

  private final List<SlaveJob> slaves;

  private final Object mutex = new Object();

  private final AtomicBoolean start = new AtomicBoolean(false);
  
  private final Random random = new Random();

  public EchoServer(int port, int zkPort) {
    this.port = port;
    this.zkClient = new ZkClient("localhost:" + zkPort);
    this.slaves = new ArrayList<SlaveJob>();
  }
  
  public boolean isLeader() {
    return start.get();
  }

  @Override
  public void run() {
    try {
      Thread.sleep(random.nextInt(1000));
      
      // Create Ephermal node
      zkNode = ZkUtils.createEpheremalNode("/echo", port, zkClient);

      // Find all children
      NavigableSet<SequentialZkNode> nodes = ZkUtils.getNodes("/echo", zkClient);

      zkLeaderNode = ZkUtils.findLeaderOfNode(nodes, zkNode);

      if (zkLeaderNode.getPath().equals(zkNode.getPath())) {
        start.set(true);
      }

      // SEt a watch on next leader path
      zkClient.subscribeDataChanges(zkLeaderNode.getPath(), this);
      zkClient.subscribeStateChanges(this);
      
      synchronized (mutex) {
        while (!start.get()) {
          LOG.info("Server on Port:" + port + " waiting to spawn socket....");
          mutex.wait();
        }
      }

      LOG.info("Server on Port:" + port + " is now the Leader. Starting to accept connections...");

      this.serverSocket = new ServerSocket(port);

      Socket clientSocket = null;

      while (!Thread.currentThread().isInterrupted() && !shutdown) {
        clientSocket = serverSocket.accept();
        SlaveJob slave = new SlaveJob(port, clientSocket);
        slave.start();
        slaves.add(slave);
      }
    }
    catch (IOException e) {
      LOG.info("Master Socket for Server on Port:" + port + " closed:" + e.getMessage());
    }
    catch (InterruptedException e) {
      LOG.info("Server on Port:" + port + " interrupted");
    }
    finally {
      synchronized (mutex) {        
        if (serverSocket != null) {
          try {
            serverSocket.close();
            serverSocket = null;
          }
          catch (IOException e) {
            e.printStackTrace();
          }
        }

        if (zkClient != null) {
          zkClient.close();
          zkClient = null;
        }
      }
    }
    LOG.info("Server on Port:" + port + " has been shutdown");
  }

  public void shutdown() {
    if (shutdown) {
      return;
    }
    
    shutdown = true;

    interrupt();

    for (SlaveJob job : slaves) {
      job.shutdown();
    }

    // No more server socket jobs
    synchronized (mutex) {
      try {
        if (serverSocket != null) {
          serverSocket.close();
          serverSocket = null;
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }

      if (zkClient != null) {
        zkClient.close();
        zkClient = null;
      }
    }
  }

  static class SlaveJob extends Thread {
    private static final Logger LOG = Logger.getLogger(SlaveJob.class);

    private Socket clientSocket;

    private volatile boolean shutdown;

    private final int serverPort;

    public SlaveJob(int serverPort, Socket clientSocket) {
      this.serverPort = serverPort;
      this.clientSocket = clientSocket;
    }

    public void shutdown() {
      LOG.info("Asking Slave of Server:" + serverPort + " to shut down");
      try {
        if (clientSocket != null) {
          clientSocket.close();
        }
      }
      catch (IOException e) {}
      shutdown = true;
      interrupt();
    }

    public void run() {
      BufferedReader reader = null;

      PrintWriter writer = null;
      try {
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        writer = new PrintWriter(clientSocket.getOutputStream(), true);

        String line = null;

        while (!shutdown) {

          line = reader.readLine();
          if (line == null) {
            break;
          }
          String response = "Echo:" + line;
          writer.println(response);
          writer.flush();
        }
      }
      catch (IOException e) {
        LOG.info("Socket communication errror:" + e.getMessage());
      }
      finally {
        if (reader != null) {
          try {
            reader.close();
          }
          catch (IOException e) {
            LOG.warn("Should not have happened", e);
          }
        }

        if (writer != null) {
          writer.close();
        }

        if (clientSocket != null) {
          try {
            clientSocket.close();
          }
          catch (IOException e) {
            LOG.info("Should not have happened", e);
          }
        }
      }
      LOG.info("Slave job for Server:" + serverPort + " exiting.");
    }
  }

  private void electNewLeader() {
    final NavigableSet<SequentialZkNode> nodes = ZkUtils.getNodes("/echo", zkClient);
    
    if (nodes.size() == 0) {
      // No nodes present
      return;
    }
    
    if (!zkClient.exists(zkLeaderNode.getPath())) {
      // My Leader does not exist
      zkLeaderNode = ZkUtils.findLeaderOfNode(nodes, zkNode);
      zkClient.subscribeDataChanges(zkLeaderNode.getPath(), this);
      zkClient.subscribeStateChanges(this);

    }

    // If I am the leader then start
    if (zkNode.getSequence().equals(nodes.first().getSequence())) {
      LOG.info("Server on port:" + port + "  will now be notifed to assume leadership");
      synchronized (mutex) {
        start.set(true);
        mutex.notify();
      }
    }
  }

  @Override
  public void handleStateChanged(KeeperState state) throws Exception {}

  @Override
  public void handleNewSession() throws Exception {}

  @Override
  public void handleDataChange(String dataPath, Object data) throws Exception {}

  @Override
  public void handleDataDeleted(String dataPath) throws Exception {
    if (dataPath.equals(zkLeaderNode.getPath())) {
      // Leader gone away
      LOG.info("Recived a notification that Leader on path:" + dataPath
        + " has gone away..electing new leader");
      electNewLeader();
    }
  }
}
