package QLearning;

import base.Map;
import base.Variables;

import java.util.Arrays;

public class QTable {

    Map map;
    double[][][] table;

    public QTable(Map map) {
        this.map = map;
        this.table = new double[Variables.MAP_HEIGHT][Variables.MAP_WIDTH][QLearning.NUMBER_OF_POSSIBLE_ACTIONS];
    }

    public void initialize(){
        for (double[][] table : table) {
            for(double[] row : table){
                Arrays.fill(row, 0);
            }
        }
    }

    public double getQ(int[] state, int action){
        return this.table[state[0]][state[1]][action];
    }

    public double getQ(int stateX, int stateY, int action){
        return this.table[stateX][stateY][action];
    }

    public void setQ(int[] state, int action, double newQ){
        table[state[0]][state[1]][action] = newQ;
    }

    public double getHighestQAvailableAtPosition(int x, int y){
        double highest = Double.NEGATIVE_INFINITY;
        for (int action = 0; action < QLearning.NUMBER_OF_POSSIBLE_ACTIONS; action++) {
            if(getQ(x,y,action) > highest)
                highest = getQ(x,y,action);
        }
        return highest;
    }

    public byte getActionWithHighestQAtState(int[] state){
        int actionWithHighestQ = 0;
        double highestQ = Double.NEGATIVE_INFINITY;
        for (byte action = 0; action < QLearning.NUMBER_OF_POSSIBLE_ACTIONS; action++) {
            if(getQ(state, action) > highestQ) {
                actionWithHighestQ = action;
                highestQ = getQ(state, action);
            }
        }
        return (byte) actionWithHighestQ;
    }

    private boolean areSamePosition(int[] p1, int[] p2){
        return p1[0] == p2[0] &&
                p1[1] == p2[1];
    }

    private boolean agentHasPreviousPosition(int[] previousPosition){
        return previousPosition.length != 0;
    }

    @Override
    public String toString() {
        for (int stateX = 0; stateX < table.length; stateX++) {
            for (int stateY = 0; stateY < table[stateX].length; stateY++) {
                for (int action = 0; action < table[stateX][stateY].length; action++) {
                    System.out.println(
                            "pos = " + Arrays.toString(new int[]{stateX, stateY}) + " action =  " + action + " qval = " + getQ(new int[]{stateX, stateY}, action));
                }
                System.out.println();
                System.out.println();
            }
            System.out.println();
            System.out.println();
        }
        return "";
    }
}