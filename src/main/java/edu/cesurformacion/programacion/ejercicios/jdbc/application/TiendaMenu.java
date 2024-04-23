package edu.cesurformacion.programacion.ejercicios.jdbc.application;

import java.util.Scanner;

import edu.cesurformacion.programacion.ejercicios.jdbc.service.ProductoService;
import edu.cesurformacion.programacion.ejercicios.jdbc.service.VentaService;

public class TiendaMenu {
	private ProductoService productoService;
	private VentaService ventaService;
	private Scanner sc;


	public TiendaMenu() {
		productoService = new ProductoService();
		ventaService = new VentaService();
		sc = new Scanner(System.in);
		mostrarMenu();
	}

	private void mostrarMenu() {

		String opcion = "";
		while (!opcion.equals("6")) {
			System.out.println("\nGestión de Tienda Electrónica - Menú Principal");
			System.out.println("1. Agregar Producto");
			System.out.println("2. Listar Productos");
			System.out.println("3. Actualizar Producto");
			System.out.println("4. Eliminar Producto");
			System.out.println("5. Realizar Venta");
			System.out.println("6. Salir");
			System.out.print("Seleccione una opción: ");
			opcion = sc.nextLine();

			switch (opcion) {
			case "1":
				//agregarProducto();
				break;
			case "2":
				//listarProductos();
				break;
			case "3":
				//actualizarProducto();
				break;
			case "4":
			//	eliminarProducto();
				break;
			case "5":
				//realizarVenta();
				break;
			case "6":
				System.out.println("Saliendo...");
				break;
			default:
				System.out.println("Opción no válida. Por favor, intente de nuevo.");
			}
		}
	}
	
	

}
