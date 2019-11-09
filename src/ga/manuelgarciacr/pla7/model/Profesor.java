package ga.manuelgarciacr.pla7.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "profesores")
public class Profesor {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idprofesor;
	private String dni;
	private String nombre;
	private String email;
	@OneToMany(mappedBy = "profesor", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})  
	private List<Modulo> modulos;

	public Profesor() {super();};
	
	public Profesor(String nombre, String dni, String email) {
		super();
		setNombre(nombre);
		setDni(dni);
		setEmail(email);
	}
	
	public int getIdprofesor() {
		return idprofesor;
	}
	public void setIdprofesor(int idprofesor) {
		this.idprofesor = idprofesor;
	}
	public String getDni() {
		return dni;
	}
	public Profesor setDni(String dni) {
		this.dni = dni;
		return this;
	}
	public String getNombre() {
		return nombre;
	}
	public Profesor setNombre(String nombre) {
		this.nombre = nombre;
		return this;
	}
	public String getEmail() {
		return email;
	}
	public Profesor setEmail(String email) {
		this.email = email;
		return this;
	}
	public List<Modulo> getModulos() {
		return modulos;
	}
	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}		
	public Profesor addModulo(Modulo modulo) {
		if (modulos == null)
			modulos=new ArrayList<Modulo>();
		modulo.setProfesor(this);
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
		return "[PROFESOR: " + getIdprofesor() + ", " + getNombre() + ", " + getDni() + ", " + getEmail() + ", " + getIdModulos() + "]";
	}
}
