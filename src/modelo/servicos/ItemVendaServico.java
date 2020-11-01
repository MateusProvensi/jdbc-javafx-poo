package modelo.servicos;

import java.util.List;

import modelo.dao.DaoCriacao;
import modelo.dao.ItemVendaDao;
import modelo.entidades.ItemVenda;

public class ItemVendaServico {

	ItemVendaDao dao = DaoCriacao.criarItemVendaDao();
	
	public List<ItemVenda> acharTodos(){
		return dao.acharTodos();
	}
	
	public void insertOuUpdate(ItemVenda obj) {
		if (obj.getIdItemVenda() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void delete(Integer id) {
		dao.deletePeloId(id);
	}
	
}
