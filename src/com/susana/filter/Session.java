package com.susana.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.hibernate.FlushMode;

import com.susana.dao.HibernateUtil;

/**
 * Servlet Filter implementation class Session
 */
@WebFilter("/*")
public class Session implements Filter {

	/*
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// abre a session e transaction
		HibernateUtil.getSession().setFlushMode(FlushMode.ALWAYS);
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
		
		// fecha a sessao hibernate e da commit
		HibernateUtil.getSession().flush();
		HibernateUtil.closeSession();
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
