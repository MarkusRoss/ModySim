package ar.edu.unsl.mys.engine;

import java.util.List;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.io.BufferedWriter;

import ar.edu.unsl.mys.resources.Server;
import ar.edu.unsl.mys.resources.Airstrip;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.entities.Aircraft;
import ar.edu.unsl.mys.events.ArrivalEvent;
import ar.edu.unsl.mys.events.Event;
import ar.edu.unsl.mys.resources.CustomQueue;
import ar.edu.unsl.mys.events.StopExecutionEvent;
import ar.edu.unsl.mys.metrics.AircraftMetric;
import ar.edu.unsl.mys.metrics.AirstripMetric;
import ar.edu.unsl.mys.metrics.AirstripMetricMini;
import ar.edu.unsl.mys.policies.RepairServerModelPolicy;
import ar.edu.unsl.mys.policies.ServerSelectionPolicy;

/**
 * Event oriented simulation of an airport
 */
public class AirportSimulation implements Engine {
    public static int costo;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    private String report = "==============================================================================================\n"
            +
            "                                        R E P O R T                                           \n" +
            "==============================================================================================\n" +
            "\n" + dtf.format(now) + "\n";
    private String history = "==============================================================================================\n"
            +
            "                                        H Y S T O R Y                                           \n" +
            "==============================================================================================\n" +
            "\n" + dtf.format(now) + "\n";
    public static int endTime;
    private boolean stopSimulation;
    private FutureEventList fel;
    private List<Server> servers;
    private ServerSelectionPolicy selectionPolicy;
    private HashMap<Integer, Integer> tipo_cantidad;

    // ANALITICAS DE REPLICACIONES
    List<AircraftMetric> metricasAviones = new ArrayList<>();
    List<AirstripMetric> metricasPistas = new ArrayList<>();



    public AirportSimulation(HashMap<Integer, Integer> tipo_cantidad, int endTime, ServerSelectionPolicy policy) {
        this.tipo_cantidad = tipo_cantidad;
        initialize(tipo_cantidad, endTime, policy);

    }

    private void initialize(HashMap<Integer, Integer> tipo_cantidad, int endTime, ServerSelectionPolicy policy) {
        // INICIALIZAR

        fel = new FutureEventList(); // La lista de eventos futuros
        AirportSimulation.endTime = endTime; // El fin de tiempo de la simulacion
        servers = new ArrayList<>(); // La lista de servidores
        stopSimulation = false;
        this.selectionPolicy = policy; // Almacenamos la politica de selección

        // Crear la lista de pistas dado un HashMap
        createAirstrips(tipo_cantidad);

        // Cargar la fel con el evento de fin y un evento de inicio para la simulacion
        StopExecutionEvent evento_fin = new StopExecutionEvent(endTime, this);
        ArrivalEvent arriboLigero = new ArrivalEvent(0, policy, new Aircraft(1));
        ArrivalEvent arriboMediano = new ArrivalEvent(0, policy, new Aircraft(2));
        ArrivalEvent arriboPesado = new ArrivalEvent(0, policy, new Aircraft(3));
        ArrivalEvent arriboMantenimiento = new ArrivalEvent(1440,
        RepairServerModelPolicy.getInstance(), new Aircraft(0));

        fel.insert(evento_fin);
        fel.insert(arriboLigero);
        fel.insert(arriboMediano);
        fel.insert(arriboPesado);
        fel.insert(arriboMantenimiento);
    }

    public static int getEndTime() {
        return endTime;
    }

    public ServerSelectionPolicy getSelectionPolicy() {
        return selectionPolicy;
    }

    public void setStopSimulation(boolean stopSimulation) {
        this.stopSimulation = stopSimulation;
    }

    public void addAirstrip(int type) {
        CustomQueue cola_espera = new CustomQueue();
        Airstrip pista = new Airstrip(cola_espera, type);
        cola_espera.setAssignedServer(pista);
        pista.setMainAirport(this);
        servers.add(pista);
        costo += 1000 * type; // Por cada pista creada, vamos aumentando el costo
        // Pista Ligera: 1000; Pista Mediana: 2000; Pista Pesada: 3000;
    }

    public void createAirstrips(HashMap<Integer, Integer> map) { // Creacion de pistas
        java.util.Iterator<Integer> it = map.keySet().iterator(); // Iterador para el Map(tipo'key', cantidad'value')

        while (it.hasNext()) { // Realizo esto mientras tenga elementos en Map
            Integer key_tipo = it.next(); // Recupero el tipo
            for (int i = 0; i < map.get(key_tipo); i++) { // Itero por sobre la cantidad pedida
                addAirstrip(key_tipo);
            }
        }
    }

