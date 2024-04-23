package edu.cesurformacion.programacion.ejercicios.jdbc.model;

import java.math.BigDecimal;

public class Producto {
	private int id;
	private String nombre;
	private String categoria;
	private BigDecimal precio;
	private int cantidad;

	public Producto(int id, String nombreProducto, String categoria, BigDecimal precio, int cantidad) {
		this.id = id;
		this.nombre = nombreProducto;
		this.categoria = categoria;
		this.precio = precio;
		this.cantidad = cantidad;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombreProducto() {
		return nombre;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombre = nombreProducto;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombreProducto=" + nombre + ", categoria=" + categoria + ", precio="
				+ precio + ", cantidad=" + cantidad + "]";
	}
	
	
}
