package modelo.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import bd.BD;
import bd.BDException;
import modelo.dao.ItemVendaDao;
import modelo.entidades.ItemVenda;

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
		List<ItemVenda> lista = null;
		
		try {
			
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM "
					+ "item_venda"
					);
			
			rs = st.executeQuery();
			
			while (rs.next()) {
				// INSTANCIAR OBJETOS
				// ADC NA LISTA
			}
			return lista;
			
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		} finally {
			BD.fecharResultSet(rs);
			BD.fecharStatement(st);
		}		
	}
}
