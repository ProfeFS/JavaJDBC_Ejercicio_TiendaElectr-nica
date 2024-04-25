package edu.cesurformacion.programacion.ejercicios.jdbc.application;

import java.math.BigDecimal;
import java.util.Scanner;

import edu.cesurformacion.programacion.ejercicios.jdbc.dao.ProductoDAO;
import edu.cesurformacion.programacion.ejercicios.jdbc.dao.ProductoDAOImpl;
import edu.cesurformacion.programacion.ejercicios.jdbc.model.Producto;
import edu.cesurformacion.programacion.ejercicios.jdbc.service.VentaService;

public class Main {
	public static void main(String[] args) {
		ProductoDAO dao = new ProductoDAOImpl();
		Scanner scanner = new Scanner(System.in);
		VentaService ventaService = new VentaService();

		// Implementar lógica para interactuar con el usuario y realizar operaciones
		// CRUD
		// Por ejemplo, agregar un producto:
		System.out.println("Agregar nuevo producto:");
		System.out.print("Nombre: ");
		String nombre = scanner.nextLine();
		System.out.print("Categoría: ");
		String categoria = scanner.nextLine();
		System.out.print("Precio: ");
		BigDecimal precio = scanner.nextBigDecimal();
		System.out.print("Cantidad: ");
		int cantidad = scanner.nextInt();

		Producto producto = new Producto(0, nombre, categoria, precio, cantidad);
		dao.addProducto(producto);

		// Mostrar productos
		System.out.println("Lista de productos:");
		dao.getAllProductos().forEach(p -> System.out.println(p.getNombreProducto() + ", " + p.getCategoria()));

		scanner.close();
	}
}
