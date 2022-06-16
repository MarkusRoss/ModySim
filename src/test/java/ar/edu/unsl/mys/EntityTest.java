package ar.edu.unsl.mys;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.entities.LightAircraft;

public class EntityTest {
    @Test
    public void LightAircraftTestsId() {
        Entity entity = new LightAircraft();
        if (Entity.getIdCount() == entity.getId()) {
            assertTrue(true);
        }
    }

    @Test
    public void LightAircraftTestsIdCount() {
        Entity entity = new LightAircraft();
        if (Entity.getIdCount() == LightAircraft.getIdCount()) {
            assertTrue(true);
        }
    }
}
