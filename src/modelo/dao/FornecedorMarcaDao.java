package modelo.dao;

import java.util.List;

import modelo.entidades.FornecedorMarca;

public interface FornecedorMarcaDao {

	void insert(FornecedorMarca obj);
	
	void update(FornecedorMarca obj);
	
	void deletePeloId(Integer id);
	
	List<FornecedorMarca> acharTodos();
	
}
