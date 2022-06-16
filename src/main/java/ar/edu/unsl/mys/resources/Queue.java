package ar.edu.unsl.mys.resources;

import java.util.LinkedList;

import ar.edu.unsl.mys.entities.Entity;

public abstract class Queue
{
    private Server assignedServer;
    private java.util.Queue<Entity> queue;
    private int maxSize;

    public Queue()
    {
        queue = new LinkedList<>();
        maxSize=0;
        
    }

    public Server getAssignedServer()
    {
        return assignedServer;

    }

    public void setAssignedServer(Server assignedServer)
    {
        this.assignedServer = assignedServer;

    }

    public java.util.Queue<Entity> getQueue()
    {
        return queue;
    }

    public int getMaxSize()
    {
        return maxSize;

    }

    public void setMaxSize(int maxSize)
    {
        this.maxSize = maxSize;
    }

    public boolean isEmpty()
    {
        return queue.isEmpty();   
    }

    /**
     * Poner en cola una entidad usando la política definida en el subyacente
     * implementación de este método.
     * 
     * @param entidad La entidad que se pondrá en cola.
     */
    public abstract void enqueue(Entity entity);

    /**
     * Obtiene la siguiente entidad en la cola.
     * Después de llamar a este método, la entidad devuelta ya no está en la cola.
     * 
     * @return La siguiente entidad en la cola.
     */
    public abstract Entity next();

    /**
     * Comprueba el siguiente elemento de la cola sin eliminarlo.
     * La cola permanece intacta después de llamar a este método.
     * 
     * @return La siguiente entidad en la cola.
     */
    public abstract Entity checkNext();
}