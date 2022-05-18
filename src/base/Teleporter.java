package base;

public class Teleporter {

    public int[]
            position,
            destination;
    public double agentAngleAtArrival;

    public Teleporter(int[] position, int[] destination, double agentAngleAtArrival) {
        this.position = position;
        this.destination = destination;
        this.agentAngleAtArrival = agentAngleAtArrival;
    }

}
