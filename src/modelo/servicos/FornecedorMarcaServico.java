package modelo.servicos;

import java.util.List;

import modelo.dao.DaoCriacao;
import modelo.dao.FornecedorMarcaDao;
import modelo.entidades.FornecedorMarca;

public class FornecedorMarcaServico {

	FornecedorMarcaDao dao = DaoCriacao.criarFornecedorMarcaDao();
	
	public List<FornecedorMarca> acharTodos(){
		return dao.acharTodos();
	}
	
	public void insertOuUpdate(FornecedorMarca obj) {
		if (obj.getIdFornecedorMarca() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void delete(Integer id) {
		dao.deletePeloId(id);
	}
	
}
