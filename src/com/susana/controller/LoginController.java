package com.susana.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.susana.dao.LoginDao;
import com.susana.dao.LoginDaoImp;
import com.susana.model.Login;

@ManagedBean
@SessionScoped
public class LoginController {
	private LoginDao dao;
	private Login login;
	
	public LoginController() {
		dao = new LoginDaoImp();
		login = new Login();
	}
	
	// metodo para efetuar o login
	public String logar() {
		// verifica se o login esta correto
		if (dao.verificaLogin(login)) {
			// recebe a session
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			
			// seta o usuario na session
			session.setAttribute("login", login);
			
			return "/admin/cadastro";
		}
			
		// aqui o login estava incorreto
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login inválido!", "Usuário ou senha incorretos."));
		return "/login";
	}
	
	// efetua logout
	public String logout() {
		// recebe a session
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		
		// seta nulo no login da session
		session.setAttribute("login", null);
		login = new Login();
		
		return "/disciplinas";
	}
	
	// getters and setters
	
	public Login getLogin() {
		return login;
	}
	public void setLogin(Login login) {
		this.login = login;
	}
}
