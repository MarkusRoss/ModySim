package ar.edu.unsl.mys.behaviors;

import ar.edu.unsl.mys.events.Event;
import ar.edu.unsl.mys.engine.AirportSimulation;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.utils.Randomizer;
import ar.edu.unsl.mys.utils.CustomRandomizer;
import ar.edu.unsl.mys.events.EndOfServiceEvent;

public class EndOfServiceEventBehavior extends EventBehavior {
    private static EndOfServiceEventBehavior endOfServiceEventBehavior;

    private EndOfServiceEventBehavior(Randomizer randomizer) {
        super(randomizer);
    }

    public static EndOfServiceEventBehavior getInstance() {
        if (endOfServiceEventBehavior == null) {
            return new EndOfServiceEventBehavior(CustomRandomizer.getInstance());
        } else
            return endOfServiceEventBehavior;
    }

    @Override
    public Event nextEvent(Event actualEvent, Entity entity) {
        EndOfServiceEvent evento_salida; // Construyo un objeto de tipo evento de salida
        double random = getRandomizer().nextRandom();
        float tiempo;

        int tipoEntidad = entity.getType();

        if (entity.getId() == 2126){
            //System.out.println(tipoEntidad);
        }

        switch(tipoEntidad){
            case 0:
            // Mantenimiento
                // Generacion de un numero entre 12 - 24 con distribucion uniforme
                tiempo = getRandomizer().nextUniforme(12, 24); // Tiempo en minutos

                tiempo *= 60; // (1 Hora - 60 min) Tiempo en horas

                // Habilitar Servidor y Repararlo
                //actualEvent.getEntity().getAttendingServer().setServerEnable(true);
                //actualEvent.getEntity().reparar();
            break;
            case 1:
            // Avion Ligero
                if (random < 0.363) {
                    tiempo = 5;
                } else if (random < 0.838) {
                    tiempo = 10;
                } else {
                    tiempo = 15;
                }
            break;
            case 2:
            // Avion Mediano
                tiempo = getRandomizer().nextUniforme(10,20);
            break;
            case 3:
            // Avion Pesado
                if (random < 0.65) {
                    tiempo = 40;
                }
                else {
                    tiempo = 50;
                }
            break;
            default:
                tiempo = 0;
            break;

        }

            

        // Si la hora de este evento supera a la del fin de simulación, no recolectamos
        // el transitTime
        if (!((actualEvent.getClock() + tiempo) > AirportSimulation.getEndTime())) {
            entity.setTransitTime(entity.getWaitingTime() + tiempo);
        }

        // Retornar Prox. Fin de Servicio
        evento_salida = new EndOfServiceEvent(actualEvent.getClock() + tiempo, entity);
        return evento_salida;
    }
}