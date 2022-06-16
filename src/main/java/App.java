//package ar.edu.unsl.mys;

import java.util.HashMap;

import ar.edu.unsl.mys.engine.AirportSimulation;
import ar.edu.unsl.mys.engine.Engine;
import ar.edu.unsl.mys.policies.MultiServerModelPolicy;

public class App {
    private static final int MIN_PER_DAY = 1440;
    private static final int NUMBER_OF_DAYS = 28;
    private static final int EXECUTION_TIME = MIN_PER_DAY * NUMBER_OF_DAYS;
    private static final int SIMULATION_TIMES = 10;

    private static HashMap<Integer, Integer> SERVERS_NUMBER;

    public static void main(String[] args) {

        System.out.print("------------------------------\n");
        System.out.print("--------- EJECUCION ----------\n");
        System.out.print("------------------------------\n");
        // Agregamos una variable costo para poder saber como afectan los cambios
        // El costo va variando acorde a la creación de pistas y al mantenimiento.
        // Pista Ligera: 1000; Pista Mediana: 2000; Pista Pesada: 3000; Mantenimiento:
        // 500;
        SERVERS_NUMBER = new HashMap<>();
        SERVERS_NUMBER.put(1, 1);
        SERVERS_NUMBER.put(2, 2);
        SERVERS_NUMBER.put(3, 1);
        
        // La mejor configuracion es: 1 pista ligera, 2 pistas medianas, y 1 pista
        // pesada.
        Engine engine = new AirportSimulation(SERVERS_NUMBER, EXECUTION_TIME, MultiServerModelPolicy.getInstance());
        for (int i = 0; i < SIMULATION_TIMES; i++) {
            engine.execute();
            //engine.generateHistory(true, "Historia_" + i + ".txt");
            //engine.generateReport(true, "Reporte_" + i + ".txt");
            engine.reset();
        }
        engine.showReplicationAnalitics();
    }
}