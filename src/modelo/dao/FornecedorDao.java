package modelo.dao;

import java.util.List;

import modelo.entidades.Fornecedor;

public interface FornecedorDao {

	void insert(Fornecedor obj);
	
	void update(Fornecedor obj);
	
	void deletePeloId(Integer id);
	
	List<Fornecedor> acharTodos();
	
}
