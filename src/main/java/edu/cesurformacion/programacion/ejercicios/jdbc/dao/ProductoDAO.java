package edu.cesurformacion.programacion.ejercicios.jdbc.dao;

import java.util.List;

import edu.cesurformacion.programacion.ejercicios.jdbc.model.Producto;

public interface ProductoDAO {
	void addProducto(Producto producto);

	List<Producto> getAllProductos();

	Producto getProductoById(int id);

	void updateProducto(Producto producto);

	void deleteProducto(int id);
}
