package modelo.dao;

import java.util.List;

import modelo.entidades.Venda;

public interface VendaDao {

	void insert(Venda obj);
	
	void update(Venda obj);
	
	List<Venda> acharTodos();
	
}
