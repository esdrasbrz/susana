package com.susana.dao;

import java.util.List;

import org.hibernate.Session;

import com.susana.model.Disciplina;

public class DisciplinaDaoImp implements DisciplinaDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Disciplina> listar() {
		Session session = HibernateUtil.getSession();
		
		// listagem
		List<Disciplina> lista = session.createCriteria(Disciplina.class).list();
		
		return lista;
	}

	@Override
	public void salvar(Disciplina disciplina) {
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		session.save(disciplina);
		session.getTransaction().commit();
	}

	@Override
	public Disciplina pesquisa(int id) {
		Session session = HibernateUtil.getSession();
		Disciplina disciplina = (Disciplina) session.createSQLQuery("select * from Disciplina where id = " + id).addEntity(Disciplina.class).list().get(0);
		
		return disciplina;
	}
}
