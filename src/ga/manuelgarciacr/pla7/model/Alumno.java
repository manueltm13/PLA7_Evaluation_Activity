package ga.manuelgarciacr.pla7.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "alumnos")
public class Alumno {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idalumno;
	private String nombre;
	private String email;
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
		CascadeType.DETACH, CascadeType.REFRESH })
	@JoinTable(name = "mod_alu",
		joinColumns = @JoinColumn(name="idalumno"),
		inverseJoinColumns = @JoinColumn(name="idmodulo"))
	private List<Modulo> modulos;	
	
	public Alumno() {super();}
	
	public Alumno(String nombre, String email) {
		super();
		setNombre(nombre);
		setEmail(email);
	}
	
	public int getIdalumno() {
		return idalumno;
	}
	public void setIdalumno(int idalumno) {
		this.idalumno = idalumno;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}	
	public List<Modulo> getModulos() {
		return modulos;
	}
	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}
	public Alumno addModulo(Modulo modulo) {
		if (modulos == null)
			modulos = new ArrayList<Modulo>();
		modulos.add(modulo);
		return this;
	}
	public String getIdModulos() {
		String output = "";

		if(modulos == null || modulos.size() == 0)
			return "[SIN MÓDULOS]";
		
		for(Modulo m: modulos)
			if(output.equals(""))
				output += "<" + m.getIdmodulo() + ", " + m.getNombre() + ">";
			else
				output += ", <" + m.getIdmodulo() + ", " + m.getNombre() + ">";
		if(modulos.size() == 1)
			output = "[MÓDULO: " + output + "]";
		else
			output = "[MÓDULOS: " + output + "]";
		return output;
	}

	@Override
	public String toString() {
		return "[ALUMNO: " + getIdalumno() + ", " + getNombre() + ", " + getEmail() + ", " + getIdModulos() + "]";
	}		
}
