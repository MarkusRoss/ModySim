package ar.edu.unsl.mys.events;
import ar.edu.unsl.mys.entities.HeavyAircraft;
import ar.edu.unsl.mys.policies.ServerSelectionPolicy;
import ar.edu.unsl.mys.behaviors.ArrivalEventHeavyBehavior;
import ar.edu.unsl.mys.behaviors.HeavyEndOfServiceEventBehavior;

public class ArrivalEventHeavy extends ArrivalEvent {

    public ArrivalEventHeavy(float clock, ServerSelectionPolicy policy) {

        super(clock, policy, new HeavyAircraft()); // Lo mismo que el de arriba pero el 2do campo es la prioridad
        setArrivalEventBehavior(ArrivalEventHeavyBehavior.getInstance());
        setEndOfServiceEventBehavior(HeavyEndOfServiceEventBehavior.getInstance());
        tipo = 3;
    }
}