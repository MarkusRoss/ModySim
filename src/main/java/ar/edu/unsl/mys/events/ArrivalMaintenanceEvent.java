package ar.edu.unsl.mys.events;

import ar.edu.unsl.mys.behaviors.ArrivalMaintenanceEventBehavior;
import ar.edu.unsl.mys.behaviors.EndOfServiceMaintenanceEventBehavior;
import ar.edu.unsl.mys.entities.Mantenimiento;
import ar.edu.unsl.mys.policies.RepairServerModelPolicy;
import ar.edu.unsl.mys.policies.ServerSelectionPolicy;

public class ArrivalMaintenanceEvent extends ArrivalEvent{

    public ArrivalMaintenanceEvent(float clock, ServerSelectionPolicy policy) {
        super(clock, RepairServerModelPolicy.getInstance(), new Mantenimiento()); // Lo mismo que el de arriba pero el 2do campo es la prioridad
        setArrivalEventBehavior(ArrivalMaintenanceEventBehavior.getInstance());
        setEndOfServiceEventBehavior(EndOfServiceMaintenanceEventBehavior.getInstance());
    }

}
