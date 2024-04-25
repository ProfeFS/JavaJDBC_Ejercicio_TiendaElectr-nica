package edu.cesurformacion.programacion.ejercicios.jdbc.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.cesurformacion.programacion.ejercicios.jdbc.database.DatabaseConnection;
import edu.cesurformacion.programacion.ejercicios.jdbc.model.Producto;

public class ProductoDAOImpl implements ProductoDAO {

	private Connection conn;
	private String query;

	public ProductoDAOImpl() {
		conn = DatabaseConnection.getConnection();
	}

	@Override
	public int addProducto(Producto producto) {
		int generatedkey = 0;
		String query = "INSERT INTO productos (nombre, categoria, precio, cantidad) VALUES (?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, producto.getNombreProducto());
			stmt.setString(2, producto.getCategoria());
			stmt.setBigDecimal(3, producto.getPrecio());
			stmt.setInt(4, producto.getCantidad());
			stmt.executeUpdate();

			ResultSet rsIds = stmt.getGeneratedKeys();
			if (rsIds.next()) {
				generatedkey = rsIds.getInt("id");
				System.out.println("Se ha creado el registro con id: " + generatedkey);
			}

		} catch (SQLException e) {
			System.out.println("Error adding product" +  e.getMessage());
		}
		return generatedkey;
	}

	@Override
	public List<Producto> getAllProductos() {
		List<Producto> productos = new ArrayList<>();
		String query = "SELECT id, nombre, categoria, precio, cantidad FROM Productos";
		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				Producto producto = new Producto(rs.getInt("id"), rs.getString("nombre"), rs.getString("categoria"),
						rs.getBigDecimal("precio"), rs.getInt("cantidad"));
				productos.add(producto);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error retrieving products", e);
		}
		return productos;
	}

	@Override
	public Producto getProductoById(int id) {
		String query = "SELECT id, nombre, categoria, precio, cantidad FROM Productos WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return new Producto(rs.getInt("id"), rs.getString("nombre"), rs.getString("categoria"),
							rs.getBigDecimal("precio"), rs.getInt("cantidad"));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error retrieving product by id", e);
		}
		return null;
	}

	@Override
	public void updateProducto(Producto producto) {
		String query = "UPDATE Productos SET nombre = ?, categoria = ?, precio = ?, cantidad = ? WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, producto.getNombreProducto());
			pstmt.setString(2, producto.getCategoria());
			pstmt.setBigDecimal(3, producto.getPrecio());
			pstmt.setInt(4, producto.getCantidad());
			pstmt.setInt(5, producto.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Error updating product", e);
		}
	}

	@Override
	public void deleteProducto(int id) {
		String query = "DELETE FROM Productos WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Error deleting product", e);
		}
	}
}