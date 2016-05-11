package com.susana.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.susana.model.Disciplina;

public class DisciplinaDaoImp implements DisciplinaDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Disciplina> listar() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		return session.createCriteria(Disciplina.class).list();
	}

	@Override
	public void salvar(Disciplina disciplina) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.save(disciplina);
		t.commit();
	}

	@Override
	public Disciplina pesquisa(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		return (Disciplina) session.createSQLQuery("select * from Disciplina where id = " + id).addEntity(Disciplina.class).list().get(0);
	}
}
