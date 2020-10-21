package modelo.dao;

import java.util.List;

import modelo.entidades.Cliente;

public interface ClienteDao {

	void insert(Cliente obj);
	
	void update(Cliente obj);
	
	List<Cliente> acharTodos();
	
}
