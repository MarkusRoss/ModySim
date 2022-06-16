package ar.edu.unsl.mys.behaviors;

import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.events.ArrivalMaintenanceEvent;
import ar.edu.unsl.mys.events.Event;
import ar.edu.unsl.mys.policies.RepairServerModelPolicy;
import ar.edu.unsl.mys.resources.Server;
import ar.edu.unsl.mys.utils.CustomRandomizer;
import ar.edu.unsl.mys.utils.Randomizer;

public class ArrivalMaintenanceEventBehavior extends EventBehavior {

    private static ArrivalMaintenanceEventBehavior arrivalMaintenanceEventMedBehavior;

    public ArrivalMaintenanceEventBehavior(Randomizer randomizer) {
        super(randomizer);
    }

    public static ArrivalMaintenanceEventBehavior getInstance() {
        if (arrivalMaintenanceEventMedBehavior == null) {
            return new ArrivalMaintenanceEventBehavior(CustomRandomizer.getInstance());
        } else
            return arrivalMaintenanceEventMedBehavior;
    }

    @Override
    public Event nextEvent(Event actualEvent, Entity entity) {
        ArrivalMaintenanceEvent evento_Arribo; // Construyo un objeto de tipo evento de arribo

        Server servidor = actualEvent.getEntity().getAttendingServer();

        float tiempo = getRandomizer().nextGaussiano(5, 0.5); // Tiempo en minutos

        tiempo *= 1440; // (1 Dia - 1440 min) Tiempo en dias

        actualEvent.getEntity().getAttendingServer().setServerEnable(false);

        servidor.getMainAirport()
                .appendHistory(" Proximo mantenimiento: " + tiempo / 1440
                        + " dias\n");
        servidor.getMainAirport().appendHistory("--------------------------------------------------------\n\n");


        evento_Arribo = new ArrivalMaintenanceEvent(actualEvent.getClock() + tiempo,
                RepairServerModelPolicy.getInstance());
        return evento_Arribo; // Retornar el evento
    }

}
