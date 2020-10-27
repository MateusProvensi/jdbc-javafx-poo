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
import modelo.dao.FornecedorMarcaDao;
import modelo.entidades.Empresa;
import modelo.entidades.Fornecedor;
import modelo.entidades.FornecedorMarca;
import modelo.entidades.Marca;

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
			
			/*
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM "
					+ "fornecedor_marca"
					);
			*/ //JUNTAR AS 3 TABELAS
			
			st = conn.prepareStatement(
					"SELECT DISTINCT fornecedor_marca.*, fornecedor.*, marca.*, empresa.* "
					+ "FROM fornecedor "
					+ "INNER JOIN empresa, marca, fornecedor_marca "
					+ "WHERE fornecedor.id_fornecedor = fornecedor_marca.fk_id_fornecedor "
					+ "AND marca.id_marca = fornecedor_marca.fk_id_marca "
					+ "AND fornecedor.fk_id_empresa = empresa.id_empresa "
					+ "AND marca.fk_id_empresa = id_empresa "
					+ "ORDER BY fornecedor_marca.id_fornecedor_marca"
					);
			
			rs = st.executeQuery();
			
			Map<Integer, Fornecedor> mapFornecedor = new HashMap<>();
			Map<Integer, Marca> mapMarca = new HashMap<>();
			Map<Integer, Empresa> mapEmpresa = new HashMap<>();
			
			while (rs.next()) {
				
				Empresa empresa = mapEmpresa.get(rs.getInt("id_empresa"));
				Fornecedor fornecedor = mapFornecedor.get(rs.getInt("id_fornecedor"));
				Marca marca = mapMarca.get(rs.getInt("id_marca"));
					
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
				}
				
				FornecedorMarca obj = InstanciacaoEntidades.instanciarFornecedorMarca(rs, fornecedor, marca);
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
