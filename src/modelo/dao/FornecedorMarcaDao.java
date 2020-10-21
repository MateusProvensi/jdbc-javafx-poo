package modelo.dao;

import java.util.List;

import modelo.entidades.FornecedorMarca;

public interface FornecedorMarcaDao {

	void insert(FornecedorMarca obj);
	
	void update(FornecedorMarca obj);
	
	List<FornecedorMarca> acharTodos();
	
}
