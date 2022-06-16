package ar.edu.unsl.mys.events;

import java.util.List;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.policies.ServerSelectionPolicy;
import ar.edu.unsl.mys.resources.Server;
import ar.edu.unsl.mys.engine.AirportSimulation;
import ar.edu.unsl.mys.engine.FutureEventList;
import ar.edu.unsl.mys.behaviors.EventBehavior;

public abstract class Event
{
    //attributes
    private float clock;
    private int priority; // Orden de Prioridad: Arribo, Salida, Fin de servicio?

    //associations
    private Entity entity;
    private EventBehavior eventBehavior;

    //other
    /**
     * Used to format toString output
     */
    protected static int END_TIME_DIGITS;

    public Event(float clock2, Entity entity2, ServerSelectionPolicy policy) {    //(PORQUE EXISTE UN CONSTRUCTOR CON LA POLITICA DE SELECCION SI
                                                                                // ESTO SOLO SE ASOCIA CON EL EVENTO DE ARRIBO ?)
    }

    public Event(float clock2, AirportSimulation airportSimulation) {
    }

    // Para el StopSimulation
    public Event(float clock2, int priority, AirportSimulation airportSimulation) {
        this.clock = clock2;
        this.priority = priority;
    }

    // Para el Arribo y Fin de Servicio
    public Event(float clock2, int priority, Entity entity2) {
        this.priority = priority;
        clock = clock2;
        this.entity = entity2;
    }

    

    public int getPriority()
    {
        return priority;

    }

    public float getClock()
    {
        return clock;

    }

    public Entity getEntity()
    {
        return entity;

    }

    public void setEntity(Entity entity)
    {

    }

    public EventBehavior getEventBehavior()
    {
        return eventBehavior;

    }

    public void setEventBehavior(EventBehavior eventBehavior)
    {
        
    }

    /**
     * This method performs the necessary planifications that this event 
     * has to do for the proper execution of bootstrapping.
     * @param servers The list of servers needed to do the planification.
     * @param fel The future event list to insert the next events.
     */
    public abstract void planificate(List<Server> servers, FutureEventList fel);
}