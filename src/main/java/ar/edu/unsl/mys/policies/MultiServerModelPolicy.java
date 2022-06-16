package ar.edu.unsl.mys.policies;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ar.edu.unsl.mys.resources.Server;

public class MultiServerModelPolicy implements ServerSelectionPolicy {
    private static MultiServerModelPolicy policy;
    private Comparator<Server> ServerComp;

    private MultiServerModelPolicy() {
        ServerComp = new Comparator<Server>() {
            @Override
            public int compare(Server o1, Server o2) {
                int qsize1 = o1.getQueue().getQueue().size();
                int qsize2 = o2.getQueue().getQueue().size();
                int ret;
                if (qsize1 < qsize2){
                    ret = -1;
                } else if (qsize1 > qsize2){
                    ret = 1;
                } else ret = 0;
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
        servers.sort(ServerComp);
        List<Server> enabledServers = enabledServer(servers); // Ya va a estar ordenada
        if (!enabledServers.isEmpty()){
            for(Server server : enabledServers){
                if (server.getTipo() == type) {
                    return server;
                }
            }
            return enabledServers.get(0);
        }
        //return servers.get(0);
        //int index = (int)Math.floor(CustomRandomizer.getInstance().nextUniforme(0, 3));
        return servers.get(0);
    }

    public List<Server> enabledServer(List<Server> servers) { // Lista Auxiliar Filtrada que contiene los servidores habilitados
        List<Server> enabled = new ArrayList<>();
        for (Server server : servers) {
            if (server.isEnable())
                enabled.add(server);
        }
        return enabled;
    }
}