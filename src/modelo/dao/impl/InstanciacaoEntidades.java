package modelo.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import modelo.entidades.Cliente;
import modelo.entidades.Empresa;
import modelo.entidades.Fornecedor;
import modelo.entidades.FornecedorMarca;
import modelo.entidades.Funcionario;
import modelo.entidades.Item;
import modelo.entidades.ItemVenda;
import modelo.entidades.Marca;
import modelo.entidades.Venda;

public class InstanciacaoEntidades {

	public static Cliente instanciarCliente(ResultSet rs) throws SQLException {
		Cliente obj = new Cliente();
		obj.setIdCliente(rs.getInt("id_cliente"));
		obj.setNome(rs.getString("nome_cliente"));
		obj.setSobrenome(rs.getString("sobrenome_cliente"));
		obj.setCpf(rs.getString("cpf_cliente"));
		obj.setRg(rs.getString("rg_cliente"));
		obj.setTelefone(rs.getString("telefone_cliente"));

		return obj;
	}
	
	public static Empresa instanciarEmpresa(ResultSet rs) throws SQLException{
		Empresa obj = new Empresa();
		obj.setIdEmpresa(rs.getInt("id_empresa"));
		obj.setNome(rs.getString("nome_empresa"));
		obj.setCnpj(rs.getString("cnpj_empresa"));
		obj.setTelefone(rs.getString("telefone_empresa"));
		
		return obj;
	}
	
	public static Fornecedor instanciarFornecedor(ResultSet rs, Empresa empresa) throws SQLException{
		Fornecedor obj = new Fornecedor();
		obj.setIdFornecedor(rs.getInt("id_fornecedor"));
		obj.setNome(rs.getString("nome_fornecedor"));
		obj.setSobrenome(rs.getString("sobrenome_fornecedor"));
		obj.setCpf(rs.getString("cpf_fornecedor"));
		obj.setRg(rs.getString("rg_fornecedor"));
		obj.setTelefone(rs.getString("telefone_fornecedor"));
		obj.setDataUltimaVisita(rs.getDate("data_ultima_visita"));
		obj.setEmpresa(empresa);
				
		return obj;
	}
	
	public static FornecedorMarca instanciarFornecedorMarca(ResultSet rs, Fornecedor fornecedor, 
			Marca marca) throws SQLException{
		FornecedorMarca obj = new FornecedorMarca();
		obj.setIdFornecedorMarca(rs.getInt("id_fornecedor_marca"));
		obj.setFornecedor(fornecedor);
		obj.setMarca(marca);
		
		return obj;
	}
	
	public static Funcionario instanciarFuncionario(ResultSet rs) throws SQLException{
		Funcionario obj = new Funcionario();
		obj.setIdFuncionario(rs.getInt("id_funcionario"));
		obj.setNome(rs.getString("nome_funcionario"));
		obj.setSobrenome(rs.getString("sobrenome_funcionario"));
		obj.setCpf(rs.getString("cpf_funcionario"));
		obj.setRg(rs.getString("rg_funcionario"));
		obj.setTelefone(rs.getString("telefone_funcionario"));
		obj.setNumeroCaixa(rs.getInt("numero_caixa"));
		
		return obj;
	}
	
	public static Item instanciarItem(ResultSet rs, FornecedorMarca fornecedorMarca) throws SQLException {
		Item obj = new Item();
		obj.setIdItem(rs.getInt("id_item"));
		obj.setDescricaoItem(rs.getString("descricao_item"));
		obj.setCodigoBarras(rs.getString("codigo_barras"));
		obj.setPrecoVenda(rs.getDouble("preco_venda"));
		obj.setQuantidade(rs.getInt("quantidade"));
		obj.setValidade(rs.getDate("validade"));
		obj.setCorredor(rs.getString("corredor"));
		obj.setFornecedorMarca(fornecedorMarca);
		
		return obj;
	}
	
	public static ItemVenda instanciarItemVenda(ResultSet rs, Item item, Venda venda) throws SQLException {
		ItemVenda obj = new ItemVenda();
		obj.setIdItemVenda(rs.getInt("id_item_venda"));
		obj.setItem(item);
		obj.setVenda(venda);
		
		return obj;
	}
	
	public static Marca instanciarMarca(ResultSet rs, Empresa empresa) throws SQLException{
		Marca obj = new Marca();
		obj.setIdMarca(rs.getInt("id_marca"));
		obj.setNome(rs.getString("nome_marca"));
		obj.setCnpj(rs.getString("id_cnpj"));
		obj.setEmpresa(empresa);
		
		return obj;
	}
}
