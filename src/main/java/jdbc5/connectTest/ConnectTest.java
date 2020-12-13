package jdbc5.connectTest;

/*
launch bd
then launche this app
java -classpath .:driverJAR test.TestDB

for tracing file:
jdbs:derby://localhost:1527/COREJAVA;create=true;traceFile=trace.out

*/

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class ConnectTest {
    public static void main(String[] args) throws IOException {
        try{
            runTest();
        }catch (SQLException ex){
            for(Throwable t :ex){
                t.printStackTrace();
            }
        }
    }
    public static void runTest() throws SQLException, IOException{

        try(Connection conn = getConnection(); Statement stat = conn.createStatement()){
            stat.executeUpdate("CREATE TABLE Greetings1 (Message CHAR(20))");
            stat.executeUpdate("INSERT INTO Greetings1 VALUES ('Hello, Java!')");

            try(ResultSet result = stat.executeQuery("SELECT * FROM Greetings1")){
                if(result.next())
                    System.out.println(result.getString(1));
            }
            stat.executeUpdate("DROP TABLE Greetings1");
        }
    }
    public static Connection getConnection() throws SQLException, IOException{

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = DriverManager.getConnection("jdbc:derby:derbyDB;create=true");
        return connection;
//        Properties props = new Properties();
//        try(InputStream in = Files.newInputStream(Paths.get("database.properties"))){
//            props.load(in);
//        }
//
//        String drivers = props.getProperty("jdbc.drivers");
//        if(drivers != null) System.setProperty("jdbc.drivers", drivers);
//
//        String url = props.getProperty("jdbc.url");
//        String username = props.getProperty("jdbc.username");
//        String password = props.getProperty("jdbc.password");
//
//        return DriverManager.getConnection(url, username, password);

    }
}
