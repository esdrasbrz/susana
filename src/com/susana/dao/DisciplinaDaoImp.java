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
		
		// listagem
		List<Disciplina> lista = session.createCriteria(Disciplina.class).list();
		
		// fecha a sessao
		session.close();
		return lista;
	}

	@Override
	public void salvar(Disciplina disciplina) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		session.save(disciplina);
		t.commit();
		session.close();
	}

	@Override
	public Disciplina pesquisa(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Disciplina disciplina = (Disciplina) session.createSQLQuery("select * from Disciplina where id = " + id).addEntity(Disciplina.class).list().get(0);
		
		session.close();
		return disciplina;
	}
}
