package modelo;

import modelo.actividades.Actividad;

import java.time.LocalDate;
import java.io.Serializable;

public class Inscripcion implements Serializable{
    private static final String CONFIRMADA = "confirmada";
    private static final long serialVersionUID = 1L;
    private Estudiante estudiante;
    private Actividad actividad;
    private LocalDate fecha;
    private String estado;
    private TicketDeAcceso ticket;

    public Inscripcion(Estudiante estudiante, Actividad actividad){
        this.estudiante = estudiante;
        this.actividad = actividad;
        this.fecha = LocalDate.now();
        this.estado = "activo";
    }

    public void confirmar() {
        this.estado = CONFIRMADA;
    }

    public boolean estaConfirmada() {
        return CONFIRMADA.equals(estado);
    }

    //solo si la inscripcion esta confirmada
    public TicketDeAcceso generarTicket() {
        if(!estaConfirmada()){
            throw new IllegalStateException("No se puede emitir ticket: la inscripcion de " + estudiante.getNombre() + " no está confirmada.");
        }
        if ( ticket == null ) {
            ticket = new TicketDeAcceso();
        }
        return ticket;
    }

    public TicketDeAcceso getTicket() {
        return ticket;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }
    
    public Actividad getActividad(){
        return this.actividad;
    }
    
    public LocalDate getFecha(){
        return this.fecha;
    }
    
    public String getEstado(){
        return estado;
    }
    
    public void setEstado(String estado){
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        return "Inscripcion{" +
                "\nestudiante=" + estudiante.getNombre() +
                "\n, actividad=" + actividad.getTitulo() +
                "\n, fecha=" + fecha +
                "\n, estado='" + estado + '\'' +
                '}';
    }

    //dentro de inscripcion por eso puede usar estudiante y actividad
    public class TicketDeAcceso implements Serializable {
        private static final long serialVersionUID = 1L;
        private String idTicket;
        private LocalDate fechaEmision;

        //constructor privado -> unica forma de crear sea por generarTicket()
        private TicketDeAcceso() {
            this.idTicket = "T-" + estudiante.getLegajo() + "-" + actividad.getId();
            this.fechaEmision = LocalDate.now();
        }

        public void enviarTicket() {
            System.out.println("[" + Thread.currentThread().getName() + "] Ticket " + idTicket
                    + " enviado a " + estudiante.getNombre()
                    + " para " + actividad.getTitulo() + " (" + fechaEmision + ")");
        }

        public String getIdTicket() {
            return idTicket;
        }
    }

}