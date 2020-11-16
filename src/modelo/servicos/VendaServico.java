package modelo.servicos;

import java.util.List;

import modelo.dao.DaoCriacao;
import modelo.dao.VendaDao;
import modelo.entidades.Venda;

public class VendaServico {

	VendaDao dao = DaoCriacao.criarVendaDao();
	
	public List<Venda> acharTodos(){
		return dao.acharTodos();
	}
	
	public void insertOuUpdate(Venda obj) {
		if (obj.getIdVenda() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void delete(Integer id) {
		dao.deletePeloId(id);
	}
	
	public Venda acharPeloId(Integer id) {
		Venda obj = dao.acharPeloId(id);
		return obj;
	}
	
}
