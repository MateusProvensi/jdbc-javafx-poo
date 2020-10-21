package modelo.dao;

import java.util.List;

import modelo.entidades.Fornecedor;

public interface FornecedorDao {

	void insert(Fornecedor obj);
	
	void update(Fornecedor obj);
	
	List<Fornecedor> acharTodos();
	
}
