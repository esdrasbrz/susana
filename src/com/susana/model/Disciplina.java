package com.susana.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Disciplina implements Serializable, Comparable<Disciplina> {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@Column(nullable = false, length = 50)
	private String nome;
	@OneToMany(mappedBy = "disciplina")
	private List<Lab> labs;
	
	// Constructors
	
	public Disciplina() {
	}
	
	public Disciplina(int id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}
	
	// Getters and Setters
	
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

	public List<Lab> getLabs() {
		return labs;
	}

	public void setLabs(List<Lab> labs) {
		this.labs = labs;
	}

	@Override
	public int compareTo(Disciplina o) {
		return this.id - o.getId();
	}
}
