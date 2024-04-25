package edu.cesurformacion.programacion.ejercicios.jdbc.service;

import edu.cesurformacion.programacion.ejercicios.jdbc.dao.ProductoDAO;
import edu.cesurformacion.programacion.ejercicios.jdbc.dao.ProductoDAOImpl;
import edu.cesurformacion.programacion.ejercicios.jdbc.model.Producto;

public class ProductoService {

	ProductoDAO productoDAO;

	public ProductoService() {
		productoDAO = new ProductoDAOImpl();
	}

	public int agregarProducto(Producto p) {

		return productoDAO.addProducto(p);
	}

}
