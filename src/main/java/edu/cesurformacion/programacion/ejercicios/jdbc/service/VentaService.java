package edu.cesurformacion.programacion.ejercicios.jdbc.service;

import java.time.LocalDate;

import edu.cesurformacion.programacion.ejercicios.jdbc.dao.ProductoDAO;
import edu.cesurformacion.programacion.ejercicios.jdbc.dao.ProductoDAOImpl;
import edu.cesurformacion.programacion.ejercicios.jdbc.dao.VentaDAO;
import edu.cesurformacion.programacion.ejercicios.jdbc.dao.VentaDAOImpl;
import edu.cesurformacion.programacion.ejercicios.jdbc.model.Producto;
import edu.cesurformacion.programacion.ejercicios.jdbc.model.Venta;

public class VentaService {
	ProductoDAO productoDAO;
	VentaDAO ventaDAO;

	public VentaService() {
		productoDAO = new ProductoDAOImpl();
		ventaDAO = new VentaDAOImpl();
	}

	public void processSale(int productId, int cantidad) {
		// valido las cantidades en stock del producto.
		Producto p = productoDAO.getProductoById(productId);

		if (p.getCantidad() < cantidad) {
			System.out.println("No es posible realizar la venta ya que no hay suficientes productos en stock");
			return;
		}

		int generatedKey = ventaDAO.addSale(new Venta(0, cantidad, productId, LocalDate.now()));
		if (generatedKey != 0) {
			System.out.println("Se ha regustrado una nueva venta con id: " + generatedKey);
		} else {
			System.out.println("No se ha podido registrar la venta");
		}
	}

}
