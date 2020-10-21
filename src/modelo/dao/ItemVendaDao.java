package modelo.dao;

import java.util.List;

import modelo.entidades.ItemVenda;

public interface ItemVendaDao {

	void insert(ItemVenda obj);
	
	void update(ItemVenda obj);
	
	List<ItemVenda> acharTodos();
	
}
