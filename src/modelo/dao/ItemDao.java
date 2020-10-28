package modelo.dao;

import java.util.List;

import modelo.entidades.Item;

public interface ItemDao {

	void insert(Item obj);
	
	void update(Item obj);
	
	void deletePeloId(Integer id);
	
	List<Item> acharTodos();
	
}
