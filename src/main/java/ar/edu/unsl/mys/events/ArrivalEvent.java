package ar.edu.unsl.mys.events;

import java.util.List;

import ar.edu.unsl.mys.resources.Server;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.entities.Mantenimiento;
import ar.edu.unsl.mys.engine.FutureEventList;
import ar.edu.unsl.mys.policies.ServerSelectionPolicy;
import ar.edu.unsl.mys.behaviors.ArrivalEventLightBehavior;
import ar.edu.unsl.mys.behaviors.LightEndOfServiceEventBehavior;
import ar.edu.unsl.mys.behaviors.EventBehavior;

public class ArrivalEvent extends Event {

    private ServerSelectionPolicy selectionPolicy;
    private EventBehavior endOfServiceEventBehavior; // Asociacion con sus comportamientos
    private EventBehavior arrivalEventBehavior;
    protected int tipo = 1;

    public ArrivalEvent(float clock, ServerSelectionPolicy policy, Entity entity) {

        super(clock, 1, entity);
        selectionPolicy = policy;
        setArrivalEventBehavior(ArrivalEventLightBehavior.getInstance());
        setEndOfServiceEventBehavior(LightEndOfServiceEventBehavior.getInstance());
    }

    public ServerSelectionPolicy getSelectionPolicy() {
        return selectionPolicy;
    }

    public EventBehavior getEndOfServiceEventBehavior() {
        return endOfServiceEventBehavior;
    }

    public void setEndOfServiceEventBehavior(EventBehavior endOfServiceEventBehavior) {
        this.endOfServiceEventBehavior = endOfServiceEventBehavior;
    }

    public EventBehavior getArrivalEventBehavior() {
        return arrivalEventBehavior;
    }

    public void setArrivalEventBehavior(EventBehavior arrivalEventBehavior) {
        this.arrivalEventBehavior = arrivalEventBehavior;
    }

    @Override
    public void planificate(List<Server> servers, FutureEventList fel) {

        Server servidor = selectionPolicy.selectServer(servers, tipo);

        if (this.getEntity() instanceof Mantenimiento) // El mantenimiento entra, por ello se deshabilita la pista
            servidor.setServerEnable(false);

        if (servidor.isBusy()) { // Verifico si el servidor esta ocupado
            this.getEntity().setAttendingServer(servidor);
            servidor.getQueue().enqueue(this.getEntity());

            // ANALITICAS
            servidor.setIdleTimeStartMark(0);
            servidor.setIdleTimeFinishMark(0);

        } else { // Si esta desocupado
            servidor.setBusy(true); // Marco el servidor como OCUPADO
            servidor.setServedEntity(this.getEntity());
            this.getEntity().setAttendingServer(servidor);
            Event prox_salida = endOfServiceEventBehavior.nextEvent // Planificamos proxima salida (esta
                                                                    // entidad)
            (this, this.getEntity());
            this.getEntity().setEvent(prox_salida);
            fel.insert(prox_salida); // Guardamos la proxima salida en la FEL

            // ANALITICAS
            servidor.setIdleTimeFinishMark(this.getClock());
        }

        Event prox_arribo = arrivalEventBehavior.nextEvent // Planificamos proximo arribo (nueva entidad)
        (this, null);
        this.getEntity().setEvent(this);
        fel.insert(prox_arribo); // Guardamos el proximo arribo en la FEL
        // HISTORIA
        String toString = this.toString() + "\n";
        String ocioTime = "Tiempo de ocio de Pista " +
        servidor.getId() + ":" + servidor.getIdleTime() + '\n';

        servidor.getMainAirport().appendHistory(toString);
        servidor.getMainAirport().appendHistory(ocioTime);
        servidor.appendReport(toString);
        servidor.appendReport(ocioTime);
        
        
        //servidor.getMainAirport().appendReport(this.toString() + "\n");
        //servidor.getMainAirport().appendReport("Tiempo de ocio de Pista " +
        //        servidor.getId() + ":" + servidor.getIdleTime() + '\n');
        
        

    }

    @Override
    public String toString() {
        //
        return "[ Evento Arribo," + this.getEntity().toString() + " Clock: " + this.getClock()
                + " / En servidor:" + this.getEntity().getAttendingServer().getId() + " de tipo: "
                + this.getEntity().getAttendingServer().getTipo() +" Cola:" + this.getEntity().getAttendingServer().getQueue().getQueue().size()+" ]";

    }

}