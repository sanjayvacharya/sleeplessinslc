package com.welflex.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.welflex.dao.TicketDao;
import com.welflex.model.Ticket;

@Repository
public class TicketDaoImpl extends HibernateDaoSupport implements TicketDao {

  public TicketDaoImpl(SessionFactory sessionFactory) {
    super.setSessionFactory(sessionFactory);
  }

  @Override
  public void save(Ticket ticket) {
    getHibernateTemplate().save(ticket);
  }

  @Override
  public List<Ticket> getTickets() {
    return getHibernateTemplate().loadAll(Ticket.class);
  }

  @Override
  public Ticket getTicket(Long ticketId) {
    return getHibernateTemplate().get(Ticket.class, ticketId);
  }

  @Override
  public void delete(Ticket ticket) {
    getHibernateTemplate().delete(ticket);
  }
}
