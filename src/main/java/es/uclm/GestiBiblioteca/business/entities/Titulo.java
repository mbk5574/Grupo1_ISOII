package es.uclm.GestiBiblioteca.business.entities;

import java.util.*;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_titulo")
public class Titulo {
	
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private Collection<Autor> autores;

    @OneToMany
    private Collection<Ejemplar> ejemplares;

    @OneToMany
    private Collection<Prestamo> prestamos;

    @OneToMany
    private Collection<Reserva> reservas;

    @Column
    private String titulo_obra;


	@Column
    private String isbn;

    @Column
    private String numReserva;
	
	public Titulo() {
		
	}
	public Titulo(Collection<Autor> autores,Collection<Ejemplar> ejemplares,Collection<Prestamo> prestamos,Collection<Reserva> reservas,String titulo_obra,String numReserva,String isbn) {
		this.autores=autores;
		this.ejemplares=ejemplares;
		this.prestamos=prestamos;
		this.reservas=reservas;
		this.titulo_obra=titulo_obra;
		this.numReserva=numReserva;
		this.isbn=isbn;
	}
	
	
	


    
    @Override
	public String toString() {
		return "Titulo [id=" + id + ", autores=" + autores + ", ejemplares=" + ejemplares + ", prestamos=" + prestamos
				+ ", reservas=" + reservas + ", titulo=" + titulo_obra + ", isbn=" + isbn + ", numReserva=" + numReserva
				+ "]";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Collection<Autor> getAutores() {
		return autores;
	}
	public void setAutores(Collection<Autor> autores) {
		this.autores = autores;
	}
	public Collection<Ejemplar> getEjemplares() {
		return ejemplares;
	}
	public void setEjemplares(Collection<Ejemplar> ejemplares) {
		this.ejemplares = ejemplares;
	}
	public Collection<Prestamo> getPrestamos() {
		return prestamos;
	}
	public void setPrestamos(Collection<Prestamo> prestamos) {
		this.prestamos = prestamos;
	}
	public Collection<Reserva> getReservas() {
		return reservas;
	}
	public void setReservas(Collection<Reserva> reservas) {
		this.reservas = reservas;
	}
	public String getTitulo_obra() {
		return titulo_obra;
	}
	public void setTitulo_obra(String titulo_obra) {
		this.titulo_obra = titulo_obra;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getNumReserva() {
		return numReserva;
	}
	public void setNumReserva(String numReserva) {
		this.numReserva = numReserva;
	}





}