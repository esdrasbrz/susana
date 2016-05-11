package com.susana.controller;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpSession;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.susana.dao.DisciplinaDaoImp;
import com.susana.dao.LabDao;
import com.susana.dao.LabDaoImp;
import com.susana.model.Lab;

@ManagedBean
@SessionScoped
public class LabController {
	private LabDao dao;
	private Lab lab;
	private List<String> mensagens;

	public LabController() {
		dao = new LabDaoImp();
		lab = new Lab();
	}

	// Selecina o Lab
	public String selLab(Lab lab) {
		this.lab = lab;

		return "/submeter";
	}

	// Faz upload do código fonte e checagem
	public void upload(FileUploadEvent event) {
		UploadedFile uploadedFile = event.getFile();
		byte[] contents = uploadedFile.getContents();

		FacesContext fCtx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
		String sessionId = session.getId();

		// seta o nome do arquivo com base na sessionId
		String fileName = sessionId + ".c";
		String path = "/opt/tomcat/webapps/susana-files/" + lab.getDisciplina().getNome() + "/"
				+ lab.getNome() + "/";

		try {
			// salva o arquivo
			FileOutputStream file = new FileOutputStream(new File(path + fileName));
			file.write(contents);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// faz os testes
		mensagens = new ArrayList<String>();

		try {
			// abre o script em Python
			ScriptEngineManager manager = new ScriptEngineManager();
			
			ScriptEngine scriptEngine = manager.getEngineByName("python");
			scriptEngine.eval(new FileReader("/opt/tomcat/webapps/susana-files/testes.py"));
			Invocable invocable = (Invocable) scriptEngine;

			// recebe a saida do comando
			String erro = (String) invocable.invokeFunction("compilar", fileName, lab.getDisciplina().getNome(), lab.getNome());

			// verifica se não houve erro na compilacao
			if (!erro.contains("error: ")) {
				// percorre todos os testes
				for (int i = 1; i <= lab.getQtdTestes(); i++) {
					// executa o programa para a entrada i e compara
					erro = (String) invocable.invokeFunction("testar", fileName, lab.getDisciplina().getNome(), lab.getNome(), i);

					// verifica se deu algum erro
					if (!erro.isEmpty()) {
						mensagens.add("Teste " + i + ": Falhou:\n" + erro);
					} else {
						mensagens.add("Teste " + i + ": OK!");
					}
				}
			} else { // houve erro na compilacao
				mensagens.add("Erro de compilação:\n" + erro);
			}

			// apaga o executavel e o codigo fonte
			invocable.invokeFunction("limpar", fileName, lab.getDisciplina().getNome(), lab.getNome());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	// adiciona um novo lab
	public void adicionar() {
		lab.setDisciplina(new DisciplinaDaoImp().pesquisa(lab.getDisciplina().getId()));
		// salva o lab no BD
		dao.salvar(lab);
		
		try {
			// abre o script em python
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine scriptEngine = manager.getEngineByName("python");
			scriptEngine.eval(new FileReader("/opt/tomcat/webapps/susana-files/testes.py"));
			Invocable invocable = (Invocable) scriptEngine;
			
			// cria o diretorio do lab e baixa os testes
			invocable.invokeFunction("cria_lab", lab.getDisciplina().getNome(), lab.getNome(), lab.getUrlTestes(), lab.getQtdTestes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Adicionado com sucesso!", ""));
		lab = new Lab();
	}
	
	// getters and setters

	public Lab getLab() {
		return lab;
	}

	public void setLab(Lab lab) {
		this.lab = lab;
	}

	public List<String> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<String> mensagens) {
		this.mensagens = mensagens;
	}
}
