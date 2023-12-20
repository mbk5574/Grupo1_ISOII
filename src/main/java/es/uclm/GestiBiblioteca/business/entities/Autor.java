package es.uclm.GestiBiblioteca.business.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;


@Entity
public class Autor {
	public Autor(){
	
	}
		public Autor(String nombre,String apellido,Set<Titulo> titulos) {
			super();
			this.nombre=nombre;
			this.apellido=apellido;
			this.titulos=titulos;
			
		}
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private Collection<Titulo> titulos = new HashSet<>();

    @Column
    private String nombre;
    @Column
    private String apellido;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Collection<Titulo> getTitulos() {
		return titulos;
	}
	public void setTitulos(Collection<Titulo> titulos) {
		this.titulos = titulos;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	@Override
	public String toString() {
		return "Autor [id=" + id + ", titulos=" + titulos + ", nombre=" + nombre + ", apellido=" + apellido + "]";
	}

    
}
