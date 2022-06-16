package ar.edu.unsl.mys.events;

import java.util.List;

import ar.edu.unsl.mys.resources.Server;
import ar.edu.unsl.mys.entities.Aircraft;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.engine.FutureEventList;
import ar.edu.unsl.mys.policies.ServerSelectionPolicy;
import ar.edu.unsl.mys.behaviors.ArrivalEventBehavior;
import ar.edu.unsl.mys.behaviors.EndOfServiceEventBehavior;

public class ArrivalEvent extends Event {

    private ServerSelectionPolicy selectionPolicy;
    //private EventBehavior endOfServiceEventBehavior; // Asociacion con sus comportamientos
    //private EventBehavior arrivalEventBehavior;
    //protected int tipo = 1;

    public ArrivalEvent(float clock, ServerSelectionPolicy policy, Entity entity) {
        super(clock, 1, entity);
        selectionPolicy = policy;
    }

    public ServerSelectionPolicy getSelectionPolicy() {
        return selectionPolicy;
    }

    @Override
    public void planificate(List<Server> servers, FutureEventList fel) {

        Entity entity = this.getEntity();
        


        Server servidor = selectionPolicy.selectServer(servers, entity.getType());
        entity.setAttendingServer(servidor);
    
        // Deshabilitar Servidor
        if (entity.getType() == 0){
            entity.disableServer(); 
        }

        if (servidor.isBusy()) { // Verifico si el servidor esta ocupado
            servidor.getQueue().enqueue(entity);

            // ANALITICAS
            servidor.setIdleTimeStartMark(0);
            servidor.setIdleTimeFinishMark(0);
            
        } else { // Si esta desocupado
            servidor.setBusy(true); // Marco el servidor como OCUPADO
            servidor.setServedEntity(entity);
            Event prox_salida = EndOfServiceEventBehavior.getInstance().nextEvent(this, entity);
            entity.setEvent(prox_salida);
            fel.insert(prox_salida); // Guardamos la proxima salida en la FEL

            // ANALITICAS
            servidor.setIdleTimeFinishMark(this.getClock());
        }
        // Planificamos proximo arribo (nueva entidad)
        Event prox_arribo = ArrivalEventBehavior.getInstance().nextEvent(this, new Aircraft(entity.getType()));
        entity.setEvent(this);
        fel.insert(prox_arribo); // Guardamos el proximo arribo en la FEL

        
        
        
        
        analiticas(servidor);
        
        
        //servidor.getMainAirport().appendReport(this.toString() + "\n");
        //servidor.getMainAirport().appendReport("Tiempo de ocio de Pista " +
        //        servidor.getId() + ":" + servidor.getIdleTime() + '\n');
    }

    private void analiticas(Server servidor) {
        // HISTORIA
        String toString = this.toString() + "\n";
        String ocioTime = "Tiempo de ocio de Pista " +
        servidor.getId() + ":" + servidor.getIdleTime() + '\n';

        String queueSize = "Cola de " +
        this.getEntity().getAttendingServer().getId() + " = "
        + this.getEntity().getAttendingServer().getQueue().getQueue().size() + '\n';

        servidor.getMainAirport().appendHistory(toString);
        servidor.getMainAirport().appendHistory(ocioTime);
        servidor.getMainAirport().appendHistory(queueSize);
        servidor.appendReport(toString);
        servidor.appendReport(ocioTime);
    }

    @Override
    public String toString() {
        return "[Evento Arribo, " + this.getEntity().toString() + " Clock: " +
                this.getClock() + " / En servidor:" + this.getEntity().getAttendingServer().getId() + " de tipo:"
                + this.getEntity().getAttendingServer().getTipo() + "]";
    }
}