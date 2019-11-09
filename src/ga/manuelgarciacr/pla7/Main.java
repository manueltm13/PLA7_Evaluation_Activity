package ga.manuelgarciacr.pla7;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import ga.manuelgarciacr.pla7.model.*;

public class Main {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// Creo la factoría de sesiones a través de la configuración que cojo del xml 
		// y añadiendo las clases del modelo.
		// Creo también la sesión
		// Creo ambas en un try-with-resources
		SessionFactory factory = null;
		Session session = null;
		Transaction trn = null;
		try{
			factory = new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(Alumno.class)
				.addAnnotatedClass(Modulo.class)
				.addAnnotatedClass(Profesor.class)
				.buildSessionFactory();
			session = factory.openSession();
			trn = session.beginTransaction();

			// Consulta
			List<String> lstOutput;
			lstOutput = showRecords(session, null, "from Profesor");
			printList(lstOutput, "PROFESORES");
			
			// Modificar dos profesores
			Profesor profesor = session.get(Profesor.class, 4);
			profesor.setNombre("Nombre Profesor Cuatro").setDni("dnicuatro").setEmail("cuatro@profe.com");
			session.update(profesor);
			profesor = session.get(Profesor.class, 5);
			profesor.setNombre("Nombre Profesor Cinco").setDni("dnicinco").setEmail("cinco@profe.com");
			session.update(profesor);
			
			String query = "from Profesor p where p.idprofesor = 4 or p.idprofesor = 5";
			lstOutput = showRecords(session, null, query);
			printList(lstOutput, "MODIFICO PROFESORES 4 Y 5");
			
			// Insertar dos profesores
			session.persist(new Profesor("Profe2 García", "dnidos", "dos@profe.com"));
			session.persist(new Profesor("Profe3 Pérez", "dnitres", "tres@profe.com"));
			
			query = "from Profesor p where p.nombre like '%Profe2%' or p.nombre like '%Profe3%' ";
			lstOutput = showRecords(session, null, query);
			printList(lstOutput, "INSERTO PROFESORES 'Profe2' Y 'Profe3'");
			
			// Insertar dos módulos y asignar a cada uno un profesor distinto
			Modulo m1, m2;
			session.persist(m1 = new Modulo("Módulo 1"));
			session.persist(m2 = new Modulo("Módulo 2"));
			List<Profesor> lstProfesores = session.createQuery(query).getResultList(); // Utilizo la query del punto anterior
			if(lstProfesores.size() > 1) {
				m1.setProfesor(lstProfesores.get(0));
				lstProfesores.get(1).addModulo(m2); // Otra forma de hacerlo
			}

			trn.commit();
			// El primer método: "m1.setProfesor(lstProfesores.get(0));" pese a que actualiza bién la base de datos no 
			// añade el módulo a la lista de módulos del objeto profesor correspondiente. Por ello hago commit y vuelvo
			// a cargar los objetos desde la base de datos. He de limpiar la sesión porque hace de caché y los objetos
			// no se actualizan en la consulta de la BDD.
			session.clear();
			trn.begin();
			
			query = "from Modulo m where m.nombre = 'Módulo 1' or m.nombre = 'Módulo 2'";
			lstOutput = showRecords(session, null, query);
			query = "from Profesor p where p.nombre like '%Profe2%' or p.nombre like '%Profe3%' ";
			lstOutput = showRecords(session, lstOutput, query);
			printList(lstOutput, "INSERTO MÓDULOS 'Módulo 1' Y 'Módulo 2'. LES ASIGNO LOS PROFESORES 'Profe2' Y 'Profe3'.");

			// Insertar tres alumnos, dos para el primer módulo y uno para el segundo.
			List<Modulo> lstModulos = session.createQuery("from Modulo m order by 1").setMaxResults(2).getResultList();
			Alumno a1 = null, a2 = null, a3 = null;
			if(lstModulos.size() > 1) {
				session.persist(a1 = new Alumno("Alumno1 López", "dnialumno1"));
				session.persist(a2 = new Alumno("Alumno2 González", "dnialumno2"));
				session.persist(a3 = new Alumno("Alumno3 Fernández", "dnialumno3"));
				lstModulos.get(0).addAlumno(a1).addAlumno(a2);
				a3.addModulo(lstModulos.get(1)); // Otra forma de hacerlo
			}

			trn.commit();
			// Pese a que ambos métodos utilizados para esta última actualización dejan bién la base de datos, solo
			// el objeto sobre el que se ejecuta el metodo add ve actualizada su lista interna de propiedades. Por ello 
			// hago commit y vuelvo a cargar los objetos desde la base de datos. He de limpiar la sesión porque hace 
			// de caché y los objetos no se actualizan en la consulta de la BDD.
			session.clear();
			trn.begin();
			
			query = "from Alumno a where a.nombre like '%Alumno%'";
			lstOutput = showRecords(session, null, query);
			query = "from Modulo m order by 1";
			lstOutput = showRecords(session, lstOutput, query, 2);
			printList(lstOutput, "INSERTO ALUMNOS 'Alumno1', 'Alumno2' Y 'Alumno3'. LES ASIGNO LOS MÓDULOS '" + 
					lstModulos.get(0).getNombre() + "' Y '" + lstModulos.get(1).getNombre() + "'.");
			
			// Elimino los alumnos Alumno1 y Alumno3
			query = "from Alumno a where a.nombre like '%Alumno1%' or a.nombre like '%Alumno3%' ";
			List<Alumno> lstAlumnos = session.createQuery(query).getResultList();
			if(lstAlumnos.size() > 1) {
				session.remove(lstAlumnos.get(0));
				session.remove(lstAlumnos.get(1));
			}
			
			lstOutput = showRecords(session, null, "from Alumno");
			printList(lstOutput, "ELIMINO LOS ALUMNOS 'Alumno1' Y 'Alumno3'.");

			trn.commit();
		}catch(Exception ex) {
			if(trn != null)
				trn.rollback();
			ex.printStackTrace();
		}finally {
			if(session != null)
				session.close();
			if(factory != null)
				factory.close();
		}
	}
	
	private static <T> List<String> showRecords (Session session, List<String> lstOutput, String query) {
		return showRecords(session, lstOutput, query, -1);
	}
	
	@SuppressWarnings("unchecked")
	private static <T> List<String> showRecords (Session session, List<String> lstOutput, String query, int maxResults) {
		List<T> lst;
		if(lstOutput == null)
			lstOutput = new ArrayList<>();
		if(maxResults < 0)
			lst = session.createQuery(query).getResultList();
		else
			lst = session.createQuery(query).setMaxResults(maxResults).getResultList();
		for (T o : lst)
			lstOutput.add(o.toString());
		return lstOutput;
	}
	
	public static void printList(List<String> lst, String header) {
		cls(header);
		for (String str : lst) {
			System.out.println(str);
		}
		pause();
	}

	private static void cls(String msg){
        for(int i = 0; i < 50; i++) System.out.println();
        System.out.println(msg + "\n");
    }
    
    private static void pause(){
        @SuppressWarnings("resource")
		Scanner scnKeyboard = new Scanner(System.in);

        System.out.println("\nPulse Enter para continuar...");
        scnKeyboard.nextLine();
    }
    
}
