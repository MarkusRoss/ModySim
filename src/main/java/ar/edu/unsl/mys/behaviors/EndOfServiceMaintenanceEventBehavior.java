package ar.edu.unsl.mys.behaviors;

import ar.edu.unsl.mys.engine.AirportSimulation;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.events.EndOfServiceMaintenanceEvent;
import ar.edu.unsl.mys.events.Event;
import ar.edu.unsl.mys.utils.CustomRandomizer;
import ar.edu.unsl.mys.utils.Randomizer;

public class EndOfServiceMaintenanceEventBehavior extends EventBehavior {

    private static EndOfServiceMaintenanceEventBehavior endOfServiceMaintenanceEventBehavior;

    private EndOfServiceMaintenanceEventBehavior(Randomizer randomizer) {
        super(randomizer);
    }

    public static EndOfServiceMaintenanceEventBehavior getInstance() {
        if (endOfServiceMaintenanceEventBehavior == null) {
            return new EndOfServiceMaintenanceEventBehavior(CustomRandomizer.getInstance());
        } else
            return endOfServiceMaintenanceEventBehavior;
    }

    @Override
    public Event nextEvent(Event actualEvent, Entity entity) {

        EndOfServiceMaintenanceEvent evento_salida; // Construyo un objeto de tipo evento de salida

        // Generacion de un numero entre 12 - 24 con distribucion uniforme
        float tiempo = getRandomizer().nextUniforme(12, 24); // Tiempo en minutos

        tiempo *= 60; // (1 Hora - 60 min) Tiempo en horas

        // Mientras el tiempo de transito no supere a el tiempo de simulacion, se
        // establece
        if (!((actualEvent.getClock() + tiempo) > AirportSimulation.getEndTime()))
            entity.setTransitTime(entity.getWaitingTime() + tiempo);

        evento_salida = new EndOfServiceMaintenanceEvent(actualEvent.getClock() + tiempo, entity); // Proximo evento de
                                                                                                   // salida
        AirportSimulation.costo += 500; // Agregamos el costo del Mantenimiento
        return evento_salida; 
    }

}
