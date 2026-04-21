// Importes necesarios para el programa
import java.sql.*;

// Clase Main para la ejecucion del programa
public class Main {
    public static void main(String[] args) {
        // Datos para conectarse a la base de datos
        String url = "jdbc:oracle:thin:@//localhost:1521/xe"; // Cambia según tu BD
        String usuario = "JAVA";
        String contrasenia = "12345";
        
        // Datos de prueba para departamentos (formato: id,nombre)
        String[] datosDepartamentos ={
                "10,Ventas",
                "20,Marketing",
                "30,Recursos Humanos"
        };
        
        // Datos de prueba para empleados (formato: id,nombre,salario,departamento_id)
        String[] datosEmpleados = {
                "1,Unai,1500,10",
                "2,Maria,3000,10",
                "3,Rebeca,2000,20",
                "4,Jacinto,1550,20",
                "5,Pedro,3000,20",
                "6,Ana,3500,30",
                "7,Jose,2500,30",
                "8,Pepe,2800,30",
                "9,Carlos,3000,10",
                "10,Laura,2700,20"
        };
        
        // Declarar la conexión fuera del try para poder usarla en finally
        Connection conn = null;
        
        // Try-catch para gestionar la conexión y las operaciones
        try {
            // Establecer la conexión con la base de datos
            conn = DriverManager.getConnection(url, usuario, contrasenia);
            
            // Desactivar el autocommit para manejar la transacción manualmente
            conn.setAutoCommit(false);
            
            // Operación 1: insertar departamentos
            String sql = "INSERT INTO departamento (id, nombre) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                // Recorrer todos los departamentos e insertarlos
                for (String departamento : datosDepartamentos) {
                    String[] campos = departamento.split(",");
                    ps.setInt(1, Integer.parseInt(campos[0]));  // ID del departamento
                    ps.setString(2, campos[1]);                 // Nombre del departamento
                    ps.executeUpdate();
                }
                
                // Operación 2: insertar empleados
                String sql2 = "INSERT INTO empleado (id, nombre, salario, departamento_id) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                    // Recorrer todos los empleados e insertarlos
                    for (String empleado : datosEmpleados) {
                        String[] campos = empleado.split(",");
                        ps2.setInt(1, Integer.parseInt(campos[0]));     // ID del empleado
                        ps2.setString(2, campos[1]);                    // Nombre del empleado
                        ps2.setDouble(3, Double.parseDouble(campos[2])); // Salario del empleado
                        ps2.setInt(4, Integer.parseInt(campos[3]));      // ID del departamento (FK)
                        ps2.executeUpdate();
                    }
                }
            }
            
            // Confirmar todos los cambios en la base de datos
            conn.commit();
            System.out.println("Todo bien insertado");
            
        } catch (SQLException e) {
            // Capturar cualquier error durante las operaciones
            System.out.println("Error al insertar en las tablas: " + e.getMessage());
            
            // Rollback implícito en caso de error para deshacer cambios
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Se realizó rollback de la transacción");
                } catch (SQLException rollbackEx) {
                    System.out.println("Error al hacer rollback: " + rollbackEx.getMessage());
                }
            }
            
        } finally {
            // Cerrar la conexión manualmente (no hay try-with-resources a este nivel)
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }
}
