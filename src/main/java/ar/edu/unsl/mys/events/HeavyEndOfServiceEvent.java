package ar.edu.unsl.mys.events;

import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.behaviors.HeavyEndOfServiceEventBehavior;

public class HeavyEndOfServiceEvent extends EndOfServiceEvent {

    public HeavyEndOfServiceEvent(float clock, Entity entity) {
        super(clock, entity); // El 2do campo es la prioridad
    }
}