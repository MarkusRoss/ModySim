package ar.edu.unsl.mys.behaviors;

import ar.edu.unsl.mys.events.Event;
import ar.edu.unsl.mys.policies.ServerSelectionPolicy;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.utils.Randomizer;
import ar.edu.unsl.mys.events.ArrivalEvent;
import ar.edu.unsl.mys.utils.CustomRandomizer;

public class ArrivalEventBehavior extends EventBehavior
{
    private static ArrivalEventBehavior arrivalEventBehavior;

    private ArrivalEventBehavior(Randomizer randomizer)
    {
        super(randomizer);
    }

    public static ArrivalEventBehavior getInstance()
    {
        if (arrivalEventBehavior == null){
            return new ArrivalEventBehavior(CustomRandomizer.getInstance());
        }
        else return arrivalEventBehavior;
    }


    @Override
    public Event nextEvent(Event actualEvent, Entity entity) {
        
        ArrivalEvent evento_Arribo; // Construyo un objeto de tipo evento de arribo
        float tiempo;
        float m = (actualEvent.getClock() % 1440) % 720;;
        int mu;
        int tipoEntidad = entity.getType();
        ServerSelectionPolicy policy = ((ArrivalEvent) actualEvent).getSelectionPolicy();//actualEvent.getEntity().getAttendingServer().getMainAirport().getSelectionPolicy();
        
        if (entity.getId() == 789){
            //System.out.println(tipoEntidad);
        }
        
        switch(tipoEntidad){
            // Mantenimiento
            case 0:
                tiempo = getRandomizer().nextGaussiano(5, 0.5); // Tiempo en minutos
                tiempo *= 24 * 60; // (1 Dia - 24 horas / 1 dia * 60 min / 1 hora) Tiempo en dias
                // Establecer politica de Reparación
                //policy = RepairServerModelPolicy.getInstance();
            break;
            // Avion Ligero
            case 1:
                mu = 40;
                //Hora pico
                if(420 <= m && m <= 600){
                    mu /= 2;
                }
                tiempo = getRandomizer().nextExponencial(mu);
            break;
            // Avion Mediano
            case 2:
                mu = 30;
                //Hora pico
                if(420 <= m && m <= 600){
                    mu /= 2;
                }
                tiempo = getRandomizer().nextExponencial(mu);
            break;
            // Avion Pesado
            case 3:
                int mean = 60;
                int stddev = 2;
                //Hora pico
                if(420 <= m && m <= 600){
                    mean /= 2;
                }
                tiempo = getRandomizer().nextGaussiano(mean, stddev); 
            break;
            default:
                tiempo = 0;
            break;
        }
        // Retornar Prox. Arribo
        evento_Arribo = new ArrivalEvent(actualEvent.getClock() + tiempo , policy, entity);
        return evento_Arribo;
    }
}