package ar.edu.unsl.mys.events;

import ar.edu.unsl.mys.behaviors.ArrivalEventLightBehavior;
import ar.edu.unsl.mys.behaviors.LightEndOfServiceEventBehavior;
import ar.edu.unsl.mys.entities.LightAircraft;
import ar.edu.unsl.mys.policies.ServerSelectionPolicy;

public class ArrivalEventLight extends ArrivalEvent{

    public ArrivalEventLight(float clock, ServerSelectionPolicy policy) {
        super(clock, policy, new LightAircraft());
        setArrivalEventBehavior(ArrivalEventLightBehavior.getInstance());
        setEndOfServiceEventBehavior(LightEndOfServiceEventBehavior.getInstance());
        tipo=1;
    }
    
}
