package base;

import QLearning.RewardTable;

public abstract class AudioObject {

    Agent agent;
    int radius;
    int[] position;

    public AudioObject(Agent agent, int radius){
        this.agent = agent;
        this.radius = radius;
        this.position = this.agent.getPosition();
    }


    public Agent getAgent() {
        return agent;
    }

    public int getRadius() {
        return radius;
    }

    public int[] getPosition() {
        return position;
    }

    public void spreadAudio(){
        for(Agent agent: GameController.agents){

            if (agent instanceof Guard && agent != this.agent){
                Guard other = (Guard) agent;
                double distance = RewardTable.distanceBetweenPoints(this.agent.getX(), this.agent.getY(), other.getX(),other.getY());

                if (distance <= this.getRadius()){
                    other.setHeardAudio(this);
                }
            }
        }
    }

}
