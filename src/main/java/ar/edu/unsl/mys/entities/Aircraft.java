package ar.edu.unsl.mys.entities;

public class Aircraft extends Entity {
    public Aircraft(int type) {
        super(type);
    }

    @Override
    public String toString() {
        return "Id: " + this.getId() + " Avion de tipo: " + this.getType();
    }
}