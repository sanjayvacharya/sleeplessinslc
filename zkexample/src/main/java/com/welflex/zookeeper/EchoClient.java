package com.welflex.zookeeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.NavigableSet;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.Logger;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class EchoClient implements IZkDataListener, IZkStateListener, IZkChildListener {
  private static final Logger LOG = Logger.getLogger(EchoClient.class);

  private Connection connection;

  private Object mutex = new Object();

  private final ZkClient zkClient;

  private SequentialZkNode connectedToNode;

  public EchoClient(int zkPort) {
    this.zkClient = new ZkClient("localhost:" + zkPort);
  }

  private Connection getConnection() throws UnknownHostException, IOException, InterruptedException {

    synchronized (mutex) {
      if (connection != null) {
        return connection;
      }

      NavigableSet<SequentialZkNode> nodes = null;

      while ((nodes = ZkUtils.getNodes("/echo", zkClient)).size() == 0) {
        LOG.info("No echo service nodes ...waiting...");
        zkClient.subscribeChildChanges("/echo", this);
        mutex.wait();
      }

      connectedToNode = nodes.first();
      Integer port = zkClient.readData(connectedToNode.getPath());
      connection = new Connection(new Socket("localhost", port));
      zkClient.subscribeDataChanges(connectedToNode.getPath(), this);
      zkClient.subscribeStateChanges(this);
    }

    return connection;
  }

  public String echo(String command) throws InterruptedException {
    while (true) {
      try {
        return getConnection().send(command);
      }
      catch (InterruptedException e) {
        throw e;
      }
      catch (Exception e) {
        synchronized (mutex) {
          if (connection != null) {
            connection.close();
            connection = null;
          }
        }
      }
    }
  }

  private static class Connection {
    private final Socket clientSocket;

    private final PrintWriter out;

    BufferedReader reader;

    public Connection(Socket socket) {
      this.clientSocket = socket;

      try {
        out = new PrintWriter(clientSocket.getOutputStream());
      }
      catch (IOException e) {
        throw new RuntimeException("Error creating writer", e);
      }

      try {
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      }
      catch (IOException e) {
        throw new RuntimeException("Error creating reader from socket", e);
      }
    }

    public void close() {
      try {
        reader.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      out.close();
      try {
        clientSocket.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }

    public String send(String data) throws IOException {
      out.println(data);
      out.flush();
      String result = reader.readLine();

      if (result == null) {
        throw new IOException("Socket is dead");
      }
      
      return result;
    }
  }

  public void shutdown() {
    synchronized (mutex) {
      if (connection != null) {
        connection.close();
      }
      zkClient.close();
    }
  }

  @Override
  public void handleStateChanged(KeeperState state) throws Exception {

  }

  @Override
  public void handleNewSession() throws Exception {}

  @Override
  public void handleDataChange(String dataPath, Object data) throws Exception {}

  @Override
  public void handleDataDeleted(String dataPath) throws Exception {
    synchronized (mutex) {
      if (connectedToNode != null && dataPath.equals(connectedToNode.getPath())) {
        LOG.info("Client Received notification that Server has died..notifying to re-connect");
        if (connection != null) {
          connection.close();
        }
        if (connectedToNode != null) {
          connectedToNode = null;
        }
      }
      mutex.notify();
    }
  }

  @Override
  public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
    synchronized (mutex) {
      LOG.info("Got a notification about a Service coming up...notifying client");
      mutex.notify();
    }
  }
}
