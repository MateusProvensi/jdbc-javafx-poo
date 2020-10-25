package bd;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class BD {

	private static Connection conn = null;
	
	public static Connection inicarConnection() {
		if (conn == null) {
			try {
				
				Properties propriedades = carregarPropriedades();
				String url = propriedades.getProperty("dburl");
				conn = DriverManager.getConnection(url, propriedades);
				
			} catch (SQLException e) {
				throw new BDException(e.getMessage());
			}
		}
		
		return conn;
	}

	private static Properties carregarPropriedades() {
		try (FileInputStream fs = new FileInputStream("bd.properties")){
			
			Properties propriedades = new Properties();
			propriedades.load(fs);
			return propriedades;
			
		} catch (IOException e) {
			throw new BDException(e.getMessage());
		}
	}
	
	public static void fecharConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		}
	}
	
	public static void fecharStatement(Statement st) {
		try {
			st.close();
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		}
	}
	
	public static void fecharResultSet(ResultSet rs) {
		try {
			rs.close();
		} catch (SQLException e) {
			throw new BDException(e.getMessage());
		}
	}
}
