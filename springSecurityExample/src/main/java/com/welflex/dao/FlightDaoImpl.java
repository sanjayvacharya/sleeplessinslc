package com.welflex.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.welflex.dao.FlightDao;
import com.welflex.model.Flight;


@Repository
public class FlightDaoImpl extends HibernateDaoSupport implements FlightDao {
  
  @Autowired
  public FlightDaoImpl(SessionFactory sessionFactory){
    super.setSessionFactory(sessionFactory);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Flight> findFlights(final String fromAirportCode, final String toAirportCode) throws DataAccessException {
    List<Flight> result = (List<Flight>)getHibernateTemplate().executeFind(new HibernateCallback<List<Flight>>() {

      public List<Flight> doInHibernate(Session session) throws HibernateException, SQLException {
        return session.createCriteria(Flight.class)
          .createAlias("from", "fromAirport")
          .createAlias("to", "toAirport")
          .add(Restrictions.eq("fromAirport.code", fromAirportCode))
          .add(Restrictions.eq("toAirport.code", toAirportCode))
          .list();
      }      
    });
    
    return result;
  }

  @Override
  public Flight getFlight(Long id) {
    return (Flight) getHibernateTemplate().get(Flight.class,id);
  }

  @Override
  public Flight getFlight(final String flightNumber) {
    return getHibernateTemplate().execute(new HibernateCallback<Flight>() {

      public Flight doInHibernate(Session session) throws HibernateException, SQLException {
        Criteria c = session.createCriteria(Flight.class)
          .add(Restrictions.eq("number", flightNumber));
       
        return Flight.class.cast(c.uniqueResult());
      }      
    });
  }

  @Override
  public void save(Flight flight) {    
    getHibernateTemplate().saveOrUpdate(flight);   
  }

  @Override public boolean decrementSeat(final Long flightId, final int quantity) {
    return getHibernateTemplate().execute(new HibernateCallback<Integer>() {

      @Override
      public Integer doInHibernate(Session session) throws HibernateException, SQLException {
        Query query = session.createQuery("update Flight set seatsAvailable=seatsAvailable- " + quantity
          + " where seatsAvailable - " + quantity + " > 0 and id=" + flightId);
        return query.executeUpdate();
      }

    }) == 1;

  }

  @Override
  public List<Flight> getFlights() {
    return getHibernateTemplate().loadAll(Flight.class);
  }
}
