package modelo.dao;

import bd.BD;
import modelo.dao.impl.ClienteDaoJDBC;
import modelo.dao.impl.EmpresaDaoJDBC;
import modelo.dao.impl.FornecedorDaoJDBC;
import modelo.dao.impl.FornecedorMarcaDaoJDBC;
import modelo.dao.impl.FuncionarioDaoJDBC;
import modelo.dao.impl.ItemDaoJDBC;
import modelo.dao.impl.ItemVendaDaoJDBC;
import modelo.dao.impl.MarcaDaoJDBC;
import modelo.dao.impl.VendaDaoJDBC;

public class DaoCriacao {

	public static ClienteDao criarClienteDao(){
		return new ClienteDaoJDBC(BD.inicarConnection());
	}
	
	public static EmpresaDao criarEmpresaDao() {
		return new EmpresaDaoJDBC(BD.inicarConnection());
	}
	
	public static FornecedorDao criarFornecedorDao() {
		return new FornecedorDaoJDBC(BD.inicarConnection());
	}
	
	public static FornecedorMarcaDao criarFornecedorMarcaDao() {
		return new FornecedorMarcaDaoJDBC(BD.inicarConnection());
	}
	
	public static FuncionarioDao criarFuncionarioDao() {
		return new FuncionarioDaoJDBC(BD.inicarConnection());
	}
	
	public static ItemDao criarItemDao() {
		return new ItemDaoJDBC(BD.inicarConnection());
	}
	
	public static ItemVendaDao criarItemVendaDao() {
		return new ItemVendaDaoJDBC(BD.inicarConnection());
	}
	
	public static MarcaDao criarMarcaDao() {
		return new MarcaDaoJDBC(BD.inicarConnection());
	}
	
	public static VendaDao criarVendaDao() {
		return new VendaDaoJDBC(BD.inicarConnection());
	}
}