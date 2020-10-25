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
import modelo.dao.EmpresaDao;
import modelo.entidades.Empresa;

public class EmpresaDaoJDBC implements EmpresaDao{

	private Connection conn;
	
	public EmpresaDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Empresa obj) {
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"INSERT INTO empresa "
					+ "(nome_empresa, cnpj_empresa, telefone_empresa) "
					+ "VALUES "
					+ "(?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS
					);
			
			st.setString(1,obj.getNome());
			st.setString(2,obj.getCnpj());
			st.setString(3,obj.getTelefone());
			
			Integer linhasAfetadas = st.executeUpdate();
			if (linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					obj.setIdEmpresa(rs.getInt(1));
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
	public void update(Empresa obj) {
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"UPDATE empresa "
					+ "SET nome_empresa = ?, cnpj_empresa = ?, telefone_empresa = ? "
					+ "WHERE "
					+ "id_empresa = ?"
					);
		
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		}
		
	}

	@Override
	public List<Empresa> acharTodos() {
		PreparedStatement st = null;
		List<Empresa> lista = new ArrayList<>();
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement(
					"Select * "
					+ "FROM "
					+ "empresa"
					);
			
			rs = st.executeQuery();
			
			while (rs.next()) {
				Empresa obj = InstanciacaoEntidades.instanciarEmpresa(rs);
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
}
