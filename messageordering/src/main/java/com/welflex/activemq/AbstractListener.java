package com.welflex.activemq;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import com.welflex.dto.MessageObject;
import com.welflex.exception.StaleObjectException;

/**
 * Abstract Message Listener that implements the Template Pattern
 */
public abstract class AbstractListener implements MessageListener {
  private static final Logger LOG = Logger.getLogger(AbstractListener.class);
  private final CountDownLatch latch;
  private final Random random = new Random();
  
  public AbstractListener(CountDownLatch latch) {
    this.latch = latch;    
  }  
  
  /**
   * On message will delegate to the doWithMessage() template method.
   */
  public void onMessage(Message m) {
    MessageObject record = null; 
    try {          
      ObjectMessage om = (ObjectMessage) m;
      record = (MessageObject) om.getObject();      
      Thread.sleep(random.nextInt(50));
      doWithMessage(record, om.getJMSTimestamp());
      LOG.info("Record updated :" + record.getRecordId());
    }
    catch (StaleObjectException staleObjectException) {
      LOG.error("Rejecting the Stale Record [" + record.getRecordId() +  "]", staleObjectException);
    }
    catch (RuntimeException unexpected) {
      LOG.error("Failed unexpectedly on processing Record [" + record.getRecordId() +  "]", unexpected);
    }
    catch (InterruptedException e) {
      LOG.info("Thread interrupted", e);
    }
    catch (JMSException e) {
      LOG.error(e, e);
    } finally {
      latch.countDown();
    }
  }
  
  protected abstract void doWithMessage(MessageObject m, long timeStamp)
    throws StaleObjectException;
}
