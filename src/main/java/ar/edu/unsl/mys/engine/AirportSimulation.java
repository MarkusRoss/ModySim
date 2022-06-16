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
import ar.edu.unsl.mys.entities.HeavyAircraft;
import ar.edu.unsl.mys.entities.LightAircraft;
import ar.edu.unsl.mys.entities.Mantenimiento;
import ar.edu.unsl.mys.entities.MediumAircraft;
import ar.edu.unsl.mys.events.ArrivalEventHeavy;
import ar.edu.unsl.mys.events.ArrivalEventLight;
import ar.edu.unsl.mys.events.ArrivalEventMed;
import ar.edu.unsl.mys.events.ArrivalMaintenanceEvent;
import ar.edu.unsl.mys.events.Event;
import ar.edu.unsl.mys.resources.CustomQueue;
import ar.edu.unsl.mys.events.StopExecutionEvent;
import ar.edu.unsl.mys.policies.RepairServerModelPolicy;
import ar.edu.unsl.mys.policies.ServerSelectionPolicy;

/**
 * Event oriented simulation of an airport
 */
public class AirportSimulation implements Engine {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static int costo;
    //Pruebapruebiosa

    private int totalaviones;
    private float transitomedia;
    private float esperamedia;
    //estaticas de entidades

    private int[] EntityIdCount = new int[4];
    public int[] getEntityIdCount() {
        return EntityIdCount;
    }

    public float[] getEntityAvgWaitingTime() {
        return EntityAvgWaitingTime;
    }

    public float[] getEntityMaxWaitingTime() {
        return EntityMaxWaitingTime;
    }

    public float[] getEntityAvgTransitTime() {
        return EntityAvgTransitTime;
    }

    public float[] getEntityMaxTransitTime() {
        return EntityMaxTransitTime;
    }

    private float[] EntityAvgWaitingTime= new float[4];
    private float[] EntityMaxWaitingTime= new float[4];
    private float[] EntityAvgTransitTime= new float[4];


    private float[] EntityMaxTransitTime= new float[4];


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
    


    private List<Server> livservers;
    private List<Server> medservers;
    private List<Server> heavservers;

    private ServerSelectionPolicy selectionPolicy;
    private HashMap<Integer, Integer> tipo_cantidad;

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

        // Crear la lista de pistas

        createAirstrips(tipo_cantidad);

        // Cargar la fel con el evento de fin y un evento de inicio para la simulacion

        StopExecutionEvent evento_fin = new StopExecutionEvent(endTime, this);
        ArrivalEventLight evento_ArriboLight = new ArrivalEventLight(0, policy);
        ArrivalEventMed evento_ArriboMedium = new ArrivalEventMed(0, policy);
        ArrivalEventHeavy evento_ArriboHeavy = new ArrivalEventHeavy(0, policy);
        ArrivalMaintenanceEvent evento_ArriboMaintenance = new ArrivalMaintenanceEvent(1440,
                RepairServerModelPolicy.getInstance());

