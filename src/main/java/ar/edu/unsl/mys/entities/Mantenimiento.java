package ar.edu.unsl.mys.entities;

import ar.edu.unsl.mys.behaviors.EndOfServiceMaintenanceEventBehavior;
import ar.edu.unsl.mys.behaviors.EventBehavior;

public class Mantenimiento extends Entity {

    private static int IdCount;
    private int id;
    private  EventBehavior Comportamiento;
    
    public EventBehavior getComportamiento() {
        return Comportamiento;
    }
    public Mantenimiento() {
        super(3);
        id = IdCount;
        IdCount++;
        Comportamiento = EndOfServiceMaintenanceEventBehavior.getInstance();
    }

    @Override
    public String toString() {
        return "Id: " + this.getId() + " Mantenimiento";
    }

    public int getIdMantenimiento() {
        return id;
    }

    public static void reset() {
        IdCount = 0;
    }
}
