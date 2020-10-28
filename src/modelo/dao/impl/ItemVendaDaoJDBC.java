package modelo.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bd.BD;
import bd.BDException;
import modelo.dao.ItemVendaDao;
import modelo.entidades.Cliente;
import modelo.entidades.Empresa;
import modelo.entidades.Fornecedor;
import modelo.entidades.FornecedorMarca;
import modelo.entidades.Funcionario;
import modelo.entidades.Item;
import modelo.entidades.ItemVenda;
import modelo.entidades.Marca;
import modelo.entidades.Venda;

public class ItemVendaDaoJDBC implements ItemVendaDao{
	
	private Connection conn;
	
	public ItemVendaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(ItemVenda obj) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"INSERT INTO item_venda "
					+ "(fk_id_item, fk_id_venda) "
					+ "VALUES "
					+ "(?, ?)", 
					Statement.RETURN_GENERATED_KEYS
					);
			
			st.setInt(1, obj.getItem().getIdItem());
			st.setInt(2, obj.getVenda().getIdVenda());
			
			Integer linhasAfetadas = st.executeUpdate();
			
			if (linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					obj.setIdItemVenda(rs.getInt(1));
				}
				BD.fecharResultSet(rs);
			} else {
				throw new BDException("Ocorreu um erro! Nenhuma linha foi alterada");
			}
			
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		} finally {
			BD.fecharStatement(st);
		}		
	}

	@Override
	public void update(ItemVenda obj) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"UPDATE item_venda "
					+ "SET fk_id_item = ?, fk_id_venda = ? "
					+ "WHERE "
					+ "id_item_venda = ?"
					);
			
			st.setInt(1, obj.getItem().getIdItem());
			st.setInt(2, obj.getVenda().getIdVenda());
			st.setInt(3, obj.getIdItemVenda());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			BD.fecharStatement(st);
		}
		
	}

	@Override
	public List<ItemVenda> acharTodos() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<ItemVenda> lista = new ArrayList<>();
		
		try {
			/*
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM "
					+ "item_venda"
					);
			*/
			
			st = conn.prepareStatement(
					"SELECT DISTINCT item_venda.*, venda.*, funcionario.*, cliente.*, item.*, fornecedor_marca.*, "
					+ "fornecedor.*, marca.*, empresa.* "
					+ "FROM item_venda "
					+ "INNER JOIN empresa, marca, fornecedor_marca , fornecedor, item, cliente, funcionario, venda "
					+ "WHERE item.fk_id_fornecedor_marca = fornecedor_marca.id_fornecedor_marca "
					+ "AND fornecedor.id_fornecedor = fornecedor_marca.fk_id_fornecedor "
					+ "AND marca.id_marca = fornecedor_marca.fk_id_marca "
					+ "AND fornecedor.fk_id_empresa = empresa.id_empresa "
					+ "AND marca.fk_id_empresa = id_empresa "
					+ "AND venda.fk_id_cliente = id_cliente "
					+ "AND venda.fk_id_funcionario = funcionario.id_funcionario "
					+ "AND item_venda.fk_id_item = item.id_item "
					+ "AND item_venda.fk_id_venda = venda.id_venda "
					+ "ORDER BY item_venda.id_item_venda"
					);
			
			rs = st.executeQuery();
			
			Map<Integer, FornecedorMarca> mapFornecedorMarca = new HashMap<>();
			Map<Integer, Fornecedor> mapFornecedor = new HashMap<>();
			Map<Integer, Marca> mapMarca = new HashMap<>();
			Map<Integer, Empresa> mapEmpresa = new HashMap<>();
			Map<Integer, Cliente> mapCliente = new HashMap<>();
			Map<Integer, Funcionario> mapFuncionario = new HashMap<>();
			Map<Integer, Item> mapItem = new HashMap<>();
			Map<Integer, Venda> mapVenda = new HashMap<>();
			
			while (rs.next()) {
				
				Empresa empresa = mapEmpresa.get(rs.getInt("id_empresa"));
				Fornecedor fornecedor = mapFornecedor.get(rs.getInt("id_fornecedor"));
				Marca marca = mapMarca.get(rs.getInt("id_marca"));
				FornecedorMarca fornecedorMarca = mapFornecedorMarca.get(rs.getInt("id_fornecedor_marca"));
				Cliente cliente = mapCliente.get(rs.getInt("id_cliente"));
				Funcionario funcionario = mapFuncionario.get(rs.getInt("id_funcionario"));
				Item item = mapItem.get(rs.getInt("id_item"));
				Venda venda = mapVenda.get(rs.getInt("id_venda"));
				
				if (empresa == null) {
					empresa = InstanciacaoEntidades.instanciarEmpresa(rs);
					mapEmpresa.put(rs.getInt("id_empresa"), empresa);
				}
				if (fornecedor == null) {
					fornecedor = InstanciacaoEntidades.instanciarFornecedor(rs, empresa);
					mapFornecedor.put(rs.getInt("id_fornecedor"), fornecedor);
				}
				if (marca == null) {
					marca = InstanciacaoEntidades.instanciarMarca(rs, empresa);
					mapMarca.put(rs.getInt("id_marca"), marca);
				}
				if (fornecedorMarca == null) {
					fornecedorMarca = InstanciacaoEntidades.instanciarFornecedorMarca(rs, fornecedor, marca);
					mapFornecedorMarca.put(rs.getInt("id_fornecedor_marca"), fornecedorMarca);
				}
				if (cliente == null) {
					cliente = InstanciacaoEntidades.instanciarCliente(rs);
					mapCliente.put(rs.getInt("id_cliente"), cliente);
				}
				if (funcionario == null) {
					funcionario = InstanciacaoEntidades.instanciarFuncionario(rs);
					mapFuncionario.put(rs.getInt("id_funcionario"), funcionario);
				}
				if (item == null) {
					item = InstanciacaoEntidades.instanciarItem(rs, fornecedorMarca);
					mapItem.put(rs.getInt("id_item"), item);
				}
				if (venda == null) {
					venda = InstanciacaoEntidades.instanciarVenda(rs, funcionario, cliente);
					mapVenda.put(rs.getInt("id_venda"), venda);
				}
				
				ItemVenda obj = InstanciacaoEntidades.instanciarItemVenda(rs, item, venda);
				lista.add(obj);
			}
			return lista;
			
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		} finally {
			BD.fecharResultSet(rs);
			BD.fecharStatement(st);
		}		
	}

	@Override
	public void deletePeloId(Integer id) {
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"DELETE FROM item_venda "
					+ "WHERE "
					+ "id_item_venda = ?"
					);
			
			st.setInt(1, id);
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		} finally {
			BD.fecharStatement(st);
		}
		
	}
}
