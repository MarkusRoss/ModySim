package ar.edu.unsl.mys.resources;

import java.util.Iterator;

import ar.edu.unsl.mys.entities.Entity;

public class CustomQueue extends Queue {
    public CustomQueue() {
        super();
    }

    @Override
    public String toString() {
        String ret = "server queue " + this.getAssignedServer().getId() + ":\n\t";

        Iterator<Entity> it = this.getQueue().iterator();

        while (it.hasNext()) {
            ret += it.next().toString();
        }

        return ret;
    }

    @Override
    public void enqueue(Entity entity) {
        this.getQueue().add(entity);
        if (this.getQueue().size() > this.getMaxSize()) {
            this.setMaxSize(this.getQueue().size());
        }
    }

    @Override
    public Entity next() { // Devuelve a la entidad en espera
        return this.getQueue().remove();
    }

    @Override
    public Entity checkNext() {
        Iterator<Entity> it = this.getQueue().iterator();

        if (it.hasNext()) {
            System.out.println(it.next().getId());
            return it.next();
        } else {
            System.out.println("Cola nula");
            return null;
        }
    }

    public void debug() {
        Iterator<Entity> it = this.getQueue().iterator();

        if (it.hasNext())
            System.out.println();

    }
}