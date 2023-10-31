package es.uclm.GestiBiblioteca.business.entities;

import java.util.*;

public class Titulo {

	Collection<Autor> autores;
	Collection<Ejemplar> ejemplares;
	Collection<Prestamo> prestamos;
	Collection<Reserva> reservas;
	private String titulo;
	private String isbn;
	private String numReserva;

}