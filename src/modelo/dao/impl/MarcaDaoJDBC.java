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
import modelo.dao.MarcaDao;
import modelo.entidades.Empresa;
import modelo.entidades.Marca;

public class MarcaDaoJDBC implements MarcaDao{

	Connection conn;
	
	public MarcaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Marca obj) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"INSERT INTO marca "
					+ "(nome_marca, cnpj_marca, fk_id_empresa) "
					+ "VALUES "
					+ "(?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS
					);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getCnpj());
			st.setInt(3, obj.getEmpresa().getIdEmpresa());
			
			Integer linhasAfetadas = st.executeUpdate();
			
			if (linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					obj.setIdMarca(rs.getInt(1));
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
	public void update(Marca obj) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement(
					"UPDATE marca "
					+ "SET nome_marca = ?, cnpj_marca = ?, fk_id_empresa = ? "
					+ "WHERE id_marca = ?"
					);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getCnpj());
			st.setInt(3, obj.getEmpresa().getIdEmpresa());
			st.setInt(4, obj.getIdMarca());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		} finally {
			BD.fecharStatement(st);
		}
		
	}

	@Override
	public List<Marca> acharTodos() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Marca> lista = new ArrayList<>();
		
		try {
			
			st = conn.prepareStatement(
					"SELECT marca.*, empresa.* "
					+ "FROM marca "
					+ "INNER JOIN empresa "
					+ "WHERE marca.fk_id_empresa = empresa.id_empresa "
					+ "ORDER BY marca.id_marca"
					);
			
			rs = st.executeQuery();
			
			Map<Integer, Empresa> map = new HashMap<>();
			while(rs.next()) {
				
				Empresa empresa = map.get(rs.getInt("id_empresa"));
				
				if (empresa == null) {
					empresa = InstanciacaoEntidades.instanciarEmpresa(rs);
					map.put(rs.getInt("id_empresa"), empresa);
				}
				
				Marca obj =InstanciacaoEntidades.instanciarMarca(rs, empresa);
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
