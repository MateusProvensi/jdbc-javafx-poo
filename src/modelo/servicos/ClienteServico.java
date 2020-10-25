package modelo.servicos;

import java.util.List;

import modelo.dao.ClienteDao;
import modelo.dao.DaoCriacao;
import modelo.entidades.Cliente;

public class ClienteServico {

	private ClienteDao dao = DaoCriacao.criarClienteDao();
	
	public List<Cliente> acharTodos(){
		return dao.acharTodos();
	}
	
	public void insertOuUpdate(Cliente obj) {
		if (obj.getIdCliente() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void delete(Cliente obj) {
		dao.deletePeloId(obj.getIdCliente());
	}
}
