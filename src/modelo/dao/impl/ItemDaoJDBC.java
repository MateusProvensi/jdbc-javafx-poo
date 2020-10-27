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
import modelo.dao.ItemDao;
import modelo.entidades.Empresa;
import modelo.entidades.Fornecedor;
import modelo.entidades.FornecedorMarca;
import modelo.entidades.Item;
import modelo.entidades.Marca;

public class ItemDaoJDBC implements ItemDao{

	private Connection conn;
	
	public ItemDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Item obj) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"INSERT INTO item "
					+ "(descricao_item, codigo_barras, preco_venda, "
					+ "quantidade, validade, corredor, fk_id_fornecedor_marca) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS
					);
			
			st.setString(1, obj.getDescricaoItem());
			st.setString(2, obj.getCodigoBarras());
			st.setDouble(3, obj.getPrecoVenda());
			st.setInt(4, obj.getQuantidade());
			st.setDate(5, new java.sql.Date(obj.getValidade().getTime()));
			st.setString(6, obj.getCorredor());
			st.setInt(7, obj.getFornecedorMarca().getIdFornecedorMarca());
			
			Integer linhasAfetadas = st.executeUpdate();
			
			if (linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					obj.setIdItem(rs.getInt(1));
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
	public void update(Item obj) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"UPDATE item "
					+ "SET descricao_item = ?, codigo_barras = ?, preco_venda = ?, "
					+ " quantidade = ?, validade = ?, corredor = ?, "
					+ "fk_id_fornecedor_marca = ? "
					+ "WHERE "
					+ "id_item = ?"
					);
			
			st.setString(1, obj.getDescricaoItem());
			st.setString(2, obj.getCodigoBarras());
			st.setDouble(3, obj.getPrecoVenda());
			st.setInt(4, obj.getQuantidade());
			st.setDate(5, new java.sql.Date(obj.getValidade().getTime()));
			st.setString(6, obj.getCorredor());
			st.setInt(7, obj.getFornecedorMarca().getIdFornecedorMarca());
			st.setInt(8, obj.getIdItem());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		} finally {
			BD.fecharStatement(st);
		}
		
	}

	@Override
	public List<Item> acharTodos() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Item> lista = new ArrayList<>();
		
		try {
			/*
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM "
					+ "item"
					);
			*/
			
			st = conn.prepareStatement(
					"SELECT DISTINCT item.*, fornecedor_marca.*, fornecedor.*, marca.*, empresa.* "
					+ "FROM item "
					+ "INNER JOIN empresa, marca, fornecedor_marca , fornecedor "
					+ "WHERE item.fk_id_fornecedor_marca = fornecedor_marca.id_fornecedor_marca "
					+ "AND fornecedor.id_fornecedor = fornecedor_marca.fk_id_fornecedor "
					+ "AND marca.id_marca = fornecedor_marca.fk_id_marca "
					+ "AND fornecedor.fk_id_empresa = empresa.id_empresa "
					+ "AND marca.fk_id_empresa = id_empresa "
					+ "ORDER BY item.id_item;"
					);
			
			rs = st.executeQuery();
			
			Map<Integer, FornecedorMarca> mapFornecedorMarca = new HashMap<>();
			Map<Integer, Fornecedor> mapFornecedor = new HashMap<>();
			Map<Integer, Marca> mapMarca = new HashMap<>();
			Map<Integer, Empresa> mapEmpresa = new HashMap<>();
			
			while (rs.next()) {
				
				Empresa empresa = mapEmpresa.get(rs.getInt("id_empresa"));
				Fornecedor fornecedor = mapFornecedor.get(rs.getInt("id_fornecedor"));
				Marca marca = mapMarca.get(rs.getInt("id_marca"));
				FornecedorMarca fornecedorMarca = mapFornecedorMarca.get(rs.getInt("id_fornecedor_marca"));
				
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
				
				Item obj = InstanciacaoEntidades.instanciarItem(rs, fornecedorMarca);
				lista.add(obj);
			}
			return lista;
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		} finally {
			BD.fecharResultSet(rs);
			BD.fecharResultSet(rs);
		}
	}
}
