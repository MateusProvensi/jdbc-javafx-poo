package modelo.servicos;

import java.util.List;

import modelo.dao.DaoCriacao;
import modelo.dao.FornecedorDao;
import modelo.entidades.Fornecedor;

public class FornecedorServico {

	FornecedorDao dao = DaoCriacao.criarFornecedorDao();
	
	public List<Fornecedor> acharTodos(){
		return dao.acharTodos();
	}
	
	public void insertOuUpdate(Fornecedor obj) {
		if (obj.getIdFornecedor() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void delete(Integer id) {
		dao.deletePeloId(id);
	}
	
}
