package com.welflex.service.impl;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.welflex.model.Order;
import com.welflex.service.Auditor;

/**
 * A Simple Auditor Service Impl. All this does is store 
 * messages in a list.
 * 
 * @author Sanjay Acharya
 */
@Service
public class AuditorImpl implements Auditor {
    private static final List<String> auditDB = new ArrayList<String>(); 
    
    public void audit(String method, Order order) {
        StringBuilder builder = new StringBuilder();
        builder.append("Time:")
              .append(System.currentTimeMillis())
              .append(",")
              .append("Method:")
              .append(method)
              .append(",")
              .append(order);
        auditDB.add(builder.toString());
    }

    public void audit(String method, Long orderId) {
        StringBuilder builder = new StringBuilder();
        
        builder.append("Time:")
               .append(System.currentTimeMillis())
               .append("Method:")
               .append(method)
               .append(",")
               .append("Order Id:")
               .append(orderId);
      
        auditDB.add(builder.toString());
    }

    public List<String> getAuditedMessages() {
        return AuditorImpl.auditDB;
    }
}
