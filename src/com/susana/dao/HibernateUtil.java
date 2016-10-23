package com.susana.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.service.ServiceRegistry;

import com.susana.model.Disciplina;
import com.susana.model.Lab;

public class HibernateUtil {
	private static SessionFactory sessionFactory;
	private static Session session;

	private HibernateUtil() {
	}

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				// create the SessionFactory from standard (hibernate.cfg.xml)
				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();
				MetadataSources metadataSources = new MetadataSources(serviceRegistry);
				Metadata metadata = metadataSources.buildMetadata();
				sessionFactory = metadata.buildSessionFactory();
			} catch (Throwable ex) {
				// Log the exception
				System.err.println("Initial SessionFactory creation failed." + ex);
				throw new ExceptionInInitializerError(ex);
			}
			return sessionFactory;
		} else {
			return sessionFactory;
		}
	}
	
	public static Session getSession() {
		try {
			session.beginTransaction();
		} catch (Exception e) {
			try {
				session = getSessionFactory().openSession();
			} catch (JDBCConnectionException exception) {
				// inicia novamente a sessao
				sessionFactory = null;
				session = getSessionFactory().openSession();
			}
		}
		
		return session;
	}
	
	public static void closeSession() {
		session.close();
	}
}
