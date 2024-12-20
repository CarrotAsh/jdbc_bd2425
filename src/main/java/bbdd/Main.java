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

        try{
            // @TODO Prueba sus funciones
            // 1. Añade los planetas a la base de datos
            nuevoPlaneta("Kepler-186f", 3.3e24, 8800, "Copernico");
            nuevoPlaneta("HD 209458 b", 1.4e27, 100000, "Beta Pictoris");
            nuevoPlaneta("LHS 1140 b", 8.3e24, 8800, "Copernico");

            // 2. Muestra por pantalla la lista de pasajeros de la cabina A-60-S
            listaPasajerosCabina("A",60,"S");

            // 3. Muestra por pantalla una lista de sistemas, planetas y número de pasajeros con origen en ellos
            listaOrigenes();
        }catch (SQLException ex){
            System.err.println("Error en la base de datos: " + ex.getMessage());
        }
        conn.close();
    }

    private static void nuevoPlaneta (String nombre, double masa, int radio, String sistema) throws SQLException {
        // @TODO Añade planetas a la base de datos
        PreparedStatement stmt = null;
        try{
            stmt = conn.prepareStatement("INSERT INTO planetas (nombre, masa, radio, sistema) VALUES (?,?,?,?)");
            stmt.setString(1, nombre);
            if (Double.isNaN(masa)) {
                stmt.setNull(2, java.sql.Types.DECIMAL);
            } else {
                stmt.setDouble(2, masa);
            }
            if (radio < 0) {
                stmt.setNull(3, java.sql.Types.DECIMAL);
            } else {
                stmt.setInt(3, radio);
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
            }
        }finally {
            if(stmt != null){
                stmt.close();
            }
        }
    }

    private static void listaPasajerosCabina (String cubierta, int cabina, String lado) throws SQLException {
        // @TODO Muestra por pantalla una lista de pasajeros de una cabina
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT p.nombre, p.edad " +
                    "FROM pasajeros p " +
                    "WHERE numero_cabina = ? AND lado_cabina = ? AND cubierta = ?");
            stmt.setInt(1, cabina);
            stmt.setString(2, lado);
            stmt.setString(3, cubierta);
            rs  = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("nombre");
                int age = rs.getInt("edad");
                System.out.println("Nombre = " + name + "; Edad = " + age);
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtener la lista de pasajeros: " + ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private static void listaOrigenes() throws SQLException {
        // @TODO Muestra por pantalla una lista de planetas, sistemas y número de pasajeros provinientes de ellos
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(
                    "SELECT " +
                            "    pl.sistema, " +
                            "    pl.nombre AS planeta, " +
                            "    COUNT(p.id) AS pasajeros " +
                            "FROM " +
                            "    planetas pl " +
                            "LEFT JOIN " +
                            "    pasajeros p ON p.planeta_natal = pl.nombre AND p.sistema_natal = pl.sistema " +
                            "GROUP BY " +
                            "    pl.sistema, " +
                            "    pl.nombre;");
            rs = stmt.executeQuery();
            while (rs.next()) {
                String sistema = rs.getString("sistema");
                String planeta = rs.getString("planeta");
                int pasajeros = rs.getInt("pasajeros");
                System.out.println("Sistema = " + sistema + "; planeta = " + planeta + "; Num-Pasajeros = " + pasajeros );
            }
        }catch (SQLException ex) {
            System.err.println("Error al obtener la lista de origenes: " + ex.getMessage());
        }finally {
            if(stmt != null){
                stmt.close();
            }
            if(rs != null){
                rs.close();
            }
        }
    }
}