    @Override
    public void execute() {
        // Simular todo
        while (!stopSimulation) {
            Event event = fel.getImminent();
            event.planificate(servers, fel);
        }
        // Guardar Analiticas de Replicación
        metricasAviones.add(new AircraftMetric());
        metricasPistas.add(new AirstripMetric(servers));
    }

    // Genera las estadisticas pedidas en el practico, se muestran solo una vez al
    // finalizar la simulación
    // Se ejecuta una cantidad de veces acorde a la cantidad de servidores
    public void generateAnalitics() {
        // Ordena los servidores por Id para una mejor lectura en el reporte
        servers.sort(new Comparator<Server>() {
            @Override
            public int compare(Server o1, Server o2) {
                int id1 = o1.getId();
                int id2 = o2.getId();
                int ret = 0;
                if (id1 < id2) {
                    ret = -1;
                } else if (id1 > id2) {
                    ret = 1;
                } else
                    ret = 0;
                return ret;
            }

        });

        // Genera Analiticas generales
        appendReport("==================ANALITICAS====================\n");
        int totalAvionesLigeros = Entity.getIdCountSpecific(1);
        int totalAvionesMedianos = Entity.getIdCountSpecific(2);
        int totalAvionesPesados = Entity.getIdCountSpecific(3);
        int totalMantenimientos = Entity.getIdCountSpecific(0);
        
        
        int totalaviones = totalAvionesLigeros + totalAvionesMedianos + totalAvionesPesados;

        float esperamedia = ((float) Entity.getTotalWaitingTime()) / totalaviones;
        float transitomedia = ((float) Entity.getTotalTransitTime()) / totalaviones;
        appendReport(("Cantidad Total de aviones que aterrizaron: " + totalaviones) + '\n');
        appendReport(("Cantidad de aviones ligeros que aterrizaron: " + totalAvionesLigeros) + '\n');
        appendReport(("Cantidad de aviones medianos que aterrizaron: " + totalAvionesMedianos) + '\n');
        appendReport(("Cantidad de aviones pesados que aterrizaron: " + totalAvionesPesados) + '\n');
        appendReport(("Cantidad de Mantenimientos hechos: " + totalMantenimientos + '\n'));
        
        appendReport(("Maximo tiempo de espera " + (Entity.getMaxWaitingTime())) + '\n');
        appendReport("Tiempo medio de espera " + esperamedia + '\n');
        appendReport("Tiempo medio de transito " + transitomedia + '\n');
        appendReport("Tiempo maximo de transito " + Entity.getMaxTransitTime() + '\n');
        appendReport(("Tiempo total de espera:") + Entity.getTotalWaitingTime() + '\n');
        appendReport(("Tiempo total de transito:") + Entity.getTotalTransitTime() + '\n');
        appendReport("Costo Final: " + costo + "\n");

        // Genera Analiticas de los servidores
        int it = 0;

        for (Server server : servers) {

            // Mostrar que tipo de servidor es
            if (server.getTipo() == 1 && it == 0) {
                appendReport("================Pistas para Aviones Liviano=================" +
                        '\n');
                appendReport("============================================================" +
                        '\n');
                it = 1;
            } else if (server.getTipo() == 2 && it == 1) {
                appendReport("================Pistas para Aviones Medianos================="
                        + '\n');
                appendReport("============================================================" +
                        '\n');
                it = 2;
            } else if (server.getTipo() == 3 && it == 2) {
                appendReport("================Pistas para Aviones Pesados=================" +
                        '\n');
                appendReport("============================================================" +
                        '\n');
                it = 3;
            }
            // Mostrar Analiticas por Server
            generateAnaliticsPerServer(server, false);
            //
        }
    }

    // Genera las Analiticas dado un Servidor
    public void generateAnaliticsPerServer(Server server, boolean writeHistory) {
        // Calculo de Analiticas
        float prop = ((float) server.getTotalIdleTime()) / endTime;
        float prop1 = ((float) server.getMaxIdleTime()) / server.getTotalIdleTime();

        // Mostrar Analiticas por Server
        appendReport("===================SERVER " + String.valueOf(server.getId()) + "====================\n"); // TEMPORAL

        appendReport(("Tiempo maximo de ocio de pista" + server.getId() + ":" + server.getMaxIdleTime())
                + '\n');
        appendReport("Vida de la pista " + server.getHp() + "\n");
        appendReport(("Maximo tamaño de cola " + server.getQueue().getMaxSize()) + '\n');
        appendReport(("Tiempo de ocio total "
                + server.getTotalIdleTime()) + '\n');
        appendReport("Tiempo de ocio total con respecto a la simulacion "
                + prop * 100 + "%" + '\n');
        appendReport("Tiempo de ocio maximo con respecto al tiempo de ocio total "
                + prop1 * 100 + "%" + '\n');

        if (writeHistory) {
            writeFile(server.getReport(), true, "server_" + server.getId() + ".txt");
        }
    }

