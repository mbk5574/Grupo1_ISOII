package es.uclm.GestiBiblioteca.business.entities;

import java.util.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
@Entity
public class Reserva {
	
	public Reserva() {
		
	}public Reserva(Usuario usuario,Titulo titulo,Date fecha) {
		super();
		this.fecha=fecha;
		this.usuario=usuario;
		this.titulo=titulo;
		
	}
	
    @ManyToOne
	Usuario usuario;
    @ManyToOne
	Titulo titulo;
    @Column
	private Date fecha;
    @Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private int id_reserva;

}