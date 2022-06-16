package ar.edu.unsl.mys.behaviors;

import ar.edu.unsl.mys.events.Event;
import ar.edu.unsl.mys.engine.AirportSimulation;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.utils.Randomizer;
import ar.edu.unsl.mys.utils.CustomRandomizer;
import ar.edu.unsl.mys.events.HeavyEndOfServiceEvent;

public class HeavyEndOfServiceEventBehavior extends EventBehavior {
    private static HeavyEndOfServiceEventBehavior HeavyEndOfServiceEventBehavior;

    private HeavyEndOfServiceEventBehavior(Randomizer randomizer) {
        super(randomizer);
    }

    public static HeavyEndOfServiceEventBehavior getInstance() {
        if (HeavyEndOfServiceEventBehavior == null) {
            return new HeavyEndOfServiceEventBehavior(CustomRandomizer.getInstance());
        } else
            return HeavyEndOfServiceEventBehavior;
    }

    @Override
    public Event nextEvent(Event actualEvent, Entity entity) {
        HeavyEndOfServiceEvent evento_salida; // Construyo un objeto de tipo evento de salida

        double random = getRandomizer().nextRandom();   // Inicializo con un valor random generado
        int tiempo = 0; // EL tiempo que le voy a sumar al reloj
        if (random < 0.65) {
            tiempo = 40;
        }
        else {
            tiempo = 50;
        }
        // Si la hora de este evento supera a la del fin de simulación, no recolectamos el transitTime
        if (!((actualEvent.getClock() + tiempo) > AirportSimulation.getEndTime())){
            entity.setTransitTime(entity.getWaitingTime() + tiempo);
        }

        evento_salida = new HeavyEndOfServiceEvent(actualEvent.getClock() + tiempo, entity);
        return evento_salida;
    }
}