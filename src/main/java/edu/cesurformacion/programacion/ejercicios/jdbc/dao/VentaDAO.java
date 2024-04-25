package edu.cesurformacion.programacion.ejercicios.jdbc.dao;

import edu.cesurformacion.programacion.ejercicios.jdbc.model.Venta;

public interface VentaDAO {
	void realizarVenta1(int productoId, int cantidadVendida);
	void realizarVenta(int productoId, int cantidadVendida);
	int addSale(Venta venta);

}
