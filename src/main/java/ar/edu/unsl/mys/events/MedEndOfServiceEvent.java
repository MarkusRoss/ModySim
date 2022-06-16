package ar.edu.unsl.mys.events;

import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.behaviors.MedEndOfServiceEventBehavior;

public class MedEndOfServiceEvent extends EndOfServiceEvent {

    public MedEndOfServiceEvent(float clock, Entity entity) {
        super(clock, entity);
    }

}