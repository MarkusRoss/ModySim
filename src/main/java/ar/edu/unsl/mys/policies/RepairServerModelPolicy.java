package ar.edu.unsl.mys.policies;

import java.util.List;
import java.util.Comparator;

import ar.edu.unsl.mys.resources.Server;

public class RepairServerModelPolicy implements ServerSelectionPolicy {

    private static RepairServerModelPolicy policy;
    private Comparator<Server> Comp;

    private RepairServerModelPolicy() {
        Comp = new Comparator<Server>() {
            @Override
            public int compare(Server o1, Server o2) { // Compara primero por cantidad de HP, y luego por lo demas si
                                                       // son similares en HP
                float hp1 = o1.getHp();
                float hp2 = o2.getHp();

                float damage1 = (float) hp1 / (float) o1.getHpAsignado(); // Proporcion de Daño = Daño recibido / Daño
                                                                          // que puede soportar
                float damage2 = (float) hp2 / (float) o2.getHpAsignado(); // El tipo es 'float', pero puede ser
                                                                          // modificado si existe mejor alternativa
                int ret;
                if (damage1 < damage2) {
                    ret = -1;
                } else if (damage1 > damage2) {
                    ret = 1;
                } else
                    ret = 0;
                return ret;
            }
        };
    }

    public static RepairServerModelPolicy getInstance() {
        if (RepairServerModelPolicy.policy == null)
            RepairServerModelPolicy.policy = new RepairServerModelPolicy();

        return RepairServerModelPolicy.policy;
    }

    @Override
    public Server selectServer(List<Server> servers, int i) {
        // Usamos la función del MultiServerPolicy que da la lista de servidores activos
        servers.sort(Comp);
        List<Server> tempList = MultiServerModelPolicy.getInstance().enabledServer(servers);
        
        if (!tempList.isEmpty())
            return tempList.get(0); // Elige la primera pista (La mas dañada)
        
        return servers.get(0);
    }
}
