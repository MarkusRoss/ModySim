package ar.edu.unsl.mys.resources;

import java.io.BufferedWriter;
import java.io.FileWriter;

import ar.edu.unsl.mys.engine.AirportSimulation;
import ar.edu.unsl.mys.entities.Entity;

public abstract class Server {

    // Parametros
    private static int idCount = 0;
    private int id;
    private boolean busy;
    private float idleTimeStartMark = 0;
    private float idleTimeFinishMark = 0;
    private float idleTime;
    private float totalIdleTime = 0;
    private float maxIdleTime = 0;
    private boolean isEneable;
    public int getMaxTimeInQueue() {
        return maxTimeInQueue;
    }

    public void setMaxTimeInQueue(int maxTimeInQueue) {
        this.maxTimeInQueue = maxTimeInQueue;
    }

    private int maxTimeInQueue =0;
    // Asociaciones
    private Entity servedEntity;
    private Queue queue;
    private AirportSimulation mainAirport;

    // Extra
    public int tipo;
    public int hpAsig;
    public void setTotalIdleTime(float totalIdleTime) {
        this.totalIdleTime = totalIdleTime;
    }

    public void setMaxIdleTime(float maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public void setHpAsig(int hpAsig) {
        this.hpAsig = hpAsig;
    }

    public float hp;


    public float OcioDivSim;
    public float MaxOcioDivOcio;
    // Reportes por Server
    private String report = "";


    // Constructor
    public Server(Queue queue) {
        initServer(queue); // NUEVO PARAMETRO (La pista ahora puede estar deshabilitada) - METODOS:
                           // isEnable(), setServerEnable(i)
    }

    // Constructor con daño
    public Server(Queue queue, int tipo) {

        initServer(queue);
        this.tipo = tipo;

        switch (tipo) {
            case 1:
                hp = 1000;
                break;
            case 2:
                hp = 3000;
                break;
            case 3:
                hp = 5000;
                break;
            default:
                hp = 0;
        }
        hpAsig = (int) hp;
    }

    // Inicializa el servidor por defecto
    public void initServer(Queue queue) {
        busy = false;
        id = idCount;
        idCount++;
        this.queue = queue;
        isEneable = true;
    }

    // Getters
    public int getTipo() {
        return tipo;
    }
    public float getOcioDivSim() {
        return OcioDivSim;
    }

    public float getMaxOcioDivOcio() {
        return MaxOcioDivOcio;
    }
    public float getHp() {
        return hp;
    }

    public int getHpAsignado() {
        return hpAsig;
    }

    public float getTotalIdleTime() {
        return totalIdleTime;
    }

    public int getId() {
        return id;
    }

    public float getIdleTime() {
        return idleTime;
    }

    public Entity getServedEntity() {
        return servedEntity;
    }

    public Queue getQueue() {
        return queue;
    }

    public float getMaxIdleTime() {
        return maxIdleTime;
    }

    public AirportSimulation getMainAirport() {
        return mainAirport;
    }

    // Setters

    public void setHp(float hp) {
        this.hp = hp;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public void setIdleTimeStartMark(float f) {
        this.idleTimeStartMark = f;
    }

    public void setIdleTimeFinishMark(float f) {
        this.idleTimeFinishMark = f;

        try {
            if (this.idleTimeStartMark != -1 && this.idleTimeFinishMark != -1
                    && this.idleTimeStartMark <= this.idleTimeFinishMark) {

                // Calcula el Tiempo de Ocio de un server
                this.idleTime = this.idleTimeFinishMark - this.idleTimeStartMark;

                // Calcula el Tiempo Total de Ocio
                this.totalIdleTime += this.idleTime;

                // Calcula el Tiempo Maximo de Ocio
                if (this.idleTime > this.maxIdleTime) {
                    this.maxIdleTime = this.idleTime;
                }

            } else {
                throw new Exception("desynchronized idle time marks");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void setServedEntity(Entity servedEntity) {
        this.servedEntity = servedEntity;
    }

    public void setMainAirport(AirportSimulation mainAirport) {
        this.mainAirport = mainAirport;
    }

    public void setServerEnable(boolean i) {
        isEneable = i;
    }

    // Metodos Extra
    public boolean isBusy() {
        return busy;
    }

    public boolean isEnable() {
        return isEneable;
    }

    public void dismHp(float dmg) {
        this.hp -= dmg;
    }

    public void appendReport(String report) {
        // this.report = this.report.concat(report);
    }

    public String getReport() {
        return report;
    }

    public static void reset() {
        idCount = 0;
    }
}