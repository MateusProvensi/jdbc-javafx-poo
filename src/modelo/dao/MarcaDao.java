package modelo.dao;

import java.util.List;

import modelo.entidades.Marca;

public interface MarcaDao {

	void insert(Marca obj);
	
	void update(Marca obj);
	
	List<Marca> acharTodos();
	
}