    // GENERATE REPORT INTO FILE EXAMPLE
    @Override
    public String generateReport(boolean intoFile, String fileName) {
        generateAnalitics();
        return writeFile(this.report, intoFile, fileName);
    }

    @Override
    public String generateHistory(boolean intoFile, String fileName) {
        return writeFile(this.history, intoFile, fileName);
    }

    private String writeFile(String textToWrite, boolean intoFile, String fileName) {
        if (intoFile) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                writer.write(textToWrite);
                writer.close();
            } catch (Exception exception) {
                System.out.println("Error when trying to write the report into a file.");
                System.out.println("Showing on screen...");
                System.out.println(textToWrite);
            }
        }
        //System.out.println(textToWrite);
        System.out.println("File written in: " + fileName);
        return textToWrite;
    }

    public void appendReport(String report) {
        this.report = this.report.concat(report);
    }

    public void appendHistory(String report) {
        this.history = this.history.concat(report);
    }

    @Override
    public void reset() {
        costo = 0;
        report = "==============================================================================================\n"
                +
                "                                        R E P O R T                                           \n" +
                "==============================================================================================\n" +
                "\n" + dtf.format(now) + "\n";
        history = "==============================================================================================\n"
                +
                "                                        H Y S T O R Y                                           \n" +
                "==============================================================================================\n" +
                "\n" + dtf.format(now) + "\n";

        // Resetear c/variable Estatica
        Entity.reset();
        Aircraft.reset();
        Server.reset();

        initialize(tipo_cantidad, endTime, selectionPolicy);
    }

    @Override
    public void showReplicationAnalitics() {
        AircraftMetric metricaAcumuladaAviones = new AircraftMetric();
        AirstripMetric listaServers = new AirstripMetric();
        //List<AirstripMetricMini> listaServers = new ArrayList<>(); // Equivale a un AirstripMetric
        
        int iterationTimes = metricasAviones.size();
        // --Acumular todas las ejecuciones--
        // Acumular Aviones
        // Calcular media de medias
        for(AircraftMetric avion: metricasAviones){
            for(int i = 0; i < 4; i++){
                metricaAcumuladaAviones.setCantidadAviones(i, metricaAcumuladaAviones.getCantidadAviones(i) + avion.getCantidadAviones(i));
                metricaAcumuladaAviones.setTiempoMedioEspera(i, metricaAcumuladaAviones.getTiempoMedioEspera(i) + avion.getTiempoMedioEspera(i));
                metricaAcumuladaAviones.setTiempoMedioEsperaMax(i, metricaAcumuladaAviones.getTiempoMedioEsperaMax(i) + avion.getTiempoMedioEsperaMax(i));
                metricaAcumuladaAviones.setTiempoMedioTransito(i, metricaAcumuladaAviones.getTiempoMedioTransito(i) + avion.getTiempoMedioTransito(i));
                metricaAcumuladaAviones.setTiempoMedioTransitoMax(i, metricaAcumuladaAviones.getTiempoMedioTransitoMax(i) + avion.getTiempoMedioTransitoMax(i));
            }
        }
        for(int i = 0; i < 4; i++){
            metricaAcumuladaAviones.setCantidadAviones(i, metricaAcumuladaAviones.getCantidadAviones(i) / iterationTimes);
            metricaAcumuladaAviones.setTiempoMedioEspera(i, metricaAcumuladaAviones.getTiempoMedioEspera(i) / iterationTimes);
            metricaAcumuladaAviones.setTiempoMedioEsperaMax(i, metricaAcumuladaAviones.getTiempoMedioEsperaMax(i) / iterationTimes);
            metricaAcumuladaAviones.setTiempoMedioTransito(i, metricaAcumuladaAviones.getTiempoMedioTransito(i) / iterationTimes);
            metricaAcumuladaAviones.setTiempoMedioTransitoMax(i, metricaAcumuladaAviones.getTiempoMedioTransitoMax(i) / iterationTimes);
        }
        // Calcular Varianzas
            // ANALITICAS DE REPLICACIONES
            double[] varCantidadAviones = new double[4];
            double[] varTiempoMedioTransito = new double[4];
            double[] varTiempoMedioEspera = new double[4];
            double[] varTiempoMedioTransitoMax = new double[4];
            double[] varTiempoMedioEsperaMax = new double[4];

        for(AircraftMetric avion: metricasAviones){
            for(int i = 0; i < 4; i++){
                varCantidadAviones[i]       += avion.getCantidadAviones(i) - metricaAcumuladaAviones.getCantidadAviones(1);
                varTiempoMedioTransito[i]   += avion.getTiempoMedioTransito(i) - metricaAcumuladaAviones.getTiempoMedioTransito(1); 
                varTiempoMedioEspera[i]     += avion.getTiempoMedioEspera(i) - metricaAcumuladaAviones.getTiempoMedioEspera(1);
                varTiempoMedioTransitoMax[i]+= avion.getTiempoMedioTransitoMax(i) - metricaAcumuladaAviones.getTiempoMedioTransitoMax(1);
                varTiempoMedioEsperaMax[i]  += avion.getTiempoMedioEsperaMax(i) - metricaAcumuladaAviones.getTiempoMedioEsperaMax(1);
            }
        }

        for(AircraftMetric avion: metricasAviones){
            for(int i = 0; i < 4; i++){
                varCantidadAviones[i]       /= Math.sqrt(iterationTimes - 1) / 1.96;
                varTiempoMedioTransito[i]   /= Math.sqrt(iterationTimes - 1) / 1.96;
                varTiempoMedioEspera[i]     /= Math.sqrt(iterationTimes - 1) / 1.96;
                varTiempoMedioTransitoMax[i]/= Math.sqrt(iterationTimes - 1) / 1.96;
                varTiempoMedioEsperaMax[i]  /= Math.sqrt(iterationTimes - 1) / 1.96;
            }
        }

        // Acumular Pistas
        for(AirstripMetricMini pista : metricasPistas.get(0).getListaServers()){
            listaServers.getListaServers().add(new AirstripMetricMini());
        }

        for(AirstripMetric pistaPorReplica: metricasPistas){ //itera la lista de servers por ejecucion, devuelve la lista de servers en esa ejecucion
            // Ordenar las pistatPorReplica por id para evitar mezcla.
            pistaPorReplica.getListaServers().sort(new Comparator<AirstripMetricMini>() {
                @Override
                public int compare(AirstripMetricMini o1, AirstripMetricMini o2) {
                    int id1 = o1.getId();
                    int id2 = o2.getId();
                    int ret = 0;
                    if (id1 < id2) {
                        ret = -1;
                    } else if (id1 > id2) {
                        ret = 1;
                    } else
                        ret = 0;
                    return ret;
                }
            });
            int i = 0;
            for(AirstripMetricMini pista : pistaPorReplica.getListaServers()){
                listaServers.getListaServers().get(i).setId(pista.getId());
                listaServers.getListaServers().get(i).setTiempoOcioTotal(listaServers.getListaServers().get(i).getTiempoOcioTotal() + pista.getTiempoOcioTotal());
                listaServers.getListaServers().get(i).setTiempoOcioMaximo(listaServers.getListaServers().get(i).getTiempoOcioMaximo() + pista.getTiempoOcioMaximo());
                listaServers.getListaServers().get(i).setTamañoMaximoCola(listaServers.getListaServers().get(i).getTamañoMaximoCola() + pista.getTamañoMaximoCola());
                listaServers.getListaServers().get(i).setDurabilidadRestante(listaServers.getListaServers().get(i).getDurabilidadRestante() + pista.getDurabilidadRestante());
                //System.out.println(listaServers.get(i));
                i++;
            }
        }
        //System.out.println(metricasPistas);
        //System.out.println(listaServers);

        // --Mostrar Estadisticas--
        // Mostrar Aviones
        for(int i = 0; i < 4; i++){
            String tipoAvion = Entity.typeToString(i);
            int cantidadAviones = metricaAcumuladaAviones.getCantidadAviones(i);
            float tiempoMedioEspera = metricaAcumuladaAviones.getTiempoMedioEspera(i);
            float tiempoMedioEsperaMax = metricaAcumuladaAviones.getTiempoMedioEsperaMax(i);
            float tiempoMedioTransito = metricaAcumuladaAviones.getTiempoMedioTransito(i);
            float tiempoMedioTransitoMax = metricaAcumuladaAviones.getTiempoMedioTransitoMax(i);
            System.out.println("[" + tipoAvion + "]");
            //System.out.println("Cantidad de aviones [ " + (cantidadAviones  - varCantidadAviones[i])  + ":" + (cantidadAviones  + Math.sqrt(varCantidadAviones[i]))+ "]");
            System.out.println("Cantidad de aviones [ "+ cantidadAviones + "]");
            
            System.out.println(tiempoMedioEspera);
            System.out.println(tiempoMedioEsperaMax);
            System.out.println(tiempoMedioTransito);
            System.out.println(tiempoMedioTransitoMax);
        }
        // Mostrar Pistass
        for(AirstripMetricMini pista : listaServers.getListaServers()){
            System.out.println("Pista id: " + pista.getId());
            System.out.println(pista.getTiempoOcioTotal() / iterationTimes);
            System.out.println(pista.getTiempoOcioMaximo() / iterationTimes);
            System.out.println(pista.getTamañoMaximoCola() / iterationTimes);
            System.out.println(pista.getDurabilidadRestante() / iterationTimes);
        }
    }
}