package edu.cesurformacion.programacion.ejercicios.jdbc.model;

import java.time.LocalDate;

public class Venta {

	private int id;
	private int cantidad;
	private int productId;
	private LocalDate fecha;
	
	public Venta(int id, int cantidad, int productId, LocalDate fecha) {
		super();
		this.id = id;
		this.cantidad = cantidad;
		this.productId = productId;
		this.fecha = fecha;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	@Override
	public String toString() {
		return "Venta [id=" + id + ", cantidad=" + cantidad + ", productId=" + productId + ", fecha=" + fecha + "]";
	}
	
	
	
	

}
