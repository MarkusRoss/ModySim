package ar.edu.unsl.mys.events;

import ar.edu.unsl.mys.behaviors.LightEndOfServiceEventBehavior;
import ar.edu.unsl.mys.entities.Entity;

public class LightEndOfServiceEvent extends EndOfServiceEvent{
    
    public LightEndOfServiceEvent(float clock, Entity entity) {
        super(clock, entity); // El 2do campo es la prioridad
    }
}
