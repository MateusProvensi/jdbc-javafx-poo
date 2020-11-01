package modelo.servicos;

import java.util.List;

import modelo.dao.DaoCriacao;
import modelo.dao.EmpresaDao;
import modelo.entidades.Empresa;

public class EmpresaServico {

	private EmpresaDao dao = DaoCriacao.criarEmpresaDao();
	
	public List<Empresa> acharTodos(){
		return dao.acharTodos();
	}
		
	public void insertOuUpdate(Empresa obj) {
		if (obj.getIdEmpresa() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void delete(Empresa obj) {
		dao.deletePeloId(obj.getIdEmpresa());
	}
	
}