        fel.insert(evento_fin);
        fel.insert(evento_ArriboMaintenance);
        fel.insert(evento_ArriboLight);
        fel.insert(evento_ArriboMedium);
        fel.insert(evento_ArriboHeavy);
    }

    public static int getEndTime() {
        return endTime;
    }
    public List<Server> getServers() {
        return servers;
    }
    public List<Server> getLivservers() {
        return livservers;
    }

    public List<Server> getMedservers() {
        return medservers;
    }

    public List<Server> getHeavservers() {
        return heavservers;
    }
    public int getTotalaviones() {
        return totalaviones;
    }

    public float getTransitomedia() {
        return transitomedia;
    }

    public float getEsperamedia() {
        return esperamedia;
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
    }

    public void createAirstrips(HashMap<Integer, Integer> map) { // Creacion de pistas
        java.util.Iterator<Integer> it = map.keySet().iterator(); // Iterador para el Map(tipo'key', cantidad'value')
        List<Server> temp = new ArrayList<>();
        while (it.hasNext()) { // Realizo esto mientras tenga elementos en Map
            temp = new ArrayList<>();
            Integer key_tipo = it.next(); // Recupero el tipo
            for (int i = 0; i < map.get(key_tipo); i++) { // Itero por sobre la cantidad pedida
                
                CustomQueue cola_espera = new CustomQueue();
                Airstrip pista = new Airstrip(cola_espera, key_tipo);
                cola_espera.setAssignedServer(pista);
                pista.setMainAirport(this);
                servers.add(pista);
                temp.add(pista);
                costo += 1000 * key_tipo; // Por cada pista creada, vamos aumentando el costo
                // Pista Ligera: 1000; Pista Mediana: 2000; Pista Pesada: 3000;
            }
            switch(key_tipo){ //Discrimino los tipos de servidores.
                case 1: livservers = temp;
                case 2: medservers = temp;
                case 3: medservers = temp;
            }
        }
    }

    @Override
    public void execute() {
        while (!stopSimulation) {
            Event event = fel.getImminent();
            event.planificate(servers, fel);
        }
        perdonenme();
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
        totalaviones = HeavyAircraft.getIdCount() + MediumAircraft.getIdCount()
                + LightAircraft.getIdCount() - 3;// (Entity.getIdCount() - 1);

        esperamedia = ((float) Entity.getTotalWaitingTime()) / totalaviones;
        transitomedia = ((float) Entity.getTotalTransitTime()) / totalaviones;
        appendReport(("Cantidad de aviones que aterrizaron: " + totalaviones) + '\n');
        appendReport(("Maximo tiempo de espera " + (Entity.getMaxWaitingTime())) + '\n');
        appendReport("Tiempo medio de espera " + esperamedia + '\n');
        appendReport("Tiempo medio de transito " + transitomedia + '\n');
        appendReport("Tiempo maximo de transito " + Entity.getMaxTransitTime() + '\n');
        appendReport(("Tiempo total de espera:") + Entity.getTotalWaitingTime() + '\n');
        appendReport(("Tiempo total de transito:") + Entity.getTotalTransitTime() + '\n');
        appendReport("Costo Final: " + costo + "\n");

        // AAAAAA

        
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
        server.OcioDivSim = ((float) server.getTotalIdleTime()) / endTime;
        server.MaxOcioDivOcio = ((float) server.getMaxIdleTime()) / server.getTotalIdleTime();

        // Mostrar Analiticas por Server
        appendReport("===================SERVER " + String.valueOf(server.getId()) + "====================\n"); // TEMPORAL

        appendReport(("Tiempo maximo de ocio de pista" + server.getId() + ":" + server.getMaxIdleTime())
                + '\n');
        appendReport("Vida de la pista " + server.getHp() + "\n");
        appendReport(("Maximo tamaño de cola " + server.getQueue().getMaxSize()) + '\n');
        appendReport(("Tiempo de ocio total "
                + server.getTotalIdleTime()) + '\n');
        appendReport("Tiempo de ocio total con respecto a la simulacion "
                + server.OcioDivSim * 100 + "%" + '\n');
        appendReport("Tiempo de ocio maximo con respecto al tiempo de ocio total "
                + server.MaxOcioDivOcio * 100 + "%" + '\n');

        if (writeHistory) {
            writeFile(server.getReport(), true, "server_" + server.getId() + ".txt");
        }
    }
    public void perdonenme(){
        
        EntityIdCount[0] = Entity.getIdCount();
        EntityMaxTransitTime[0] = Entity.getMaxTransitTime();
        EntityMaxWaitingTime[0] = Entity.getMaxWaitingTime();
        EntityAvgTransitTime[0] = Entity.getTotalTransitTime();
        EntityAvgWaitingTime[0] = Entity.getTotalWaitingTime();
        EntityIdCount[1] = LightAircraft.getIdCount();
        EntityMaxTransitTime[1] = LightAircraft.getMaxTransitTime();
        EntityMaxWaitingTime[1] = LightAircraft.getMaxWaitingTime();
        EntityAvgTransitTime[1] = LightAircraft.getTotalTransitTime()/LightAircraft.getIdCount();
        EntityAvgWaitingTime[1] = LightAircraft.getTotalWaitingTime()/LightAircraft.getIdCount();
        EntityIdCount[2] = MediumAircraft.getIdCount();
        EntityMaxTransitTime[2] = MediumAircraft.getMaxTransitTime();
        EntityMaxWaitingTime[2] = MediumAircraft.getMaxWaitingTime();
        EntityAvgTransitTime[2] = MediumAircraft.getTotalTransitTime()/MediumAircraft.getIdCount();
        EntityAvgWaitingTime[2] = MediumAircraft.getTotalWaitingTime()/MediumAircraft.getIdCount();
        EntityIdCount[3] = HeavyAircraft.getIdCount();
        EntityMaxTransitTime[3] = HeavyAircraft.getMaxTransitTime();
        EntityMaxWaitingTime[3] = HeavyAircraft.getMaxWaitingTime();
        EntityAvgTransitTime[3] = HeavyAircraft.getTotalTransitTime()/HeavyAircraft.getIdCount();
        EntityAvgWaitingTime[3] = HeavyAircraft.getTotalWaitingTime()/HeavyAircraft.getIdCount();
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
        // TODO Auto-generated method stub
        // ¿Como resetear el Aeropuerto?
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
        LightAircraft.reset();
        MediumAircraft.reset();
        HeavyAircraft.reset();
        Mantenimiento.reset();
        Server.reset();

        initialize(tipo_cantidad, endTime, selectionPolicy); // Error: Se crean nuevos servidores y se mantienen los
                                                             // anteriores

    }
}
