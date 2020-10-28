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
import modelo.dao.FuncionarioDao;
import modelo.entidades.Funcionario;

public class FuncionarioDaoJDBC implements FuncionarioDao{

	private Connection conn;
	
	public FuncionarioDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Funcionario obj) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"INSERT INTO funcionario "
					+ "(nome_funcionario, sobrenome_funcionario, cpf_funcionario, "
					+ "rg_funcionario, telefone_funcionario, numero_caixa) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS
					);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getSobrenome());
			st.setString(3, obj.getCpf());
			st.setString(4, obj.getRg());
			st.setString(5, obj.getTelefone());
			st.setInt(6, obj.getNumeroCaixa());
			
			Integer linhasAfetadas = st.executeUpdate();
			
			if (linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					obj.setIdFuncionario(rs.getInt(1));
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
	public void update(Funcionario obj) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"UPDATE funcionario "
					+ "SET nome_funcionario = ?, sobrenome_funcionario = ?, cpf_funcionario = ?, "
					+ "rg_funcionario = ?, telefone_funcionario = ?, numero_caixa = ? "
					+ "WHERE "
					+ "id_funcionario = ?"
					);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getSobrenome());
			st.setString(3, obj.getCpf());
			st.setString(4, obj.getRg());
			st.setString(5, obj.getTelefone());
			st.setInt(6, obj.getNumeroCaixa());
			st.setInt(7, obj.getIdFuncionario());
			
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		} finally {
			BD.fecharStatement(st);
		}
		
	}

	@Override
	public List<Funcionario> acharTodos() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Funcionario> lista = new ArrayList<>();
		
		try {
			
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM "
					+ "funcionario"
					);
			
			rs = st.executeQuery();
			
			while (rs.next()) {
				Funcionario obj = InstanciacaoEntidades.instanciarFuncionario(rs);
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
					"DELETE FROM funcionario "
					+ "WHERE "
					+ "id_funcionario = ?"
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
