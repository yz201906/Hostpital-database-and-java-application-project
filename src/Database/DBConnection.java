package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    static final String       jdbcURL = "jdbc:oracle:thin:@orca.csc.ncsu.edu:1521:orcl01";

    private static Connection _dbConnection;

    private DBConnection () {
    }

    public static Connection getConnection () {
        if ( _dbConnection == null ) {
            try {
                Class.forName( "oracle.jdbc.driver.OracleDriver" );
                final String user = "";
                final String password = "";
                _dbConnection = DriverManager.getConnection( jdbcURL, user, password );

            }
            catch(SQLException ex)
            {
            	System.out.println("Login denied: " + ex.getMessage());
            }
            catch ( final Throwable exception ) {
            }
        }

        return _dbConnection;
    }

    public static void close ( final Connection conn ) {
        if ( conn != null ) {
            try {
                conn.close();
            }
            catch ( final Throwable whatever ) {
            }
        }
    }

    public static void close ( final Statement st ) {
        if ( st != null ) {
            try {
                st.close();
            }
            catch ( final Throwable whatever ) {
            }
        }
    }

    public static void close ( final ResultSet rs ) {
        if ( rs != null ) {
            try {
                rs.close();
            }
            catch ( final Throwable whatever ) {
            }
        }
    }

}
