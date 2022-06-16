package ar.edu.unsl.mys.entities;

import ar.edu.unsl.mys.behaviors.EventBehavior;
import ar.edu.unsl.mys.behaviors.LightEndOfServiceEventBehavior;

public class LightAircraft extends Entity {
    private static int idCount;
    private static int totalWaitingTime;
    private static float maxWaitingTime;
    private static float totalTransitTime;
    private static float maxTransitTime;
    private EventBehavior Comportamiento;
    
    public EventBehavior getComportamiento() {
        return Comportamiento;
    }
    private int id;

    public LightAircraft() {
        super(0);
        id = idCount;
        idCount++;
        a = 0;
        b = 1;
        Comportamiento = LightEndOfServiceEventBehavior.getInstance();
    }


    @Override
    public String toString() {
        return "Id: " + this.getId() + " Avion Liviano";
    }
    public void setWaitingTime(float f) {
        // Calcula el Tiempo de Espera de una entidad
        waitingTime = f;
            totalWaitingTime += f;
            if (waitingTime > maxWaitingTime) {
                maxWaitingTime = waitingTime;
            }
            if(attendingServer.getMaxTimeInQueue()<waitingTime){
                attendingServer.setMaxTimeInQueue((int)waitingTime);
            }
        }
    public void setTransitTime(float transitTime) {
        // System.out.println("\n");

        // Calcula el Tiempo de Transito de una Entidad
        this.transitTime = transitTime;

            // Calcula el Tiempo Total de Transito
            totalTransitTime += transitTime;

            // Calcula el Tiempo Maximo de Transito
            if (this.transitTime > maxTransitTime) {
                maxTransitTime = this.transitTime;
            }
        }
    //estadisticas estaticas
    static public int getTotalWaitingTime() {
        return totalWaitingTime;

    }

    static public float getTotalTransitTime() {
        return totalTransitTime;
    }


    static public float getMaxWaitingTime() {
        return maxWaitingTime;

    }
    static public int getIdCount() {
        return idCount;

        
    }
    static public float getMaxTransitTime() {
        return maxTransitTime;

    }
    //ID
    public int getId() {
        return id;
    }
    //Resetear variables estaticas
    public static void reset() {
        idCount = 0;
        totalWaitingTime = 0;
        maxWaitingTime = 0;
        totalTransitTime = 0;
        maxTransitTime = 0;
    }
}