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
import modelo.dao.VendaDao;
import modelo.entidades.Venda;

public class VendaDaoJDBC implements VendaDao{

	private Connection conn;
	
	public VendaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Venda obj) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"INSERT INTO venda "
					+ "(preco_total, data_hora_venda, fk_id_cliente, fk_id_funcionario) "
					+ "VALUES "
					+ "(?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS
					);
			
			st.setDouble(1, obj.getPrecoTotal());
			st.setDate(2, new java.sql.Date(obj.getDataHoraVenda().getTime()));
			st.setInt(3, obj.getCliente().getIdCliente());
			st.setInt(4, obj.getFuncionario().getIdFuncionario());
			
			Integer linhasAfetadas = st.executeUpdate();
			
			if (linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					obj.setIdVenda(rs.getInt(1));
				}
				BD.fecharResultSet(rs);
			} else {
				throw new BDException("Ocorreu um erro! ");
			}
			
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		} finally {
			BD.fecharStatement(st);
		}	
	}

	@Override
	public void update(Venda obj) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"UPDATE venda "
					+ "SET preco_total = ?, data_hora_venda = ?, fk_id_cliente = ?, "
					+ "fk_id_funcionario = ? "
					+ "WHERE "
					+ "id_venda = ?"
					);
			
			st.setDouble(1, obj.getPrecoTotal());
			st.setDate(2, new java.sql.Date(obj.getDataHoraVenda().getTime()));
			st.setInt(3, obj.getCliente().getIdCliente());
			st.setInt(4, obj.getFuncionario().getIdFuncionario());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		} finally {
			BD.fecharStatement(st);
		}
		
	}

	@Override
	public List<Venda> acharTodos() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Venda> lista = new ArrayList<>();
		
		try {
			
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM "
					+ "venda"
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
			BD.fecharStatement(st);
		}			
	}	
}
