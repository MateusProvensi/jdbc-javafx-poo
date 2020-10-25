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
import modelo.dao.FornecedorMarcaDao;
import modelo.entidades.FornecedorMarca;

public class FornecedorMarcaDaoJDBC implements FornecedorMarcaDao{

	private Connection conn;
	
	public FornecedorMarcaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(FornecedorMarca obj) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"INSERT INTO fornecedor_marca "
					+ "(fk_id_fornecedor, fk_id_marca) "
					+ "VALUES "
					+ "(?, ?)",
					Statement.RETURN_GENERATED_KEYS
					);
			
			st.setInt(1, obj.getFornecedor().getIdFornecedor());
			st.setInt(2, obj.getMarca().getIdMarca());
			
			Integer linhasAfetadas = st.executeUpdate();
			
			if (linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					obj.setIdFornecedorMarca(rs.getInt(1));
				}
				BD.fecharResultSet(rs);
			} else {
				throw new BDException("Ocorreu um erro! Nenhuma ");
			}
			
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		} finally {
			BD.fecharStatement(st);
		}
		
	}

	@Override
	public void update(FornecedorMarca obj) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"UPDATE fornecedor_marca "
					+ "SET fk_id_fornecedor = ?, fk_id_marca = ? "
					+ "WHERE "
					+ "id_fornecedor_marca = ?"
					);
			
			st.setInt(1, obj.getFornecedor().getIdFornecedor());
			st.setInt(2, obj.getMarca().getIdMarca());
			st.setInt(3, obj.getIdFornecedorMarca());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		} finally {
			BD.fecharStatement(st);
		}
		
		
	}

	@Override
	public List<FornecedorMarca> acharTodos() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<FornecedorMarca> lista = new ArrayList<>();
		
		try {
			
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM "
					+ "fornecedor_marca"
					);
			
			rs = st.executeQuery();
			
			while (rs.next()) {
				// INSTANCIAR A ENTIDADE
				// ADICIONAR NA LISTA
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
