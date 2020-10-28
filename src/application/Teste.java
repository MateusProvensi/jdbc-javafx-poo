package application;

import java.util.List;

import modelo.dao.ClienteDao;
import modelo.dao.DaoCriacao;
import modelo.dao.ItemVendaDao;
import modelo.entidades.Cliente;
import modelo.entidades.ItemVenda;

public class Teste {

	public static void main(String[] args) {
		
		ClienteDao clienteDao = DaoCriacao.criarClienteDao();
		List<Cliente> clientes = clienteDao.acharTodos();
	
		for (Cliente cliente : clientes) {
			System.out.println(cliente);
		}
		
		System.out.println();
		
		ItemVendaDao itemVendaDao = DaoCriacao.criarItemVendaDao();
		List<ItemVenda> lista = itemVendaDao.acharTodos();
		
		for (ItemVenda itemVenda : lista) {
			System.out.println(itemVenda);
		}
		
	}
	
}
