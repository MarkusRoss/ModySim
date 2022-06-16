//package ar.edu.unsl.mys;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import ar.edu.unsl.mys.engine.AirportSimulation;
import ar.edu.unsl.mys.engine.Engine;
import ar.edu.unsl.mys.policies.MultiServerModelPolicy;
import ar.edu.unsl.mys.resources.Server;
import ar.edu.unsl.mys.utils.CustomRandomizer;
import ar.edu.unsl.mys.utils.Randomizer;

public class App {
    private static final int MIN_PER_DAY = 1440;
    private static final int NUMBER_OF_DAYS = 28;
    private static final int EXECUTION_TIME = MIN_PER_DAY * NUMBER_OF_DAYS;
    private static final int SIMULATION_TIMES = 10;

    private static HashMap<Integer, Integer> SERVERS_NUMBER;

    public static void main(String[] args) {
        Comparator<Server> ServerComp = new Comparator<Server>() {
            @Override
            public int compare(Server o1, Server o2) {

                int tipo1 = o1.getTipo();
                int tipo2 = o2.getTipo();
                int id1 = o1.getId();
                int id2 = o2.getId();
                int ret = 0;
                if (tipo1 < tipo2) {
                    ret = -1;
                } else if (tipo1 > tipo2) {
                    ret = 1;
                } else if (id1 < id2) {
                    ret = -1;
                } else if (id1 > id2) {
                    ret = 1;
                } else
                    ret = 0;
                return ret;
            }

        };
    
       CustomRandomizer random = CustomRandomizer.getInstance( );
       /* 
       for(int x = 0;x<10000;x++){
        int a = random.nextExponencial(40);
        System.out.print(a+",");
       }
*/
        System.out.print("------------------------------\n");
        System.out.print("--------- EJECUCION ----------\n");
        System.out.print("------------------------------\n");
        // Agregamos una variable costo para poder saber como afectan los cambios
        // El costo va variando acorde a la creación de pistas y al mantenimiento.
        // Pista Ligera: 1000; Pista Mediana: 2000; Pista Pesada: 3000; Mantenimiento:
        // 500;
        
        //Datos 
        float[] aeronaves= {0,0,0,0};
        float[] maxtransito= {0,0,0,0};
        float[] maxespera= {0,0,0,0};
        float[] mediatransito={0,0,0,0};
        float[] mediaespera={0,0,0,0};
        //Numero de servidores
        SERVERS_NUMBER = new HashMap<>();
        SERVERS_NUMBER.put(1, 1); 
        SERVERS_NUMBER.put(2, 2);
        SERVERS_NUMBER.put(3, 1);
        // La mejor configuracion es: 1 pista ligera, 2 pistas medianas, y 1 pista
        // pesada.
        
        Engine engine = new AirportSimulation(SERVERS_NUMBER, EXECUTION_TIME, MultiServerModelPolicy.getInstance());
        engine.execute();
        List<Server> serverStats = new ArrayList<>();
        AirportSimulation temp = (AirportSimulation)engine;
        serverStats = temp.getServers();
        serverStats.sort(ServerComp);
        engine.generateReport(true, "Reporte_" + 0 + ".txt");
        engine.generateHistory(true, "Historia_" + 0 + ".txt");
        engine.reset(); 
        
        for (int i = 1; i < SIMULATION_TIMES; i++) {
            engine.execute();
            engine.generateHistory(true, "Historia_" + i + ".txt");
            engine.generateReport(true, "Reporte_" + i + ".txt");
            //datos[i]=(AirportSimulation)engine;
            temp = (AirportSimulation)engine;
            temp.getServers().sort(ServerComp);
            for(int c=0;c<temp.getServers().size();c++)
            {
                    serverStats.get(c).setTotalIdleTime(serverStats.get(c).getTotalIdleTime()+temp.getServers().get(c).getTotalIdleTime());
                    serverStats.get(c).setHp(serverStats.get(c).getHp()+temp.getServers().get(c).getHp());
                    serverStats.get(c).setMaxIdleTime(serverStats.get(c).getMaxIdleTime()+temp.getServers().get(c).getMaxIdleTime());
                    System.out.println("Tiempo maximo de espera en cola: "+ temp.getServers().get(c).getMaxTimeInQueue());
                    serverStats.get(c).setMaxTimeInQueue(serverStats.get(c).getMaxTimeInQueue()+temp.getServers().get(c).getMaxTimeInQueue());
            }
            for(int j =0;j<4;j++){
                System.out.println("Tiempo de espera maximo: "+ temp.getEntityMaxWaitingTime()[j]);
            maxtransito[j]+=temp.getEntityMaxTransitTime()[j];
            aeronaves[j]+=temp.getEntityIdCount()[j];
            maxespera[j]+=temp.getEntityMaxWaitingTime()[j];
            mediatransito[j]+=temp.getEntityAvgTransitTime()[j];
            mediaespera[j]+=temp.getEntityAvgWaitingTime()[j];
            }
            
            engine.reset(); 
        }
            
        for (int i = 0; i < 4; i++) {
            System.out.println("aeronaves " + (float)aeronaves[i]/SIMULATION_TIMES);
            System.out.println("maxtransito "+ (float)maxtransito[i]/SIMULATION_TIMES);
            System.out.println("maxespera " + (float)maxespera[i]/SIMULATION_TIMES);
            System.out.println("mediatransito " + (float)mediatransito[i]/SIMULATION_TIMES);
            System.out.println("mediaespera " + (float)mediaespera[i]/SIMULATION_TIMES);
            System.out.println("--------------------------------------");
        }
        for(Server a: serverStats){
            System.out.println("Server "+ a.getId() +" Tipo: "+a.getTipo());
            System.out.println("Total idle time:" +(float)a.getTotalIdleTime()/SIMULATION_TIMES);
            System.out.println("Total hp: "+(float)a.getHp()/SIMULATION_TIMES);
            System.out.println("Total MaxIdle time: "+(float)a.getMaxIdleTime()/SIMULATION_TIMES);
            System.out.println("Max time in queue: "+(float)a.getMaxTimeInQueue()/SIMULATION_TIMES);
            System.out.println("----------------------------------------");
        }


    }
}