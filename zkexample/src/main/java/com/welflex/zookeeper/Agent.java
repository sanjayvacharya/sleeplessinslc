package com.welflex.zookeeper;

import java.util.List;
import java.util.Random;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.Logger;

/**
 * Performs three simple tasks:
 * 1. Joins the meeting and waits for all attendees
 * 2. Presents
 * 3. Leaves after all attendees complete their presentations
 * 
 */
public class Agent extends Thread implements IZkChildListener {
  private static final Logger LOG = Logger.getLogger(Agent.class);

  private final ZkClient zkClient;

  private final int agentCount;

  private final String agentNumber;

  private final Object mutex = new Object();

  private final Random random = new Random();

  private SequentialZkNode agentRegistration;

  private final String meetingId;

  public Agent(String agentNumber, String meetingId, int agentCount, int zkPort) {
    this.agentNumber = agentNumber;
    this.meetingId = meetingId;
    this.agentCount = agentCount;
    this.zkClient = new ZkClient("localhost:" + zkPort);
    zkClient.subscribeChildChanges(meetingId, this);
  }

  private void joinBriefing() throws InterruptedException {
    Thread.sleep(random.nextInt(1000));
    agentRegistration = ZkUtils.createEpheremalNode(meetingId, agentNumber, zkClient);
    LOG.info("Agent:" + agentNumber + " joined Briefing:" + meetingId);

    while (true) {
      synchronized (mutex) {
        List<String> list = zkClient.getChildren(meetingId);

        if (list.size() < agentCount) {
          LOG.info("Agent:" + agentNumber
            + " waiting for other agents to join before presenting report...");
          mutex.wait();
        }
        else {
          break;
        }
      }
    }
  }

  private void presentReport() throws InterruptedException {
    LOG.info("Agent:" + agentNumber + " presenting report...");
    Thread.sleep(random.nextInt(1000));
    LOG.info("Agent:" + agentNumber + " completed report...");
    zkClient.delete(agentRegistration.getPath());
  }

  private void leaveBriefing() throws InterruptedException {

    while (true) {
      synchronized (mutex) {
        List<String> list = zkClient.getChildren(meetingId);
        if (list.size() > 0) {
          LOG.info("Agent:" + agentNumber
            + " waiting for other agents to complete their briefings...");
          mutex.wait();
        }
        else {
          break;
        }
      }
    }
    LOG.info("Agent:" + agentNumber + " left briefing");
  }

  @Override
  public void run() {
    try {
      joinBriefing();
      presentReport();
      leaveBriefing();
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
    finally {
      if (zkClient != null) {
        zkClient.close();
      }
    }
  }

  @Override
  public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
    synchronized (mutex) {
      mutex.notifyAll();
    }
  }
}
