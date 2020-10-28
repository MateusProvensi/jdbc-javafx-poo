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
import modelo.dao.FornecedorDao;
import modelo.entidades.Empresa;
import modelo.entidades.Fornecedor;

public class FornecedorDaoJDBC implements FornecedorDao{

	private Connection conn;
	
	public FornecedorDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Fornecedor obj) {
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"INSERT INTO fornecedor "
					+ "(nome_fornecedor, sobrenome_fornecedor, cpf_fornecedor, "
					+ "rg_fornecedor, telefone_fornecedor, data_ultima_visita, fk_id_empresa) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS
					);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getSobrenome());
			st.setString(3, obj.getCpf());
			st.setString(4, obj.getRg());
			st.setString(5, obj.getTelefone());
			st.setDate(6, new java.sql.Date(obj.getDataUltimaVisita().getTime()));
			st.setInt(7, obj.getEmpresa().getIdEmpresa());
			
			
			Integer linhasAfetadas = st.executeUpdate();
			
			if (linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					obj.setIdFornecedor(rs.getInt(1));
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
	public void update(Fornecedor obj) {
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"UPDATE fornecedor "
					+ "SET nome_fornecedor = ?, sobrenome_fornecedor = ?, cpf_fornecedor =?, "
					+ "rg_fornecedor = ?, telefone_fornecedor = ?, data_ultima_visita = ?, "
					+ "fk_id_empresa = ? "
					+ "WHERE "
					+ "id_fornecedor = ?"
					);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getSobrenome());
			st.setString(3, obj.getCpf());
			st.setString(4, obj.getRg());
			st.setString(5, obj.getTelefone());
			st.setDate(6, new java.sql.Date(obj.getDataUltimaVisita().getTime()));
			st.setInt(7, obj.getEmpresa().getIdEmpresa());
			st.setInt(8, obj.getIdFornecedor());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		} finally {
			BD.fecharStatement(st);
		}
	}

	@Override
	public List<Fornecedor> acharTodos() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Fornecedor> lista = new ArrayList<>();
		
		try {
			/*
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM "
					+ "fornecedor"
					);
			Caso o código abaixo não de certo tem esse acima para "BACKUP"
			*/
			
			st = conn.prepareStatement(
					"SELECT fornecedor.*, empresa.* "
					+ "FROM fornecedor INNER JOIN empresa "
					+ "WHERE fornecedor.fk_id_empresa = empresa.id_empresa"
					);
			rs = st.executeQuery();
			
			Map<Integer, Empresa> map = new HashMap<>();
			
			while (rs.next()) {
				
				Empresa empresa = map.get(rs.getInt("id_empresa"));
				
				if (empresa == null) {
					empresa = InstanciacaoEntidades.instanciarEmpresa(rs);
					map.put(rs.getInt("id_empresa"), empresa);
				}
				
				Fornecedor obj = InstanciacaoEntidades.instanciarFornecedor(rs, empresa);
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
					"DELETE FROM fornecedor "
					+ "WHERE "
					+ "id_fornecedor = ?"
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
