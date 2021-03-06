package dao.alignprivate;

import enums.Term;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import model.alignprivate.Electives;

import java.util.List;

public class ElectivesDao {
  private SessionFactory factory;
  private Session session;

  public SessionFactory getFactory() {
    return factory;
  }

  public Session getSession() {
    return session;
  }

  public ElectivesDao(boolean test) {
    if (test) {
      this.factory = StudentTestSessionFactory.getFactory();
    } else {
      this.factory = StudentSessionFactory.getFactory();
    }
  }


  /**
   * This is the function to add an Elective for a given student into database.
   *
   * @param elective elective to be added; not null.
   * @return true if insert successfully. Otherwise throws exception.
   */
  public Electives addElective(Electives elective) {
    Transaction tx = null;

    try {
      session = factory.openSession();
      tx = session.beginTransaction();
      session.save(elective);
      tx.commit();
    } catch (HibernateException e) {
      if (tx != null) tx.rollback();
      throw new HibernateException(e);
    } finally {
      session.close();
    }

    return elective;
  }

  /**
   * Update an elective.
   *
   * @param elective
   * @return true if update successfully.
   */
  public boolean updateElectives(Electives elective) {
    Transaction tx = null;
    try {
      session = factory.openSession();
      tx = session.beginTransaction();
      session.saveOrUpdate(elective);
      tx.commit();
    } catch (HibernateException e) {
      if (tx != null) tx.rollback();
      throw new HibernateException(e);
    } finally {
      session.close();
    }
    return true;
  }

  /**
   * Get a unique elective from database
   *
   * @param neuId
   * @param courseId
   * @param term
   * @param year
   * @return
   */
  public Electives getElective(String neuId, String courseId, Term term, int year) {
    boolean find = false;
    Electives elective = null;

    try {
      session = factory.openSession();
      org.hibernate.query.Query query = session.createQuery("FROM Electives WHERE neuId = :neuId " +
              "AND courseId = :courseId " +
              "AND courseTerm = :courseTerm " +
              "AND courseYear = :courseYear");
      query.setParameter("neuId", neuId);
      query.setParameter("courseId", courseId);
      query.setParameter("courseTerm", term);
      query.setParameter("courseYear", year);
      List list = query.list();
      if (!list.isEmpty()) {
        elective = (Electives) list.get(0);
      }
    } finally {
      session.close();
    }

    return elective;
  }
}
