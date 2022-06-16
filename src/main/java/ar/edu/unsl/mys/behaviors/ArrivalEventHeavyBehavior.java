package ar.edu.unsl.mys.behaviors;

import ar.edu.unsl.mys.events.Event;
import ar.edu.unsl.mys.policies.ServerSelectionPolicy;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.utils.Randomizer;
import ar.edu.unsl.mys.events.ArrivalEventHeavy;
import ar.edu.unsl.mys.utils.CustomRandomizer;

public class ArrivalEventHeavyBehavior extends EventBehavior
{
    private static ArrivalEventHeavyBehavior ArrivalEventHeavyBehavior;

    private ArrivalEventHeavyBehavior(Randomizer randomizer)
    {
        super(randomizer);
    }

    public static ArrivalEventHeavyBehavior getInstance()
    {
        if (ArrivalEventHeavyBehavior == null){
            return new ArrivalEventHeavyBehavior(CustomRandomizer.getInstance());
        }
        else return ArrivalEventHeavyBehavior;
    }


    @Override
    public Event nextEvent(Event actualEvent, Entity entity) {
        
        ArrivalEventHeavy evento_Arribo; // Construyo un objeto de tipo evento de arribo
        float tiempo;
        float m = actualEvent.getClock()%720;
        int mean = 60;
        int stddev = 2;

        //Hora pico
        if(420<=m && m<=600){
            mean/=2;
        }
        
        tiempo = getRandomizer().nextGaussiano(mean, stddev); 

        ServerSelectionPolicy policy = actualEvent.getEntity().getAttendingServer().getMainAirport().getSelectionPolicy();
        evento_Arribo = new ArrivalEventHeavy(actualEvent.getClock() + tiempo , policy);
        return evento_Arribo;   // Retornar el evento
        //return new ArrivalEvent(actualEvent.getClock() + 10 , OneServerModelPolicy.getInstance()); //TEST
    }
}