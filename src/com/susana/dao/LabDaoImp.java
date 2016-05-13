package com.susana.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.susana.model.Lab;

public class LabDaoImp implements LabDao {

	@Override
	public void salvar(Lab lab) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.save(lab);
		t.commit();
		session.close();
	}
}
