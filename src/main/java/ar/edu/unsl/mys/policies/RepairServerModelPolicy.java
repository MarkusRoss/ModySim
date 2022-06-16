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
                /*
                 * boolean busy1 = o1.isBusy();
                 * boolean busy2 = o2.isBusy();
                 * int qsize1 = o1.getQueue().getQueue().size();
                 * int qsize2 = o2.getQueue().getQueue().size();
                 * int id1 = o1.getId();
                 * int id2 = o2.getId();
                 */
                int ret;
                if (damage1 < damage2) {
                    ret = -1;
                } else if (damage1 > damage2) {
                    ret = 1;
                } else
                    ret = 0;
                /*
                 * else if (Boolean.compare(busy1, busy2) < 0) {
                 * ret = -1;
                 * } else if (Boolean.compare(busy1, busy2) > 0) {
                 * ret = 1;
                 * } else if (qsize1 < qsize2) {
                 * ret = -1;
                 * } else if (qsize1 > qsize2) {
                 * ret = 1;
                 * } else if (id1 < id2) {
                 * ret = -1;
                 * } else if (id1 > id2) {
                 * ret = 1;
                 * } else
                 * ret = 0;
                 */
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
        List<Server> tempList = MultiServerModelPolicy.getInstance().enabledServer(servers); // Usamos la función del
                                                                                             // MultiServerPolicy que da
                                                                                             // la lista de servidores
                                                                                             // activos
        tempList.sort(Comp);
        return tempList.get(0); // Elige la primera pista (La mas dañada)
    }

    public List<Server> serversTemps(List<Server> servers, int i) {
        List<Server> tempList = MultiServerModelPolicy.getInstance().enabledServer(servers); // Usamos la función del
                                                                                             // MultiServerPolicy que da
                                                                                             // la lista de servidores
                                                                                             // activos
        tempList.sort(Comp);
        return tempList; // Elige la primera pista (La mas dañada)
    }
}
