package ar.edu.unsl.mys.entities;

import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import ar.edu.unsl.mys.events.Event;
import ar.edu.unsl.mys.resources.Server;
import ar.edu.unsl.mys.utils.CustomRandomizer;
import ar.edu.unsl.mys.utils.Randomizer;
import ar.edu.unsl.mys.events.ArrivalEvent;
import ar.edu.unsl.mys.events.EndOfServiceEvent;

public abstract class Entity {
    private static int idCount;
    private static int[] idCountSpecific = new int[4];
    private static int totalWaitingTime;
    private static float maxWaitingTime;
    private static float totalTransitTime;
    private static float maxTransitTime;

    // atributos especificos
    private static int[] totalWaitingTimeSpec = new int[4];
    private static float[] maxWaitingTimeSpec = new float[4];
    private static float[] totalTransitTimeSpec = new float[4];
    private static float[] maxTransitTimeSpec = new float[4];

    // attributes
    private int id;
    private float waitingTime;
    private float transitTime;
    private int type;
    //protected int a, b;
    // associations
    private Server attendingServer;
    protected List<Event> events;
    private Randomizer randomizer;

    public Entity(int type) {
        randomizer = CustomRandomizer.getInstance(); // Instancia necesaria para general variables aleatorias uniformes
        this.id = idCount;
        this.type = type;
        idCount++;
        idCountSpecific[type]++;
        events = new ArrayList<>(); // ?
        transitTime = 0;
        waitingTime = 0;
    }

    public void aplicarDanio() { // Disminuye el HP del servidor que la atiende por una cantidad resultante de
                                 // una
                                 // distribucion Uniforme [a -- b]
        

        int a;
        int b;
        switch(this.getType()){
            case 1:
            a = 0;
            b = 1;
            break;
            case 2:
            a = 1;
            b = 4;
            break;
            case 3:
            a = 3;
            b = 6;
            break;
            default:
            a = 0;
            b = 0;
            break;
        }
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

    static public int getTotalWaitingTime() {
        return totalWaitingTime;

    }

    static public float getTotalTransitTime() {
        return totalTransitTime;
    }

    static public int getIdCount() {
        return idCount;
    }

    static public int getIdCountSpecific(int type) {
        return idCountSpecific[type];
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
        this.waitingTime = f;
        
        if (getType() > 0){
            // Calcula el Tiempo Total de Espera
            totalWaitingTime += f;
            totalWaitingTimeSpec[getType()] += f;

            // Calcula el Tiempo Maximo de Espera
            if (this.waitingTime > maxWaitingTime) {
                maxWaitingTime = this.waitingTime;
                maxWaitingTimeSpec[getType()] = this.waitingTime;
            }
        }
        
    }

    public float getTransitTime() {
        return this.transitTime;
    }

    public void setTransitTime(float transitTime) {
        // Calcula el Tiempo de Transito de una Entidad
        this.transitTime = transitTime;
        if (getType() > 0){
            // Calcula el Tiempo Total de Transito
            totalTransitTime += transitTime;
            totalTransitTimeSpec[getType()] += transitTime;

            // Calcula el Tiempo Maximo de Transito
            if (this.transitTime > maxTransitTime) {
                maxTransitTime = this.transitTime;
                maxTransitTimeSpec[getType()] = this.transitTime;
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

    public static int getTotalWaitingTimeSpec(int type){
        return totalWaitingTimeSpec[type];
    }
    public static float getMaxWaitingTimeSpec(int type){
        return maxWaitingTimeSpec[type];
    }
    public static float getTotalTransitTimeSpec(int type){
        return totalTransitTimeSpec[type];
    }
    public static float getMaxTransitTimeSpec(int type){
        return maxTransitTimeSpec[type];
    }


    @Override
    public String toString() {
        return "Entity [attendingServer=" + attendingServer + ", events=" + events + ", id=" + id + ", transitTime="
                + transitTime + ", waitingTime=" + waitingTime + "]";
    }

    public static void reset() {
        idCount = 0;
        for(int i = 0; i < idCountSpecific.length - 1; i++){
            idCountSpecific[i] = 0;
        }
        totalWaitingTime = 0;
        maxWaitingTime = 0;
        totalTransitTime = 0;
        maxTransitTime = 0;
    }

    public void disableServer() {
        attendingServer.setServerEnable(false);
    }
    
    public void enableServer() {
        attendingServer.setServerEnable(true);
    }
    
    public static String typeToString(int type){
        String t;
        switch(type){
            case 0:
                t = "Mantenimiento";
            break;
            case 1:
                t = "Avion Liviano";
            break;
            case 2:
                t = "Avion Mediano";
            break;
            case 3:
                t = "Avion Pesado";
            break;
            default:
                t = "No definido";
            break;
        }
        return t;
    }
}