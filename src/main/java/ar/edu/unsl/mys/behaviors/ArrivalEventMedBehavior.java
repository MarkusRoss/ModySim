package ar.edu.unsl.mys.behaviors;

import ar.edu.unsl.mys.events.Event;
import ar.edu.unsl.mys.policies.ServerSelectionPolicy;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.utils.Randomizer;
import ar.edu.unsl.mys.events.ArrivalEventMed;
import ar.edu.unsl.mys.utils.CustomRandomizer;

public class ArrivalEventMedBehavior extends EventBehavior
{
    private static ArrivalEventMedBehavior arrivalEventMedBehavior;

    private ArrivalEventMedBehavior(Randomizer randomizer)
    {
        super(randomizer);
    }

    public static ArrivalEventMedBehavior getInstance()
    {
        if (arrivalEventMedBehavior == null){
            return new ArrivalEventMedBehavior(CustomRandomizer.getInstance());
        }
        else return arrivalEventMedBehavior;
    }


    @Override
    public Event nextEvent(Event actualEvent, Entity entity) { 
        
        ArrivalEventMed evento_Arribo; // Construyo un objeto de tipo evento de arribo
        
        float tiempo;
        float m = actualEvent.getClock()%720;
        int mu = 30;
        //Hora pico
        if(420<=m && m<=600){
            mu /= 2;
        }
        tiempo = getRandomizer().nextExponencial(mu);

        
        ServerSelectionPolicy policy = actualEvent.getEntity().getAttendingServer().getMainAirport().getSelectionPolicy();
        evento_Arribo = new ArrivalEventMed(actualEvent.getClock() + tiempo , policy);
        return evento_Arribo;   // Retornar el evento
        //return new ArrivalEvent(actualEvent.getClock() + 10 , OneServerModelPolicy.getInstance()); //TEST
    }
}