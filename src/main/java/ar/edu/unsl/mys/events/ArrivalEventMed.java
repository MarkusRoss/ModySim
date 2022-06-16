package ar.edu.unsl.mys.events;

import ar.edu.unsl.mys.policies.ServerSelectionPolicy;
import ar.edu.unsl.mys.behaviors.ArrivalEventMedBehavior;
import ar.edu.unsl.mys.behaviors.MedEndOfServiceEventBehavior;
import ar.edu.unsl.mys.entities.MediumAircraft;

public class ArrivalEventMed extends ArrivalEvent { 
    
    public ArrivalEventMed(float clock, ServerSelectionPolicy policy) {
        super(clock, policy, new MediumAircraft());
        setArrivalEventBehavior(ArrivalEventMedBehavior.getInstance());
        setEndOfServiceEventBehavior(MedEndOfServiceEventBehavior.getInstance());
        tipo=2;
    }
}