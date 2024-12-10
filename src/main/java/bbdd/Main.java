package bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    // @TODO Sistituye xxx por los parámetros de tu conexión
    private final static String DB_SERVER = "localhost";
    private final static int DB_PORT = 3306;
    private final static String DB_NAME = "titanic_spaceship";
    private final static String DB_USER = "root";
    private final static String DB_PASS = "";
    private static Connection conn;

    public static void main(String[] args) throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://" + DB_SERVER + ":" + DB_PORT + "/" + DB_NAME;
        conn = DriverManager.getConnection(url, DB_USER, DB_PASS);

        // @TODO Prueba sus funciones
        // 1. Añade los planetas a la base de datos
        nuevoPlaneta("Kepler-186f", 3.3e24, 8800, "Copernico");
        nuevoPlaneta("HD 209458 b", 1.4e27, 100000, "Beta Pictoris");
        nuevoPlaneta("LHS 1140 b", 8.3e24, 8800, "Copernico");
        // 2. Muestra por pantalla la lista de pasajeros de la cabina A-60-S
        // 3. Muestra por pantalla una lista de sistemas, planetas y número de pasajeros con origen en ellos

        conn.close();


    }


    private static void nuevoPlaneta (String nombre, Double masa, Integer radio, String sistema) throws SQLException {
        // @TODO Añade planetas a la base de datos
        try{
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO planetas (nombre, masa, radio, sistema) VALUES (?,?,?,?)");
            stmt.setString(1, nombre);
            if (masa == null) {
                stmt.setNull(2, java.sql.Types.DECIMAL); // Si masa es nulo, insertar NULL (si no se hace asi salta nullPointerException)
            } else {
                stmt.setDouble(2, masa); // Si no, insertar el valor de masa
            }
            if (radio == null) {
                stmt.setNull(3, java.sql.Types.DECIMAL);  // lo mismo que pasaba con la masa
            } else {
                stmt.setInt(3, radio);  //
            }
            stmt.setString(4,sistema);
            stmt.executeUpdate();
            System.out.println("Creado el planeta \""+ nombre + "\"");
        } catch(SQLException ex){
            if (ex.getMessage().contains("Duplicate entry"))
                System.out.println("Ya existe el planeta en la BBDD. No es necesario crearlo");
            else
            {
                System.out.println(ex);
                System.exit(1);
            }
        }


    }

    private static void listaPasajerosCabina (String cubierta, int cabina, String lado) throws SQLException {
        // @TODO Muestra por pantalla una lista de pasajeros de una cabina
        
    }

    private static void listaOrigenes() throws SQLException {
        // @TODO Muestra por pantalla una lista de planetas, sistemas y número de pasajeros provinientes de ellos

    }
}

