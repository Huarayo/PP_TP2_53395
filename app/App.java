/******************************************************************************

Welcome to GDB Online.
  GDB online is an online compiler and debugger tool for C, C++, Python, PHP, Ruby, 
  C#, OCaml, VB, Perl, Swift, Prolog, Javascript, Pascal, COBOL, HTML, CSS, JS
  Code, Compile, Run and Debug online from anywhere in world.

*******************************************************************************/
package app;
import certificacion.Certificable;
import excepciones.CupoExcedidoException;

import excepciones.DatosInvalidosException;
import jdk.jfr.Event;
import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.Sala;

import java.io.FileNotFoundException;
import java.io.IOException;

import certificacion.Certificable;
import modelo.actividades.Actividad;
import modelo.Inscripcion;

import java.util.List;
import modelo.actividades.Taller;
import modelo.actividades.Charla;
import modelo.actividades.Curso;

import hilos.EnvioTicketsTheread;

public class App {

    private static final String RESET  = "\u001B[0m";
    private static final String CIAN   = "\u001B[36m"; // Ejercicio 1
    private static final String AMARILLO = "\u001B[33m"; // Ejercicio 2
    private static final String VERDE  = "\u001B[32m"; // Ejercicio 3
    public static void main(String[] args){
        System.out.println("========== SISTEMA DE EVENTOS UNIVERSITARIOS ==========\n");

        //no existen eventos fuera del try catch f)
        EventoUniversitario evento1 = null;
        EventoUniversitario evento2 = null;

        try {
            System.out.println(CIAN + "\n########## EJERCICIO 1: EXCEPCIONES, PAQUETES Y PERSISTENCIA ##########" + RESET);
            // a) REGISTRAR ESTUDIANTES
            System.out.println("a) REGISTRANDO ESTUDIANTES:");
            Estudiante est1 = new Estudiante("2024001", "Juan Pérez");
            Estudiante est2 = new Estudiante("2024002", "María García");
            Estudiante est3 = new Estudiante("2024003", "Carlos López");
            System.out.println(est1.getNombre());
            System.out.println(est2.getNombre());
            System.out.println(est3.getNombre());


            // b) CONSTRUIR LOS EVENTOS
            System.out.printf("\nb) CONSTRUYENDO EVENTOS:");
            evento1 = new EventoUniversitario("01", "Presentación Proyectos", 5000, false);
            evento2 = new EventoUniversitario("02", "Conferencia de IA", 500.0, true);
            System.out.println(evento1.getTitulo());
            System.out.println(evento2.getTitulo());

            // c) ASIGNAR SALAS
            System.out.println("\nc) ASIGNANDO SALAS:");
            Sala sala1 = new Sala(103, "Aula Total");
            Sala sala2 = new Sala(400, "Aula Conferencias");
            evento1.asignarSala(sala1);
            evento2.asignarSala(sala2);

            // d) CREAR ACTIVIDADES
            System.out.println("\nd) CREANDO ACTIVIDADES:");
            evento1.crearActividad("Taller", 3, "Introducción", 100,1);
            evento1.crearActividad("Taller", 4, "Conclusión", 1, 2);

            evento1.crearActividad("Curso", 5, "Curso de Java", 30, 5);

            evento2.crearActividad("Charla", 1, "Inteligencia Artificial", 200,3);
            evento2.crearActividad("Charla", 2, "Tarde de charla", 300,4);
            evento1.crearActividad("Charla", 6, "Charla de cierre", 50, 0);
            // e) INSCRIPCION ESTUDIANTES
            System.out.println("\ne) INSCRIBIENDO ESTUDIANTES:");

                Inscripcion insc1 = evento1.getActividades().get(0).inscribir(est1);
                Inscripcion insc2 = evento2.getActividades().get(1).inscribir(est2);

                Inscripcion insc3 = evento1.getActividades().get(1).inscribir(est3); // 1/1 -> exito

                evento2.getActividades().get(0).inscribir(est3);
                evento1.getActividades().get(2).inscribir(est3);

                //parte del ejercicio 4
                System.out.println(CIAN + "\n########## EJERCICIO 4: CLASES ANIDADAS E HILOS ##########" + RESET);

                insc1.confirmar();
                System.out.println("\nInscripción de " + insc1.getEstudiante().getNombre()
                        + " -> estado: " + insc1.getEstado() + " | confirmada: " + insc1.estaConfirmada());
                System.out.println("Inscripción de " + insc2.getEstudiante().getNombre()
                        + " -> estado: " + insc2.getEstado() + " | confirmada: " + insc2.estaConfirmada());

                insc3.confirmar();
                System.out.println("Inscripción de " + insc3.getEstudiante().getNombre()
                        + " -> estado: " + insc3.getEstado() + " | confirmada: " + insc3.estaConfirmada());

            // generar un ticket por cada inscripcion confirmada
                System.out.println("\nGENERANDO TICKETS:");
                for (EventoUniversitario ev: List.of(evento1, evento2)) {
                    for(Actividad act : ev.getActividades()) {
                        for(Inscripcion insc : act.getInscripciones()) {
                            if(insc.estaConfirmada()) {
                                //ticket = insc.generarTicket()
                                //ticket.getIdTicket()
                                System.out.println("Ticket generado: " + insc.generarTicket().getIdTicket());
                            }
                        }
                    }
                }

                //iniciar evento concurrente. hilo por evento
                EnvioTicketsTheread envio1 = new EnvioTicketsTheread(evento1);
                EnvioTicketsTheread envio2 = new EnvioTicketsTheread(evento2);

                envio1.start();
                envio2.start();

                //hilo principal sigue mostrando datos mientras se envían los tickets
                String hiloMain = Thread.currentThread().getName();
                //Recorrer los dos evnetos  con un solo for en vez de escribir lo mismo 2 veces
                // primera vuelta: ev = evento1
                // segunda vuelta: ev = evento2
                for(EventoUniversitario ev : List.of(evento1, evento2)) {
                    //Dos tareas  corriendo al mismo tiempo
                    //mientras el hilo envia , el main muestra el evento , las actividad y los inscriptos intercalando con sleep
                    System.out.println("[" + hiloMain + "] Evento: " + ev.getTitulo());
                    for (Actividad act: ev.getActividades()) {
                        Thread.sleep(300);
                        System.out.println("[" + hiloMain + "] Actividad: " + act.getTitulo() );
                        for (Inscripcion insc : act.getInscripciones()) {
                            System.out.println("[" + hiloMain + "] Inscripto: " +insc.getEstudiante().getNombre() );
                        }
                     }
                }
                //Espera al hilo que termine antes de seguir
                envio1.join();
                envio2.join();
                System.out.println("[" + hiloMain + "] Todos los envíos finalizaron.");

            // FILTRAR ACTIVIDADES
            System.out.println(VERDE + "\n########## EJERCICIO 3: GENÉRICOS Y WILDCARDS ##########" + RESET);

            for (EventoUniversitario ev : List.of(evento1, evento2)) {
                List<Taller> talleres = ev.filtrarActividadesPorTipo(Taller.class);
                List<Charla> charlas  = ev.filtrarActividadesPorTipo(Charla.class);
                List<Curso> cursos    = ev.filtrarActividadesPorTipo(Curso.class);

                System.out.println("\nEVENTO: " + ev.getTitulo());
                System.out.println("Cantidad -> Talleres: " + talleres.size()
                        + " | Charlas: " + charlas.size() + " | Cursos: " + cursos.size());
                System.out.println("Costo materiales -> Talleres: " + ev.calcularCostoMateriales(talleres)
                        + " | Charlas: " + ev.calcularCostoMateriales(charlas)
                        + " | Cursos: " + ev.calcularCostoMateriales(cursos));

                // g) Tipado: métodos propios de cada subtipo, sin cast
                for (Taller t : talleres) {
                    System.out.println("  Taller " + t.getTitulo() + " -> requiere notebook: " + t.isRequiereNotebook());
                }
                for (Charla c : charlas) {
                    System.out.println("  Charla " + c.getTitulo() + " -> disertante: " + c.getDisertante());
                }
                for (Curso c : cursos) {
                    System.out.println("  Curso " + c.getTitulo() + " -> nivel: " + c.getNivel());
                }
            }

            //PERSISTENCIA DEL EVENTO
            boolean persistido = evento1.persistirEvento();
            System.out.println("\nEvento persistido correctamente: " + persistido);

            // LEER
            EventoUniversitario eventoRecuperado = EventoUniversitario.recuperarEvento(evento1.getId());
            System.out.println("Evento recuperado desde archivo:");
            System.out.println("  Id: " + eventoRecuperado.getId()
                    + " | Título: " + eventoRecuperado.getTitulo()
                    + " | Actividades: " + eventoRecuperado.getActividades().size());
            for (Actividad act : eventoRecuperado.getActividades()) {
                System.out.println("  - " + act.getTitulo() + " (" + act.getTipo() + ") inscriptos: "
                        + act.getInscripciones().size());
            }

            System.out.println(AMARILLO + "\n########## EJERCICIO 2: INTERFACES Y CERTIFICACIÓN ##########" + RESET);
            System.out.println("\n EMITIENDO CERTIFICADOS:");

            for (EventoUniversitario ev : List.of(evento1, evento2)) {
                System.out.println("\nEVENTO: " + ev.getTitulo());
                int emitidos = 0;
                for (Actividad actividad : ev.getActividades()) {
                    // Solo Taller y Curso implementan Certificable; Charla no
                    if (actividad instanceof Certificable certificable) {
                        for (Inscripcion i : actividad.getInscripciones()) {
                            System.out.println("  " + certificable.generarCertificado(i.getEstudiante()));
                            emitidos++;
                        }
                    } else {
                        System.out.println("  " + actividad.getTitulo() + " (" + actividad.getTipo() + ") -> no certificable");
                    }
                }
                System.out.println("  Certificados emitidos: " + emitidos);
            }

            // CASO FALLIDO CONTROLADO

            evento1.getActividades().get(1).inscribir(est2);  // 2/1 -> cupoExcedido


        } catch (DatosInvalidosException e) {
            System.err.println("Datos inválidos - no se pudo construir el objeto: " + e.getMessage());
        } catch (CupoExcedidoException e) {
            System.err.println("Error de Cupo " + e.getMessage());
        } catch(FileNotFoundException e){
            System.err.println("NO se encontró el archivo del evento: " + e.getMessage());
        } catch(ClassNotFoundException e) {
            System.err.println("El archivo no corresponde a la clase esperada: " + e.getMessage());
        } catch(IOException e) {
            System.err.println("Error al persistir el evento: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("El hilo principal fue interrumpido: " + e.getMessage());
        }
        finally {
            System.out.println("Finalizó le intento de persistencia/lectura del evento");

            // f) RESUMEN POR EVENTO
            System.out.println("\nf) RESUMEN EVENTOS Y ACTIVIDADES");
            if (evento1 != null) evento1.mostrarDatos();
            if (evento2 != null) evento2.mostrarDatos();

            // g) CANTIDAD EVENTOS
            System.out.println("\n g) CANTIDAD DE EVENTOS: "+ EventoUniversitario.cantidadEventos);

            System.out.println(CIAN + "\n########## EJERCICIO 1: CASO FALLIDO CONTROLADO ##########" + RESET);

        }


    }
    
}