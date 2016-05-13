package com.susana.dao;

import org.hibernate.Session;

import com.susana.model.Login;

public class LoginDaoImp implements LoginDao {

	@Override
	public boolean verificaLogin(Login login) {
		Session session = HibernateUtil.getSession();
		
		// recebe o login do banco de dados
		Login login_bd = (Login) session.createCriteria(Login.class).list().get(0); // retorna o primeiro login
		
		// retorna se os logins sao iguais
		return login.equals(login_bd);
	}

}
