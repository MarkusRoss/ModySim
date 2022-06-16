package ar.edu.unsl.mys.behaviors;

import ar.edu.unsl.mys.events.Event;
import ar.edu.unsl.mys.events.LightEndOfServiceEvent;
import ar.edu.unsl.mys.engine.AirportSimulation;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.utils.Randomizer;
import ar.edu.unsl.mys.utils.CustomRandomizer;
import ar.edu.unsl.mys.events.EndOfServiceEvent;

public class LightEndOfServiceEventBehavior extends EventBehavior {
    private static LightEndOfServiceEventBehavior LightEndOfServiceEventBehavior;

    private LightEndOfServiceEventBehavior(Randomizer randomizer) {
        super(randomizer);
    }

    public static LightEndOfServiceEventBehavior getInstance() {
        if (LightEndOfServiceEventBehavior == null) {
            return new LightEndOfServiceEventBehavior(CustomRandomizer.getInstance());
        } else
            return LightEndOfServiceEventBehavior;
    }

    @Override
    public Event nextEvent(Event actualEvent, Entity entity) {
        LightEndOfServiceEvent evento_salida; // Construyo un objeto de tipo evento de salida

        double random = getRandomizer().nextRandom(); // Inicializo con un valor random generado
        int tiempo = 0; // EL tiempo que le voy a sumar al reloj
        if (random < 0.363) {
            tiempo = 5;
        } else if (random < 0.838) {
            tiempo = 10;
        } else {
            tiempo = 15;
        }

        // Si la hora de este evento supera a la del fin de simulación, no recolectamos
        // el transitTime
        if (!((actualEvent.getClock() + tiempo) > AirportSimulation.getEndTime())) {
            entity.setTransitTime(entity.getWaitingTime() + tiempo);
        }

        evento_salida = new LightEndOfServiceEvent(actualEvent.getClock() + tiempo, entity);

        return evento_salida;
    }
}