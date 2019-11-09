package ga.manuelgarciacr.pla7.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "modulos")
public class Modulo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idmodulo;
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "idprofesor")
	private Profesor profesor;
	private String nombre;
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH })
	@JoinTable(name = "mod_alu",
		joinColumns = @JoinColumn(name = "idmodulo"),
		inverseJoinColumns = @JoinColumn(name = "idalumno"))
	private List<Alumno> alumnos;	
	
	public Modulo() {super();}
	
	public Modulo(String nombre) {
		super();
		setNombre(nombre);
	}
	
	public int getIdmodulo() {
		return idmodulo;
	}
	public void setIdmodulo(int idmodulo) {
		this.idmodulo = idmodulo;
	}
	public Profesor getProfesor() {
		return profesor;
	}
	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<Alumno> getAlumnos() {
		return alumnos;
	}
	public void setAlumnos(List<Alumno> alumnos) {
		this.alumnos = alumnos;
	}
	public Modulo addAlumno(Alumno alumno) {
		if (alumnos == null)
			alumnos = new ArrayList<Alumno>();
		alumnos.add(alumno);
		return this;
	}
	public String getIdAlumnos() {
		String output = "";

		if(alumnos == null || alumnos.size() == 0)
			return "[SIN ALUMNOS]";
		
		for(Alumno a: alumnos)
			if(output.equals(""))
				output += "<" + a.getIdalumno() + ", " + a.getNombre() + ">";
			else
				output += ", <" + a.getIdalumno() + ", " + a.getNombre() + ">";
		if(alumnos.size() == 1)
			output = "[ALUMNO: " + output + "]";
		else
			output = "[ALUMNOS: " + output + "]";
		return output;
	}

	@Override
	public String toString() {
		return "[MÓDULO: " + getIdmodulo() + ", " + getNombre() + ", " + (getProfesor() == null ? "[SIN PROFESOR]" : getProfesor().toString()) + getIdAlumnos() + "]";
	}	
	
}
