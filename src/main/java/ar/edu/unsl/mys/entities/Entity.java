package ar.edu.unsl.mys.entities;

import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import ar.edu.unsl.mys.events.Event;
import ar.edu.unsl.mys.resources.Server;
import ar.edu.unsl.mys.utils.CustomRandomizer;
import ar.edu.unsl.mys.utils.Randomizer;
import ar.edu.unsl.mys.behaviors.EventBehavior;
import ar.edu.unsl.mys.events.ArrivalEvent;
import ar.edu.unsl.mys.events.EndOfServiceEvent;

public abstract class Entity {
    private static int idCount;
    private static int totalWaitingTime;
    private static float maxWaitingTime;
    private static float totalTransitTime;
    private static float maxTransitTime;
    private EventBehavior Comportamiento;
    public EventBehavior getComportamiento() {
        return Comportamiento;
    }

    // attributes
    protected int id;
    protected float waitingTime;
    protected float transitTime;
    protected int type;
    protected int a, b;
    // associations
    protected Server attendingServer;
    protected List<Event> events;
    private Randomizer randomizer;

    public Entity(int type) {
        randomizer = CustomRandomizer.getInstance(); // Instancia necesaria para general variables aleatorias uniformes
        this.id = idCount;
        this.type = type;
        idCount++;
        events = new ArrayList<>();
        transitTime = 0;
        waitingTime = 0;
    }

    public void aplicarDanio() { // Disminuye el HP del servidor que la atiende por una cantidad resultante de
                                 // una
                                 // distribucion Uniforme [a -- b]
        attendingServer.dismHp(randomizer.nextUniforme(a, b));
        if (attendingServer.getHp() < 0) {
            attendingServer.setHp(0);
            attendingServer.setServerEnable(false);
        }
    }

    public void reparar() { // Repara un 15% con respecto el hp total

        Server server = this.getAttendingServer();
        float reparo = (float) (server.getHp() + server.getHpAsignado() * 0.15);

        if (reparo > server.getHpAsignado()) {
            reparo = server.getHpAsignado();
            this.getAttendingServer().setHp(reparo);
        } else
            this.getAttendingServer().setHp(reparo);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
//Estadisticas:
    static public int getTotalWaitingTime() {
        return totalWaitingTime;

    }

    static public float getTotalTransitTime() {
        return totalTransitTime;
    }

    static public int getIdCount() {
        return idCount;

    }

    static public float getMaxWaitingTime() {
        return maxWaitingTime;

    }

    public float getAccumulatedTransitTime() {
        return totalTransitTime;

    }

    static public float getMaxTransitTime() {
        return maxTransitTime;

    }

    public int getId() {
        return this.id;
    }

    public float getWaitingTime() {
        return waitingTime;

    }

    public void setWaitingTime(float f) {
        // Calcula el Tiempo de Espera de una entidad
        waitingTime = f;
        if (!(this instanceof Mantenimiento)) {
            // Calcula el Tiempo Total de Espera
            totalWaitingTime += f;

            // Calcula el Tiempo Maximo de Espera
            if (waitingTime > maxWaitingTime) {
                maxWaitingTime = waitingTime;
                
            }
            if(attendingServer.getMaxTimeInQueue()<waitingTime){
                attendingServer.setMaxTimeInQueue((int)waitingTime);
            }
        }
    }

    public float getTransitTime() {
        return transitTime;
    }

    public void setTransitTime(float transitTime) {
        // System.out.println("\n");

        // Calcula el Tiempo de Transito de una Entidad
        this.transitTime = transitTime;

        if (!(this instanceof Mantenimiento)) {
            // Calcula el Tiempo Total de Transito
            totalTransitTime += transitTime;

            // Calcula el Tiempo Maximo de Transito
            if (this.transitTime > maxTransitTime) {
                maxTransitTime = this.transitTime;
            }
        }

    }

    public Event getArrivalEvent() {
        ListIterator<Event> it = events.listIterator();
        while (it.hasNext()) {
            Event a = it.next();
            if (a instanceof ArrivalEvent) {
                return (ArrivalEvent) a;
            }
        }
        return null;

    }

    public Event getEndOfServiceEvent() {
        ListIterator<Event> it = events.listIterator();
        while (it.hasNext()) {
            Event a = it.next();
            if (a instanceof EndOfServiceEvent) {
                return (EndOfServiceEvent) a;
            }
        }
        return null;

    }

    public Server getAttendingServer() {
        return attendingServer;
    }

    public void setAttendingServer(Server attendingServer) {
        this.attendingServer = attendingServer;
    }

    public List<Event> getEvents() {
        return events;

    }

    public void setEvent(Event event) {
        events.add(event);
    }

    @Override
    public String toString() {
        return "Entity [attendingServer=" + attendingServer + ", events=" + events + ", id=" + id + ", transitTime="
                + transitTime + ", waitingTime=" + waitingTime + "]";
    }

    public static void reset() {
        idCount = 0;
        totalWaitingTime = 0;
        maxWaitingTime = 0;
        totalTransitTime = 0;
        maxTransitTime = 0;
    }
}