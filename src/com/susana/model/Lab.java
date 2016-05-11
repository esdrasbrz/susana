package com.susana.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class Lab implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@Column(nullable = false, length = 50)
	private String nome;
	private int qtdTestes;
	private String urlTestes;
	@ManyToOne
	@JoinColumn(name = "disciplina_id")
	private Disciplina disciplina;
	
	// Constructors
	
	public Lab() {
		this.disciplina = new Disciplina();
	}

	public Lab(int id, String nome, int qtdTestes, String urlTestes, Disciplina disciplina) {
		super();
		this.id = id;
		this.nome = nome;
		this.qtdTestes = qtdTestes;
		this.urlTestes = urlTestes;
		this.disciplina = disciplina;
	}

	// getters and setters
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getQtdTestes() {
		return qtdTestes;
	}

	public void setQtdTestes(int qtdTestes) {
		this.qtdTestes = qtdTestes;
	}

	public String getUrlTestes() {
		return urlTestes;
	}

	public void setUrlTestes(String urlTestes) {
		this.urlTestes = urlTestes;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}	
}
