package ar.edu.unsl.mys.behaviors;

import ar.edu.unsl.mys.events.Event;
import ar.edu.unsl.mys.engine.AirportSimulation;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.utils.Randomizer;
import ar.edu.unsl.mys.utils.CustomRandomizer;
import ar.edu.unsl.mys.events.MedEndOfServiceEvent;

public class MedEndOfServiceEventBehavior extends EventBehavior {
    private static MedEndOfServiceEventBehavior MedEndOfServiceEventBehavior;

    private MedEndOfServiceEventBehavior(Randomizer randomizer) {
        super(randomizer);
    }

    public static MedEndOfServiceEventBehavior getInstance() {
        if (MedEndOfServiceEventBehavior == null) {
            return new MedEndOfServiceEventBehavior(CustomRandomizer.getInstance());
        } else
            return MedEndOfServiceEventBehavior;
    }

    @Override
    public Event nextEvent(Event actualEvent, Entity entity) {
        MedEndOfServiceEvent evento_salida; // Construyo un objeto de tipo evento de salida

        int tiempo = (int)getRandomizer().nextUniforme(10,20);

        // Si la hora de este evento supera a la del fin de simulación, no recolectamos el transitTime
        if (!((actualEvent.getClock() + tiempo) > AirportSimulation.getEndTime())){
            entity.setTransitTime(entity.getWaitingTime() + tiempo);
        }

        evento_salida = new MedEndOfServiceEvent(actualEvent.getClock() + tiempo, entity);
        return evento_salida; 
    }
}