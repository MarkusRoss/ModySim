package ar.edu.unsl.mys.metrics;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unsl.mys.resources.Server;

public class AirstripMetric {
    // ANALITICAS DE REPLICACIONES
    List<AirstripMetricMini> listaServers = new ArrayList<>(); // 4 metricas por server

    public AirstripMetric() {
    }

    public AirstripMetric(List<Server> servers) {
        for(Server server: servers){
            AirstripMetricMini metricaPorServidor = new AirstripMetricMini();
            metricaPorServidor.setId(server.getId());
            metricaPorServidor.setTiempoOcioTotal(server.getTotalIdleTime());
            metricaPorServidor.setTiempoOcioMaximo(server.getMaxIdleTime());
            metricaPorServidor.setTamañoMaximoCola(server.getQueue().getMaxSize());
            metricaPorServidor.setDurabilidadRestante(server.getHp());
            listaServers.add(metricaPorServidor);
        }
        //System.out.println(listaServers);
    }

    public List<AirstripMetricMini> getListaServers() {
        return listaServers;
    }

    @Override
    public String toString() {
        return "AirstripMetric [listaServers=" + listaServers + "]";
    }
    
    

    // hay n server por cada ejecución
    // hay m iteraciones, o sea: m*n*4 analiticas por almacenar


}
