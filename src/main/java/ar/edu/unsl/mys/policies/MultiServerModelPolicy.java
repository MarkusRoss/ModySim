package ar.edu.unsl.mys.policies;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import ar.edu.unsl.mys.resources.Server;

public class MultiServerModelPolicy implements ServerSelectionPolicy {
    private static MultiServerModelPolicy policy;
    private Comparator<Server> ServerComp;

    private MultiServerModelPolicy() {
        ServerComp = new Comparator<Server>() {
            @Override
            public int compare(Server o1, Server o2) {

                /*int tipo1 = o1.getTipo();
                int tipo2 = o2.getTipo();
                boolean busy1 = o1.isBusy();
                boolean busy2 = o2.isBusy();*/
                int qsize1 = o1.getQueue().getQueue().size();
                int qsize2 = o2.getQueue().getQueue().size();
                int id1 = o1.getId();
                int id2 = o2.getId();
                int ret = 0;
                /*if (tipo1 < tipo2) {
                    ret = -1;
                } else if (tipo1 > tipo2) {
                    ret = 1;
                } else if (Boolean.compare(busy1, busy2) < 0) {
                    ret = -1;
                } else if (Boolean.compare(busy1, busy2) > 0) {
                    ret = 1;
                } else */if (qsize1 < qsize2) {
                    ret = -1;
                } else if (qsize1 > qsize2) {
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
    }

    public static MultiServerModelPolicy getInstance() {
        if (MultiServerModelPolicy.policy == null)
            MultiServerModelPolicy.policy = new MultiServerModelPolicy();

        return MultiServerModelPolicy.policy;
    }

    @Override
    public Server selectServer(List<Server> servers, int type) // Selecciona de entre los servidores habilitados
    {
        int i = 0; // Posicion normal (Caso normal)
        int minqueue = 1000; // Control de tamaño de cola (Caso excepcional)
        int alt = 0; // Posicion alternativa (Caso excepcional)

        try {
            servers.sort(ServerComp);
            List<Server> enabledServers = enabledServer(servers);

            int size = enabledServers.size();

            // El control por vacio va primero para evitar problemas

            if (enabledServers.isEmpty()) {
                System.out.println("ERROR NO HAY PISTAS DISPONIBLES. CERRANDO AEROPUERTO");
                servers.get(0).getMainAirport().generateReport(true, "Reporte.txt");
                System.exit(0);
                return null;
            }

            for (Server server : enabledServers) { // Esto para que pueda recorrer toda la lista
                                                   // (con el while no reconocia el ultimo elemento)

                if (enabledServers.get(i).getTipo() == type) {
                    return server;

                } else {

                    if (server.getQueue().getMaxSize() < minqueue) {
                        minqueue = server.getQueue().getMaxSize();
                        alt = i;
                    }

                    if (i < size - 1) // Esto para que incremente i mientras no supere el limite de la lista
                        i++;
                }
            }

            if (enabledServers.get(i).getTipo() != type) { // Si no encontro de su tipo entonces retorna en la posicion
                                                           // alternativa (Pista con menor cola)
                return enabledServers.get(alt);
            }
            return servers.get(0);
        } catch (NoSuchElementException e) {
            System.out.println("NO HAY PISTAS DISPONIBLES. CERRANDO AEROPUERTO");
            servers.get(0).getMainAirport().generateReport(true, "Reporte.txt");
            System.exit(0);
            return null;
        }
    }

    public List<Server> enabledServer(List<Server> servers) { // Lista Auxiliar que contiene los servidores habilitados

        List<Server> enabled = new ArrayList<>();
        for (Server server : servers) {
            if (server.isEnable())
                enabled.add(server);

        }
        return enabled;
    }
}