package com.susana.dao;

import com.susana.model.Login;

public interface LoginDao {
	// verifica o login
	boolean verificaLogin(Login login);
}