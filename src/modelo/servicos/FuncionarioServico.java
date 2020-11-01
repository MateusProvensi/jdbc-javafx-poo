package modelo.servicos;

import java.util.List;

import modelo.dao.DaoCriacao;
import modelo.dao.FuncionarioDao;
import modelo.entidades.Funcionario;

public class FuncionarioServico {

	FuncionarioDao dao = DaoCriacao.criarFuncionarioDao();
	
	public List<Funcionario> acharTodos(){
		return dao.acharTodos();
	}
	
	public void insertOuUpdate(Funcionario obj) {
		if (obj.getIdFuncionario() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void delete(Integer id) {
		dao.deletePeloId(id);
	}
	
}
