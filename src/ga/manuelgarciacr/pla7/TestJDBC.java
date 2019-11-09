package ga.manuelgarciacr.pla7;


import java.sql.DriverManager;
import java.sql.Connection;

public class TestJDBC {
	public static void main(String[] args) {
		String bd = "instituto";
		String jdbcUrl = "jdbc:mysql://localhost:3306/" + bd + "?useSSL=false&serverTimezone=UTC";
		String user = "root";
		String pass = "";
		try {
			System.out.println("Conectando: " + jdbcUrl);
			Connection myConn = DriverManager.getConnection(jdbcUrl, user, pass);
			System.out.println("Todo bien. Circulen.");
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}
