package edu.cesurformacion.programacion.ejercicios.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.cesurformacion.programacion.ejercicios.jdbc.database.DatabaseConnection;
import edu.cesurformacion.programacion.ejercicios.jdbc.model.Venta;

public class VentaDAOImpl implements VentaDAO {

	public void realizarVenta(int productoId, int cantidadVendida) {
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

	@Override
	public void realizarVenta1(int productoId, int cantidadVendida) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = DatabaseConnection.getConnection();

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

	@Override
	public int addSale(Venta venta) throws RuntimeException {

		// transacciones
		// Obtenemos la conn y seteamos autocimit a false

		// 1.- Disminuir el stock en la tabla productos.
		// 2.- Insertar registro con datoos de la venta en tabla venta

		// comiteamos
		// cerramos la conn si es necesario.

		int generatedKey=0;
		String sqlUpdateP = "UPDATE Productos SET cantidad = cantidad - ? WHERE id = ?";
		String sqlInsertVenta = "INSERT INTO ventas (producto_id, cantidad_vendida, fecha_venta) VALUES(?, ?, ?)";

		Connection conn = DatabaseConnection.getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdateP);
				PreparedStatement pstmtV = conn.prepareStatement(sqlInsertVenta, Statement.RETURN_GENERATED_KEYS)) {

			conn.setAutoCommit(false);
			pstmt.setInt(1, venta.getCantidad());
			pstmt.setInt(2, venta.getProductId());
			int rowAfeccted = pstmt.executeUpdate();

			if (rowAfeccted != 0) {
				System.out.println("Se ha actualizado el stock del producto con id: " + venta.getProductId());
			}

			pstmtV.setInt(1, venta.getProductId());
			pstmtV.setInt(2, venta.getCantidad());
			pstmtV.setDate(3, java.sql.Date.valueOf(venta.getFecha()));
			rowAfeccted = pstmtV.executeUpdate();

			try (ResultSet rs = pstmtV.getGeneratedKeys()) {
				while (rs.next()) {					
					return generatedKey = rs.getInt("id");
				}

			} catch (Exception e) {
				throw new RuntimeException("Error: No se pudo obtener la clave generada para la venta.");
			}
			conn.commit();

		} catch (SQLException e) {
			System.out.println("Error al realizar una venta, ejecutando roolback" + e.getMessage());
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		
		
		return generatedKey;

	}

	public void addSaleMejorado(Venta venta) {
		String updateProductoSQL = "UPDATE Productos SET cantidad = cantidad - ? WHERE id = ?";
		String insertVentaSQL = "INSERT INTO ventas (producto_id, cantidad_vendida, fecha_venta) VALUES(?, ?, ?)";

		Connection conn = null; // Definir conn fuera de try para acceder en catch
		try {
			conn = DatabaseConnection.getConnection();
			conn.setAutoCommit(false);

			try (PreparedStatement pstmtProducto = conn.prepareStatement(updateProductoSQL)) {
				pstmtProducto.setInt(1, venta.getCantidad());
				pstmtProducto.setInt(2, venta.getProductId());
				int rowAffected = pstmtProducto.executeUpdate();
				if (rowAffected == 0) {
					throw new SQLException("Error: No se pudo actualizar el stock del producto.");
				}
			}

			try (PreparedStatement pstmtVenta = conn.prepareStatement(insertVentaSQL,
					Statement.RETURN_GENERATED_KEYS)) {
				pstmtVenta.setInt(1, venta.getProductId());
				pstmtVenta.setInt(2, venta.getCantidad());
				pstmtVenta.setDate(3, java.sql.Date.valueOf(venta.getFecha()));
				int rowAffected = pstmtVenta.executeUpdate();
				if (rowAffected == 0) {
					throw new SQLException("Error: No se pudo registrar la venta.");
				}

				try (ResultSet rs = pstmtVenta.getGeneratedKeys()) {
					if (rs.next()) {
						venta.setId(rs.getInt(1)); // Asumiendo que el id está siendo generado automáticamente.
					}
				}
			}

			conn.commit();
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
					System.out.println("Transacción revertida debido a un error.");
				} catch (SQLException ex) {
					throw new RuntimeException("Error al intentar revertir la transacción: " + ex.getMessage(), ex);
				}
			}
			throw new RuntimeException("Error durante la operación de venta: " + e.getMessage(), e);
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					System.out.println("Error al restablecer el autoCommit o cerrar la conexión: " + e.getMessage());
				}
			}
		}
	}

}
