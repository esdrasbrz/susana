package com.susana.dao;

import java.util.List;

import com.susana.model.Disciplina;

public interface DisciplinaDao {
	// realiza a listagem de todas as disciplinas
	List<Disciplina> listar();
	
	// salva uma disciplina nova
	void salvar(Disciplina disciplina);
	
	// retorna a disciplina com base no id
	Disciplina pesquisa(int id);
}
