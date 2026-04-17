import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@//localhost:1521/xe"; // Cambia según tu BD
        String usuario = "JAVA";
        String contrasenia = "12345";

        String[] datosDepartamentos ={
                "10,Ventas",
                "20,Marketing",
                "30,Recursos Humanos"
        };
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

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, usuario, contrasenia);
            conn.setAutoCommit(false);

            // Operación 1: insertar departamentos
            String sql =  "INSERT INTO departamento (id, nombre) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (String departamento : datosDepartamentos) {
                    String[] campos = departamento.split(",");
                    ps.setInt(1, Integer.parseInt(campos[0]));
                    ps.setString(2, campos[1]);
                    ps.executeUpdate();
                }

                // Operación 2: insertar empleados
                String sql2 =  "INSERT INTO empleado (id, nombre, salario, departamento_id) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                    for (String empleado : datosEmpleados) {
                        String[] campos = empleado.split(",");
                        ps2.setInt(1, Integer.parseInt(campos[0]));
                        ps2.setString(2, campos[1]);
                        ps2.setDouble(3, Double.parseDouble(campos[2]));
                        ps2.setInt(4, Integer.parseInt(campos[3]));
                        ps2.executeUpdate();
                    }
                }
            }

            conn.commit();
            System.out.println("Todo bien insertado");

        } catch (SQLException e) {
            System.out.println("Error al insertar en las tablas: " + e.getMessage());

            // Rollback implícito en caso de error
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Se realizó rollback de la transacción");
                } catch (SQLException rollbackEx) {
                    System.out.println("Error al hacer rollback: " + rollbackEx.getMessage());
                }
            }

        } finally {
            // Cerrar la conexión manualmente
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