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
	//private static final String SUSANA_FILES = "/home/esdrasbrz/Projects/java/susana/susana-files/"; // developer
	private static final String SUSANA_FILES = "/opt/tomcat/webapps/susana-files/"; // server
	private LabDao dao;
	private Lab lab;
	private List<String> mensagens;
	private List<String> compilacao;
	private String sources; // codigos fontes para compilar

	public LabController() {
		dao = new LabDaoImp();
		lab = new Lab();
		sources = "";
	}

	// Selecina o Lab
	public String selLab(Lab lab) {
		this.lab = lab;

		return "/submeter";
	}

	// salva cada arquivo na lista
	public void upload(FileUploadEvent event) {
		UploadedFile arq = event.getFile();
		
		FacesContext fCtx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
		String sessionId = session.getId();
		
		try {
			// abre o script em python
			ScriptEngineManager manager = new ScriptEngineManager();

			ScriptEngine scriptEngine = manager.getEngineByName("python");
			scriptEngine.eval(new FileReader(SUSANA_FILES + "testes.py"));
			Invocable invocable = (Invocable) scriptEngine;
			
			// cria um diretorio para executar o programa do usuario
			invocable.invokeFunction("new_user", sessionId, lab.getDisciplina().getNome(), lab.getNome());
			
			// salva o arquivo
			byte[] contents = arq.getContents();
			
			// seta o nome do arquivo e o diretorio com base na sessionId
			String fileName = arq.getFileName();
			String path = SUSANA_FILES + lab.getDisciplina().getNome() + "/" + lab.getNome() + "/" + sessionId
					+ "/";
			
			try {
				// salva o arquivo
				FileOutputStream file = new FileOutputStream(new File(path + fileName));
				file.write(contents);
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// verifica se o arquivo termina em .c
			if (fileName.charAt(fileName.length() - 1) == 'c')
				sources += SUSANA_FILES + lab.getDisciplina().getNome() + "/" + lab.getNome() + "/" + sessionId + "/" + fileName + " ";
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	// Submete os arquivos
	public String submeter() {
		FacesContext fCtx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
		String sessionId = session.getId();

		try {
			// abre o script em Python
			ScriptEngineManager manager = new ScriptEngineManager();

			ScriptEngine scriptEngine = manager.getEngineByName("python");
			scriptEngine.eval(new FileReader(SUSANA_FILES + "testes.py"));
			Invocable invocable = (Invocable) scriptEngine;

			// faz os testes
			mensagens = new ArrayList<String>();
			compilacao = new ArrayList<String>();

			// recebe a saida do comando
			String erro = (String) invocable.invokeFunction("compilar", sources, lab.getDisciplina().getNome(),
					lab.getNome(), sessionId);

			// verifica se compilacao esta vazia
			if (erro == null || erro.isEmpty()) {
				compilacao.add("Nenhuma mensagem");
			} else {
				// adiciona todas as linhas separadamente em compilacao
				for (String linha : erro.split("\n")) {
					compilacao.add(linha);
				}
			}

			// verifica se não houve erro na compilacao
			if (!erro.contains("error: ")) {
				// seta as permissoes de execucao no arquivo
				invocable.invokeFunction("set_permissao", sessionId, lab.getDisciplina().getNome(), lab.getNome());

				// percorre todos os testes
				for (int i = 1; i <= lab.getQtdTestes(); i++) {
					// executa o programa para a entrada i e compara
					erro = (String) invocable.invokeFunction("testar", sessionId, lab.getDisciplina().getNome(),
							lab.getNome(), i);

					// verifica se deu algum erro
					if (!erro.isEmpty()) {
						mensagens.add("Teste " + i + ": Falhou:");

						// adiciona todas as linhas separadamente
						for (String linha : erro.split("\n")) {
							mensagens.add(linha);
						}
					} else {
						mensagens.add("Teste " + i + ": OK!");
					}

					// pula uma linha
					mensagens.add("");
				}
			} else { // houve erro na compilacao
				mensagens.add("Erro de compilação!");
			}

			// apaga o executavel e o(s) codigo(s) fonte(s)
			invocable.invokeFunction("limpar", sessionId, lab.getDisciplina().getNome(), lab.getNome());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} finally {
			// zera os arquivos fonte
			sources = "";
		}

		return "/exibir";
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
			scriptEngine.eval(new FileReader(SUSANA_FILES + "testes.py"));
			Invocable invocable = (Invocable) scriptEngine;

			// cria o diretorio do lab e baixa os testes
			invocable.invokeFunction("cria_lab", lab.getDisciplina().getNome(), lab.getNome(), lab.getUrlTestes(),
					lab.getQtdTestes());
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

	public List<String> getCompilacao() {
		return compilacao;
	}

	public void setCompilacao(List<String> compilacao) {
		this.compilacao = compilacao;
	}
}
