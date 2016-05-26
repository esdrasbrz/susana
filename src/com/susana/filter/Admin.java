package com.susana.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.susana.model.Login;

/**
 * Servlet Filter para manipular as requisicoes ao administrativo (/admin)
 */
@WebFilter("/admin/*")
public class Admin implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		
		// recebe o login da session
		Login login = (Login) session.getAttribute("login");
		
		if (login == null) { // usuario nao esta logado
			// redireciona para a tela de login
			((HttpServletResponse) response).sendRedirect("/susana/login.susy");
			return;
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
	
	@Override
	public void destroy() {
	}
}
