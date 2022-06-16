package ar.edu.unsl.mys.engine;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Comparator;

import ar.edu.unsl.mys.events.Event;

public class FutureEventList {
    private List<Event> felImpl;
    private Comparator<Event> comparator;

    public FutureEventList() {
        felImpl = new ArrayList<Event>();
        comparator = new Comparator<Event>() {

            @Override
            public int compare(Event o1, Event o2) {
                float clock1 = o1.getClock();
                float clock2 = o2.getClock();
                int priority1 = o1.getPriority();
                int priority2 = o2.getPriority();
                int ret = 0;

                if (clock1 < clock2) {
                    ret = -1;
                } else if (clock1 > clock2) {
                    ret = 1;
                } else if (priority1 < priority2) {
                    ret = -1;
                } else if (priority1 > priority2) {
                    ret = 1;
                } else
                    ret = 0;
                return ret;
            }

        };
    }

    public Event getImminent() {
        return felImpl.remove(0);
    }

    public void insert(Event event) {
        felImpl.add(event);
        felImpl.sort(comparator);
    }

    @Override
    public String toString() {
        String ret = "============================================================== F E L ==============================================================\n";

        Iterator<Event> it = this.felImpl.iterator();

        while (it.hasNext()) {
            ret += it.next().toString() + "\n";
        }
        ret += "***********************************************************************************************************************************\n\n";

        return ret;
    }

    public String test() { // PRUEBA DE ORDENAMIENTO

        String ret = "============================================================== F E L ==============================================================\n";

        for (Event event : felImpl) {

            ret += "Clock: " + event.getClock() + "\nPrioridad: " + event.getPriority() + "\nNota: "
                    + event.getEntity() + "\n";
        }

        ret += "***********************************************************************************************************************************\n\n";

        return ret;
    }
}