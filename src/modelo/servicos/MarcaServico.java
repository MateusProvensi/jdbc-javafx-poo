package modelo.servicos;

import java.util.List;

import modelo.dao.DaoCriacao;
import modelo.dao.MarcaDao;
import modelo.entidades.Marca;

public class MarcaServico {

	MarcaDao dao = DaoCriacao.criarMarcaDao();
	
	public List<Marca> acharTodos(){
		return dao.acharTodos();
	}
	
	public void insertOuUpdate(Marca obj) {
		if (obj.getIdMarca() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void delete(Integer id) {
		dao.deletePeloId(id);
	}
	
}
