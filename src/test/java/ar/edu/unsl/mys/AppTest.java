package ar.edu.unsl.mys;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

import ar.edu.unsl.mys.behaviors.ArrivalEventLightBehavior;
import ar.edu.unsl.mys.engine.AirportSimulation;
import ar.edu.unsl.mys.engine.Engine;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.entities.LightAircraft;
import ar.edu.unsl.mys.events.ArrivalEvent;
import ar.edu.unsl.mys.policies.MultiServerModelPolicy;
import ar.edu.unsl.mys.policies.ServerSelectionPolicy;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private static final int MIN_PER_DAY = 1440;
    private static final int NUMBER_OF_DAYS = 28;
    private static final int EXECUTION_TIME = MIN_PER_DAY * NUMBER_OF_DAYS;
    private static final int SIMULATION_TIMES = 10;

    private static HashMap<Integer, Integer> SERVERS_NUMBER;

    /**
     * Rigorous Test :-)
     */
    @Test
    public void AppTest1() {

        SERVERS_NUMBER = new HashMap<>();
        SERVERS_NUMBER.put(1, 1);
        SERVERS_NUMBER.put(2, 2);
        SERVERS_NUMBER.put(3, 1);
        ServerSelectionPolicy policy = MultiServerModelPolicy.getInstance();
        // Engine engine = new AirportSimulation(SERVERS_NUMBER, EXECUTION_TIME, );

        if (policy != null) {
            assertTrue(true);
        }
    }
}
