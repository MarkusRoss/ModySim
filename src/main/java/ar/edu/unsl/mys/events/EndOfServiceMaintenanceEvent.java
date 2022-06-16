package ar.edu.unsl.mys.events;

import ar.edu.unsl.mys.behaviors.EndOfServiceMaintenanceEventBehavior;
import ar.edu.unsl.mys.entities.Entity;

public class EndOfServiceMaintenanceEvent extends EndOfServiceEvent{

    public EndOfServiceMaintenanceEvent(float clock, Entity entity) {
        super(clock, entity);
    }

}
