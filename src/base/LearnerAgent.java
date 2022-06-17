package base;

import QLearning.QLearning;

public class LearnerAgent extends Agent {

    QLearning brain;

    public LearnerAgent(int[] position) {
        super(position);
        brain = new QLearning(this);
    }

    public void learn(){
        brain.learn();
    }

    public void moveSmartly(){
        brain.moveSmartly();
    }

    public QLearning getBrain() {
        return brain;
    }

}
