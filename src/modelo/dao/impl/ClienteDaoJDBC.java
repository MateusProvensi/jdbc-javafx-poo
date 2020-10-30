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
import modelo.dao.ClienteDao;
import modelo.entidades.Cliente;

public class ClienteDaoJDBC implements ClienteDao{

	private Connection conn;
	
	
	public ClienteDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Cliente obj) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"INSERT INTO cliente "
					+ " (nome_cliente, sobrenome_cliente, cpf_cliente, rg_cliente, telefone_cliente) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS
					);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getSobrenome());
			st.setString(3, obj.getCpf());
			st.setString(4, obj.getRg());
			st.setString(5, obj.getTelefone());
			
			Integer linhasAfetadas = st.executeUpdate();
			
			if (linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					obj.setIdCliente(rs.getInt(1));
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
	public void update(Cliente obj) {

		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"UPDATE cliente "
					+ "SET nome_cliente = ?, sobrenome_cliente = ?, cpf_cliente = ?, "
					+ "rg_cliente = ?, telefone_cliente = ? "
					+ "WHERE "
					+ "id_cliente = ?"
					);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getSobrenome());
			st.setString(3, obj.getCpf());
			st.setString(4, obj.getRg());
			st.setString(5, obj.getTelefone());
			st.setInt(6, obj.getIdCliente());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		} finally {
			BD.fecharStatement(st);
		}
		
	}

	@Override
	public List<Cliente> acharTodos() {
		
		List<Cliente> lista = new ArrayList<>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM cliente"
					);
			
			rs = st.executeQuery();
			
			while (rs.next()) {
				Cliente obj = InstanciacaoEntidades.instanciarCliente(rs);
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
					"DELETE FROM cliente "
					+ "WHERE "
					+ "id_cliente = ?"
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
