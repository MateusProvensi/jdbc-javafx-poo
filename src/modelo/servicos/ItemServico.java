package modelo.servicos;

import java.util.List;

import modelo.dao.DaoCriacao;
import modelo.dao.ItemDao;
import modelo.entidades.Item;

public class ItemServico {

	ItemDao dao = DaoCriacao.criarItemDao();
	
	public List<Item> acharTodos(){
		return dao.acharTodos();
	}
	
	public void insertOuUpdate(Item obj) {
		if (obj.getIdItem() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void delete(Integer id) {
		dao.deletePeloId(id);
	}
	
}
