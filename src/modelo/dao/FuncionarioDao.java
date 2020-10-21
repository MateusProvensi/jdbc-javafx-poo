package modelo.dao;

import java.util.List;

import modelo.entidades.Funcionario;

public interface FuncionarioDao {

	void insert(Funcionario obj);
	
	void update(Funcionario obj);
	
	List<Funcionario> acharTodos();
	
}
