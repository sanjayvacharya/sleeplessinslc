package com.welflex.model;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;

import com.welflex.hibernate.HibernateUtil;

public class NationalPlayerTest {
  private NationalPlayer indiaPlayer;
  private NationalPlayer usaPlayer;
  private NationalPlayer italyPlayer;
  
  @Before
  public void beforeTest() {
    for (int i = 0; i < 3; i++) {
      Session session = HibernateUtil.getShardDbSession(i);
      Transaction tx = session.beginTransaction();
      session.createQuery("delete from NationalPlayer").executeUpdate();
      tx.commit();
      session.close();
    }
    // Player from India
    indiaPlayer = new NationalPlayer().withCountry(Country.INDIA).withCareerGoals(
      100).withFirstName("Sanjay").withLastName("Acharya").withWorldRanking(1);

    // Player from Usa
    usaPlayer = new NationalPlayer().withCountry(Country.USA).withCareerGoals(80)
        .withFirstName("Blake").withLastName("Acharya").withWorldRanking(10);
    
     italyPlayer = new NationalPlayer().withCountry(Country.ITALY)
      .withCareerGoals(20).withFirstName("Valentino").withLastName("Acharya")
       .withWorldRanking(32);     
  }
  
  @Test
  public void testShardingPersistence() {
    BigInteger indiaPlayerId = null;
    BigInteger usaPlayerId = null;
    BigInteger italyPlayerId = null;
    
    // Save all three players
    savePlayer(indiaPlayer);
    savePlayer(usaPlayer);
    savePlayer(italyPlayer);
    
    indiaPlayerId = indiaPlayer.getId();
    System.out.println("Indian Player Id:" + indiaPlayerId);
    
    usaPlayerId = usaPlayer.getId();
    System.out.println("Usa Player Id:" + usaPlayerId);
    
    italyPlayerId = italyPlayer.getId();
    System.out.println("Italy Player Id:" + italyPlayerId);
    
    assertNotNull("Indian Player must have been persisted", getShardPlayer(indiaPlayerId));
    assertNotNull("Usa Player must have been persisted", getShardPlayer(usaPlayerId));
    assertNotNull("Italy Player must have been persisted", getShardPlayer(italyPlayerId));
    
    // Ensure that the appropriate shards contain the players    
    assertExistsOnlyOnShard("Indian Player should have existed on only shard 0", 0, indiaPlayerId);    
    assertExistsOnlyOnShard("Usa Player should have existed only on shard 1", 1, usaPlayerId);    
    assertExistsOnlyOnShard("Italian Player should have existed only on shard 2", 2, italyPlayerId);    
  }
  
  private void savePlayer(NationalPlayer player) {
    Session session = null;
    Transaction tx = null;
    try {
      session = HibernateUtil.getSession();
      tx = session.beginTransaction();
      session.save(player);
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }
  
  @Test
  public void testSimpleCriteira() throws Exception {
    savePlayer(indiaPlayer);
    savePlayer(usaPlayer);
    savePlayer(italyPlayer);
    
    Session session = HibernateUtil.getSession();
    Transaction tx = session.beginTransaction();
    
    try {
      Criteria c = session.createCriteria(NationalPlayer.class)
        .add(Restrictions.eq("country", Country.INDIA));
      List<NationalPlayer> players = c.list();
      assertTrue("Should only return the sole Indian Player", players.size() == 1);
      assertContainsPlayers(players, indiaPlayer);
      
      c = session.createCriteria(NationalPlayer.class)
        .add(Restrictions.gt("careerGoals", 50));
      players = c.list();
      assertEquals("Should return the usa and india players", 2, players.size());
      assertContainsPlayers(players, indiaPlayer, usaPlayer);
      
      c = session.createCriteria(NationalPlayer.class)
        .add(Restrictions.between("worldRanking", 5, 15));
      players = c.list();
      assertEquals("Should only have the usa player", 1, players.size());
      assertContainsPlayers(players, usaPlayer);
      
      c = session.createCriteria(NationalPlayer.class)
        .add(Restrictions.eq("lastName", "Acharya"));
      players = c.list();
      assertEquals("All Players should be found as they have same last name", 3, players.size());
      assertContainsPlayers(players, indiaPlayer, usaPlayer, italyPlayer);
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      throw e;
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }
  
  /**
   * Simple Method that ensures that the list contains all the players provided.
   * 
   * @param playerList List of Players
   * @param players    Expected players 
   */
  private void assertContainsPlayers(List<NationalPlayer> playerList, NationalPlayer ...players) {
    assertTrue("Not all players are found in the list", playerList.containsAll(Arrays.asList(players)));    
  }
  
  /**
   * Method that asserts that a player defined by {@code playerId} exists only
   * on the correct shard and not others.
   * 
   * @param message Display message
   * @param shardId Expected to exist in this shard only
   * @param playerId Player identifier
   */
  private void assertExistsOnlyOnShard(String message, int shardId, BigInteger playerId) {
    for (int i = 0; i < 3; i++) {
      NationalPlayer player = getDirectPlayer(playerId, i);

      if (i != shardId) {
        assertNull(message, player);
      }
      else {
        assertNotNull(message, player);
      }
    }
  }
  
  /**
   * Gets the National Player by using Hibernate Shards.
   * 
   * @param id Identifier of player
   * @return   National Player or {@code null} if not found
   */
  private NationalPlayer getShardPlayer(BigInteger id) {
    Session session = null;
    try {
      session = HibernateUtil.getSession();
      return getNationalPlayer(id, session);
    }
    finally {
      if (session != null) {
        session.close();
      }
    }
  }

  /**
   * Gets a Player from the sharded database directly.
   * 
   * @param id      Player identefier
   * @param shardId Shard database id.
   * @return        NationalPlayer or {@code null} if not found
   */
  private NationalPlayer getDirectPlayer(BigInteger id, int shardId) {
    Session session = null;
    try {
      session = HibernateUtil.getShardDbSession(shardId);
      return getNationalPlayer(id, session);
    }
    finally {
      if (session != null) {
        session.close();
      }
    }
  }

  private NationalPlayer getNationalPlayer(BigInteger id, Session session) throws HibernateException {

    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      NationalPlayer NationalPlayer = (NationalPlayer) session.get(NationalPlayer.class, id);
      tx.commit();

      return NationalPlayer;
    }
    catch (HibernateException e) {
      if (tx != null) {
        tx.rollback();
      }
      throw e;
    }
  }
}
