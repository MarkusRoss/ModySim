package ar.edu.unsl.mys.events;

import java.util.List;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.resources.Server;
import ar.edu.unsl.mys.engine.FutureEventList;
import ar.edu.unsl.mys.behaviors.EndOfServiceEventBehavior;

public class EndOfServiceEvent extends Event {



    public EndOfServiceEvent(float clock, Entity entity) {
        super(clock, 0, entity); // El 2do campo es la prioridad
    }
    @Override
    public String toString() {
        return "[Evento Salida, " + this.getEntity().toString() + " Clock: " +
                this.getClock() + " / En servidor:" + this.getEntity().getAttendingServer().getId() + " de tipo:"
                + this.getEntity().getAttendingServer().getTipo() + "]";
    }

    @Override
    public void planificate(List<Server> servers, FutureEventList fel) {

        Server servidor = getEntity().getAttendingServer(); // El servidor es quien atendio a la entidad que
        Entity entidad = getEntity();

        if (!(servidor.getQueue().isEmpty())) { // Si hay cola en este servidor
            Entity entidadInQueue = servidor.getQueue().next();
            entidadInQueue.setWaitingTime(getClock() - entidadInQueue.getArrivalEvent().getClock());
            
            Event prox_salida = EndOfServiceEventBehavior.getInstance().nextEvent(this, entidadInQueue);
            servidor.setServedEntity(entidadInQueue);
            entidadInQueue.setEvent(prox_salida);
            fel.insert(prox_salida); // Guardo la proxima salida en la FEL

        } else {
            servidor.setServedEntity(null);
            servidor.setBusy(false); // Si NO hay cola significa que el servidor no espera
                                     // mas entidades para salir, asi que esta libre
            servidor.setIdleTimeStartMark(this.getClock()); // Comienza el tiempo de ocio de la pista (No tiene cola)
        }

        // Habilitar Servidor y Repararlo
        if (entidad.getType() == 0) {
            entidad.enableServer();//.getAttendingServer().setServerEnable(true);
            entidad.reparar();
        }

        entidad.aplicarDanio(); // El mantenimiento aplica un daño de 0 (No modifica el HP)

        // HISTORIA
        String toString = this.toString() + "\n";
        String esperaTime = "Tiempo de espera de " +
        this.getEntity().getId() + " = "
        + this.getEntity().getWaitingTime() + '\n';
        String transitTime = "Tiempo de transito de " +
        this.getEntity().getId() + " = "
        + this.getEntity().getTransitTime() + '\n';

        String queueSize = "Cola de " +
        this.getEntity().getAttendingServer().getId() + " = "
        + this.getEntity().getAttendingServer().getQueue().getQueue().size() + '\n';

        servidor.getMainAirport().appendHistory(toString);
        servidor.getMainAirport().appendHistory(esperaTime);
        servidor.getMainAirport().appendHistory(transitTime);
        servidor.getMainAirport().appendHistory(queueSize);
        
        servidor.appendReport(toString);
        servidor.appendReport(esperaTime);
        servidor.appendReport(transitTime);


        /*servidor.getMainAirport().appendReport(this.toString() + "\n");
        servidor.getMainAirport().appendReport("Tiempo de espera de " +
                this.getEntity().getId() + " = "
                + this.getEntity().getWaitingTime() + '\n');
        servidor.getMainAirport().appendReport("Tiempo de transito de " +
                this.getEntity().getId() + " = "
                + this.getEntity().getTransitTime() + '\n');*/
    }
}