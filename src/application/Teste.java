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
		
		Cliente cliente = new Cliente(5, "Hugo", "Assunçao", "93624826905", "446916547", "(46)293263618");
		clienteDao.update(cliente);
		
		System.out.println();
		
		ItemVendaDao itemVendaDao = DaoCriacao.criarItemVendaDao();
		List<ItemVenda> lista = itemVendaDao.acharTodos();
		
		for (ItemVenda itemVenda : lista) {
			System.out.println(itemVenda);
		}
		
	}
	
}
