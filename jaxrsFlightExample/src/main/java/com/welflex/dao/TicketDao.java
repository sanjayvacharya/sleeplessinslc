package com.welflex.dao;

import java.util.List;

import com.welflex.model.Ticket;

public interface TicketDao {  
  void save(Ticket ticket);
  
  List<Ticket> getTickets();
  
  Ticket getTicket(Long ticketId);
}
