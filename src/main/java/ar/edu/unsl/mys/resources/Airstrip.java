package ar.edu.unsl.mys.resources;

public class Airstrip extends Server {

    public Airstrip(Queue queue,int tipo) {
        
        super(queue,tipo);
    }

    public Airstrip(Queue queue) {
        super(queue);
    }
  
    public int getHpAsignado(){
        return hpAsig;
    }

    @Override
    public String toString() {
        return "Airstrip " + this.getId() + " -- busy? : " + this.isBusy() + " -- attending: " + this.getServedEntity();
    }
}