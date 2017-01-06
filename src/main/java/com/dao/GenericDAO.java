package com.dao;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.util.LogUtil;
import com.util.SessionUtil;

public abstract class GenericDAO {
	Session session;

	public GenericDAO() {
		session = SessionUtil.getSessionFactory().getCurrentSession();
	}

	public <E> List<E> findAll(final Class<E> entityClass) {
		LogUtil.log("finding all " + entityClass.getName() + " instances",
				Level.INFO, null);

		try {
			if (session.getTransaction() != null
					&& session.getTransaction().isActive()) {
				session.getTransaction();
			} else {
				session.beginTransaction();
			}

			Query q = session.createQuery("select model from "
					+ entityClass.getName() + " model");
			return q.list();

		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public <E> boolean save(E entity) {
		LogUtil.log("saving " + entity.getClass().getName() + " instance",
				Level.INFO, null);
		boolean isSaved = false;
		Transaction tx = null;

		try {
			if (session.getTransaction() != null
					&& session.getTransaction().isActive()) {
				tx = session.getTransaction();
			} else {
				tx = session.beginTransaction();
			}
			session.clear();
			session.save(entity.getClass().getName(), entity);
			session.flush();
			tx.commit();

			LogUtil.log("save successful", Level.INFO, null);
			isSaved = true;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			tx.rollback();
			throw re;
		}
		return isSaved;
	}

	public <E> List<E> findByListOfIds(final Class<E> entityClass,
			final String identifierColName, final Collection<Integer> idsList) {
		LogUtil.log("find by list of Ids  " + entityClass.getName()
				+ " instances", Level.INFO, null);
		try {
			if (session.getTransaction() != null
					&& session.getTransaction().isActive()) {
				session.getTransaction();
			} else {
				session.beginTransaction();
			}

			final String queryString = "SELECT FROM " + entityClass.getName()
					+ " o WHERE o." + identifierColName + " IN (:idsList)";
			Query q = session.createQuery(queryString).setParameter("idsList",
					idsList);

			return q.list();

		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public <E> E findById(Class<E> entity, Integer id) {
		LogUtil.log("finding " + entity.getName() + " instance with id: " + id,
				Level.INFO, null);
		try {
			if (session.getTransaction() != null
					&& session.getTransaction().isActive()) {
				session.getTransaction();
			} else {
				session.beginTransaction();
			}

			Query q = session.createQuery("select model from "
					+ entity.getName() + " model WHERE model.id = :id");
			return (E) q.setParameter("id", id).uniqueResult();

		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

}
