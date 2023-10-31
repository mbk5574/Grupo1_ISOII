package es.uclm.GestiBiblioteca.business.controller;

import es.uclm.GestiBiblioteca.persistence.PrestamoDAO;
import es.uclm.GestiBiblioteca.persistence.ReservaDAO;

public class GestorPrestamos {

	PrestamoDAO prestamoDAO;
	ReservaDAO reservaDAO;

	/**
	 * 
	 * @param isbn
	 * @param idEjemplar
	 * @param idUsuario
	 */
	public void realizarPrestamo(String isbn, String idEjemplar, String idUsuario) {
		// TODO - implement GestorPrestamos.realizarPrestamo
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param isbn
	 * @param idEjemplar
	 * @param idUsuario
	 */
	public void realizarDevolucion(String isbn, String idEjemplar, String idUsuario) {
		// TODO - implement GestorPrestamos.realizarDevolucion
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param idUsuario
	 * @param isbn
	 */
	public void realizarReserva(String idUsuario, String isbn) {
		// TODO - implement GestorPrestamos.realizarReserva
		throw new UnsupportedOperationException();
	}

}