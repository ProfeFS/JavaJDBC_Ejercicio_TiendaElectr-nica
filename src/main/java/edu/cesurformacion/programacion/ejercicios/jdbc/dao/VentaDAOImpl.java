package edu.cesurformacion.programacion.ejercicios.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.cesurformacion.programacion.ejercicios.jdbc.database.DatabaseConnection;

public class VentaDAOImpl implements VentaDAO {

	Connection conn = DatabaseConnection.getConnection();

	@Override
	public void realizarVenta(int productoId, int cantidadVendida) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DatabaseConnection.closeConnection();

		try {
			// Desactivar auto-commit para manejar la transacción manualmente
			conn.setAutoCommit(false);

			// 1. Verificar el stock del producto
			pstmt = conn.prepareStatement("SELECT cantidad FROM Productos WHERE id = ?");
			pstmt.setInt(1, productoId);
			rs = pstmt.executeQuery();
			int stockActual = 0;
			if (rs.next()) {
				stockActual = rs.getInt("cantidad");
			}

			if (cantidadVendida > stockActual) {
				throw new Exception("No hay suficiente stock para realizar la venta");
			}

			// 2. Reducir el stock del producto
			pstmt = conn.prepareStatement("UPDATE Productos SET cantidad = cantidad - ? WHERE id = ?");
			pstmt.setInt(1, cantidadVendida);
			pstmt.setInt(2, productoId);
			pstmt.executeUpdate();

			// 3. Registrar la venta
			pstmt = conn.prepareStatement("INSERT INTO Ventas (producto_id, cantidad_vendida) VALUES (?, ?)");
			pstmt.setInt(1, productoId);
			pstmt.setInt(2, cantidadVendida);
			pstmt.executeUpdate();

			// Comprometer la transacción si todo es correcto
			conn.commit();
		} catch (Exception e) {
			try {
				// Revertir la transacción si algo sale mal
				conn.rollback();
			} catch (SQLException ex) {
				System.out.println("Error al revertir la transacción: " + ex.getMessage());
			}
			System.out.println("No se pudo realizar la venta: " + e.getMessage());
		} finally {
			// Restablecer modo de auto-commit y cerrar recursos
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				conn.setAutoCommit(true);
				conn.close();
			} catch (SQLException ex) {
				System.out.println("Error al cerrar conexiones: " + ex.getMessage());
			}
		}

	}

	 public void realizarVenta1(int productoId, int cantidadVendida) {
	        // Obtener la conexión y desactivar auto-commit
	        try (Connection conn = DatabaseConnection.getConnection()) {
	            conn.setAutoCommit(false);

	            // Verificar el stock del producto
	            int stockActual = verificarStock(conn, productoId, cantidadVendida);
	            if (cantidadVendida > stockActual) {
	                throw new Exception("No hay suficiente stock para realizar la venta");
	            }

	            // Reducir el stock del producto
	            actualizarStock(conn, productoId, cantidadVendida);

	            // Registrar la venta
	            registrarVenta(conn, productoId, cantidadVendida);

	            // Comprometer la transacción si todo es correcto
	            conn.commit();
	        } catch (Exception e) {
	            // Imprimir y manejar adecuadamente la excepción
	            System.out.println("Error en la transacción: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }

	    private int verificarStock(Connection conn, int productoId, int cantidadVendida) throws SQLException {
	        String sql = "SELECT cantidad FROM Productos WHERE id = ?";
	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, productoId);
	            try (ResultSet rs = pstmt.executeQuery()) {
	                if (rs.next()) {
	                    return rs.getInt("cantidad");
	                }
	            }
	        }
	        return 0;
	    }

	    private void actualizarStock(Connection conn, int productoId, int cantidadVendida) throws SQLException {
	        String sql = "UPDATE Productos SET cantidad = cantidad - ? WHERE id = ?";
	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, cantidadVendida);
	            pstmt.setInt(2, productoId);
	            pstmt.executeUpdate();
	        }
	    }

	    private void registrarVenta(Connection conn, int productoId, int cantidadVendida) throws SQLException {
	        String sql = "INSERT INTO Ventas (producto_id, cantidad_vendida) VALUES (?, ?)";
	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, productoId);
	            pstmt.setInt(2, cantidadVendida);
	            pstmt.executeUpdate();
	        }
	    }
	
}
