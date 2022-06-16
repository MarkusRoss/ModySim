package ar.edu.unsl.mys.metrics;

import java.util.Arrays;

import ar.edu.unsl.mys.entities.Entity;

public class AircraftMetric {
    // ANALITICAS DE REPLICACIONES
    int[] cantidadAviones = new int[4];
    float[] tiempoMedioTransito = new float[4];
    float[] tiempoMedioEspera = new float[4];
    float[] tiempoMedioTransitoMax = new float[4];
    float[] tiempoMedioEsperaMax = new float[4];

    
    
    public AircraftMetric() {
        for(int i = 0; i < 4; i++){
            // Metricas de los Aviones
            this.setCantidadAviones(i, Entity.getIdCountSpecific(i));
            this.setTiempoMedioEspera(i, Entity.getTotalWaitingTimeSpec(i) / Entity.getIdCountSpecific(i));
            this.setTiempoMedioEsperaMax(i, Entity.getMaxWaitingTimeSpec(i));
            this.setTiempoMedioTransito(i, Entity.getTotalTransitTimeSpec(i) / Entity.getIdCountSpecific(i));
            this.setTiempoMedioTransitoMax(i, Entity.getMaxTransitTimeSpec(i));
        }
        //System.out.println(this);
    }
    public int getCantidadAviones(int type) {
        return cantidadAviones[type];
    }
    public float getTiempoMedioTransito(int type) {
        return tiempoMedioTransito[type];
    }
    public float getTiempoMedioEspera(int type) {
        return tiempoMedioEspera[type];
    }
    public float getTiempoMedioTransitoMax(int type) {
        return tiempoMedioTransitoMax[type];
    }
    public float getTiempoMedioEsperaMax(int type) {
        return tiempoMedioEsperaMax[type];
    }

    public void setCantidadAviones(int type, int cantidadAviones) {
        this.cantidadAviones[type] = cantidadAviones;
    }
    public void setTiempoMedioTransito(int type, float tiempoMedioTransito) {
        this.tiempoMedioTransito[type] = tiempoMedioTransito;
    }
    public void setTiempoMedioEspera(int type, float tiempoMedioEspera) {
        this.tiempoMedioEspera[type] = tiempoMedioEspera;
    }
    public void setTiempoMedioTransitoMax(int type, float tiempoMedioTransitoMax) {
        this.tiempoMedioTransitoMax[type] = tiempoMedioTransitoMax;
    }
    public void setTiempoMedioEsperaMax(int type, float tiempoMedioEsperaMax) {
        this.tiempoMedioEsperaMax[type] = tiempoMedioEsperaMax;
    }
    @Override
    public String toString() {
        return "AircraftMetric [cantidadAviones=" + Arrays.toString(cantidadAviones) + ", tiempoMedioEspera="
                + Arrays.toString(tiempoMedioEspera) + ", tiempoMedioEsperaMax=" + Arrays.toString(tiempoMedioEsperaMax)
                + ", tiempoMedioTransito=" + Arrays.toString(tiempoMedioTransito) + ", tiempoMedioTransitoMax="
                + Arrays.toString(tiempoMedioTransitoMax) + "]";
    }

    

    
}
