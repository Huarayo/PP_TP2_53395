package hilos;

import jdk.jfr.Event;
import modelo.EventoUniversitario;
import modelo.Inscripcion;
import modelo.actividades.Actividad;
public class EnvioTicketsTheread extends Thread {
    public EventoUniversitario evento;

    public EnvioTicketsTheread(EventoUniversitario evento){
        super("Hilo-Envio-" + evento.getId());
        this.evento = evento;
    }

    // recorre actividades del evento y por cada inscripcion confirmadad con ticket , espera medio segundo y envia
    @Override
    public void run(){
        System.out.println("[" + getName() + "] Inicia envío de tickets de: " + evento.getTitulo());
        for(Actividad act : evento.getActividades()) {
            for(Inscripcion insc : act.getInscripciones()) {
                if(insc.estaConfirmada() && insc.getTicket() != null) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println("[" + getName() + "] Envío interrumpido.");
                        return;
                    }
                    insc.getTicket().enviarTicket();
                }
            }
        }
        System.out.println("[" + getName() + "] Terminó el envío de tickets.");
    }
}


