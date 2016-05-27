package com.susana.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.susana.dao.DisciplinaDao;
import com.susana.dao.DisciplinaDaoImp;
import com.susana.model.Disciplina;

@ManagedBean
@SessionScoped
public class DisciplinasController {
	//private static final String SUSANA_FILES = "/home/esdrasbrz/Projects/java/susana/susana-files/"; // developer
	private static final String SUSANA_FILES = "/opt/tomcat/webapps/susana-files/"; // server
	private List<Disciplina> disciplinas;
	private Disciplina disciplina;
	private DisciplinaDao dao;
	
	public DisciplinasController() {
		dao = new DisciplinaDaoImp();
		disciplina = new Disciplina();
		disciplinas = dao.listar();
	}
	
	// Seleciona a disciplina
	public String selDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
		
		return "/labs";
	}
	
	// adiciona uma disciplina
	public void adicionar() {
		// salva a disciplina no BD
		dao.salvar(disciplina);
		
		try {
			// abre o script em python
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine scriptEngine = manager.getEngineByName("python");
			scriptEngine.eval(new FileReader(SUSANA_FILES + "testes.py"));
			Invocable invocable = (Invocable) scriptEngine;
			
			// cria o diretorio da disciplina
			invocable.invokeFunction("cria_disciplina", disciplina.getNome());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Adicionado com sucesso!", ""));
		disciplina = new Disciplina();
		disciplinas = dao.listar();
	}
	
	// getters and setters
	
	public List<Disciplina> getDisciplinas() {
		// lista todas as disciplinas
		disciplinas = dao.listar();
		return disciplinas;
	}

	public void setDisciplinas(List<Disciplina> disciplinas) {
		this.disciplinas = disciplinas;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}
}
