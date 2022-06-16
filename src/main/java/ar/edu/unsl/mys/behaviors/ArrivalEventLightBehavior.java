package ar.edu.unsl.mys.behaviors;

import ar.edu.unsl.mys.events.Event;
import ar.edu.unsl.mys.policies.ServerSelectionPolicy;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.utils.Randomizer;
import ar.edu.unsl.mys.events.ArrivalEvent;
import ar.edu.unsl.mys.events.ArrivalEventLight;
import ar.edu.unsl.mys.utils.CustomRandomizer;

public class ArrivalEventLightBehavior extends EventBehavior
{
    private static ArrivalEventLightBehavior arrivalEventBehavior;

    private ArrivalEventLightBehavior(Randomizer randomizer)
    {
        super(randomizer);
    }

    public static ArrivalEventLightBehavior getInstance()
    {
        if (arrivalEventBehavior == null){
            return new ArrivalEventLightBehavior(CustomRandomizer.getInstance());
        }
        else return arrivalEventBehavior;
    }


    @Override
    public Event nextEvent(Event actualEvent, Entity entity) {
        
        ArrivalEventLight evento_Arribo; // Construyo un objeto de tipo evento de arribo
        
        float tiempo;
        float m = actualEvent.getClock()%720;
        int mu = 40;
        //Hora pico
        if(420<=m && m<=600){
            mu /= 2;
        }
        tiempo = getRandomizer().nextExponencial(mu);



        ServerSelectionPolicy policy = actualEvent.getEntity().getAttendingServer().getMainAirport().getSelectionPolicy();
        evento_Arribo = new ArrivalEventLight(actualEvent.getClock() + tiempo , policy);
        return evento_Arribo;   // Retornar el evento
        //return new ArrivalEvent(actualEvent.getClock() + 10 , OneServerModelPolicy.getInstance()); //TEST
    }
}