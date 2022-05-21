package QLearning;


import java.util.HashMap;

public class EMTable {

    HashMap<int[], Integer> emTable = new HashMap<>();
    HashMap<int[], int[]> instructions = new HashMap<>();


    int[] startPosition;
    int[] previousPosition;

    public EMTable(int[] startPos){
        this.startPosition = startPos;
        this.previousPosition = startPos;
        emTable.put(startPos, 0);
    }

    public int[] getInstructionOf(int[] position){
        if (instructions.containsKey(position)){
            return instructions.get(position);
        }
        return null;
    }

    public int getDistanceToStart(int[] position){
        if (instructions.containsKey(position)){
            return emTable.get(position);
        }
        return -1;
    }

    public void updateEMtable(int step, int[] currentPos){
        if (previousPosition != currentPos){
            if(!instructions.containsKey(previousPosition)){
                instructions.put(previousPosition, currentPos);
            }
        }
        if (!emTable.containsKey(currentPos)){
            emTable.put(currentPos, step);
        }else{
            if(step < emTable.get(currentPos)){
                emTable.put(currentPos, step);
            }
        }
        previousPosition = currentPos;
    }
}
