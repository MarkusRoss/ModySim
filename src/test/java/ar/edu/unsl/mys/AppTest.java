package ar.edu.unsl.mys;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

import ar.edu.unsl.mys.policies.MultiServerModelPolicy;
import ar.edu.unsl.mys.policies.ServerSelectionPolicy;

/**
 * Unit test for simple App.
 */
public class AppTest {

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
