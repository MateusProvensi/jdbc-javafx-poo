package modelo.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bd.BD;
import bd.BDException;
import modelo.dao.ItemDao;
import modelo.entidades.Item;

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
			
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM "
					+ "item"
					);
			
			rs = st.executeQuery();
			
			while (rs.next()) {
				// INSTANCIAR O OBJETO
				// ADC NA LISTA
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
