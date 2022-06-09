package Controller;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileParser {

    public static Variables parser(String path) {
        String content = "";
        try{
            content = new String(Files.readAllBytes(Paths.get(path)));
        } catch (Exception e){
            e.printStackTrace();
        }

        String[] unparsedVals = content.split("\n");
        Variables v = new Variables();
        for(String s: unparsedVals){
            parseVariables(v,s,0);
        }
        return v;
    }

    public static Variables parse(String[] unparsedVals){
        Variables v = new Variables();
        for(String s : unparsedVals){
            parseVariables(v,s,0);
        }
        return v;
    }


    public static void parseVariables(Variables v, String nextVariable, int numberLines) {


        String[] unparsedVals = nextVariable.split(" ");
        String id = unparsedVals[0];

        String value = unparsedVals[2];

        for(String s: unparsedVals){
            s = s.trim();
        }
        id = id.trim();
        value = value.trim();
        String[] coords = unparsedVals;

        int i = 2;
        switch (id){

            case "height":
                v.setHeight(Integer.parseInt(value));
                break;
            case "width":
                v.setWidth(Integer.parseInt(value));
                break;
            case "scaling":
                v.setScaling((float) Double.parseDouble(value));
                break;
            case "numGuards":
                v.setNumberOfGuards(Integer.parseInt(value));
                break;
            case "numIntruders":
                v.setNumberOfIntruders(Integer.parseInt(value));
                break;
            case "baseSpeedIntruder":
                v.setWalkingSpeedIntruder((float) Double.parseDouble(value));
                break;
            case "sprintSpeedIntruder":
                //v.setSprintingSpeedIntruder(Double.parseDouble(value));
                break;
            case "baseSpeedGuard":
                v.setWalkingSpeedGuard((float) Double.parseDouble(value));
                break;
            case "visionRange":
                v.setVisionRange(Integer.parseInt(value));
                break;
            case "wall":
                v.createWall(Integer.parseInt(coords[0+i].trim()),Integer.parseInt(coords[1+i].trim()),Integer.parseInt(coords[2+i].trim()),
                        Integer.parseInt(coords[3+i].trim()));
                break;
            case "teleport":
                v.createTeleport(Integer.parseInt(coords[0+i].trim()),Integer.parseInt(coords[1+i].trim()),Integer.parseInt(coords[2+i].trim()),
                        Integer.parseInt(coords[3+i].trim()),Integer.parseInt(coords[4+i].trim()),Integer.parseInt(coords[5+i].trim()),
                        Double.parseDouble(coords[6+i].trim()));
                break;
            case "spawnAreaGuards":
                v.createSpawnAreaGuards(Integer.parseInt(coords[0+i].trim()),Integer.parseInt(coords[1+i].trim()),Integer.parseInt(coords[2+i].trim()),
                        Integer.parseInt(coords[3+i].trim()));
                break;
            case "spawnAreaIntruders":
                v.createSpawnAreaIntruders(Integer.parseInt(coords[0+i].trim()),Integer.parseInt(coords[1+i].trim()),Integer.parseInt(coords[2+i].trim()),
                        Integer.parseInt(coords[3+i].trim()));
                break;
            case "isGoal":
                v.createGoal(Integer.parseInt(coords[0+i].trim()),Integer.parseInt(coords[1+i].trim()),Integer.parseInt(coords[2+i].trim()),
                        Integer.parseInt(coords[3+i].trim()));
                break;
            case "shade":
                v.createShade(Integer.parseInt(coords[0+i].trim()),Integer.parseInt(coords[1+i].trim()),Integer.parseInt(coords[2+i].trim()),
                        Integer.parseInt(coords[3+i].trim()));
                break;
            case "tower":
                v.createTower(Integer.parseInt(coords[0+i].trim()),Integer.parseInt(coords[1+i].trim()),Integer.parseInt(coords[2+i].trim()),
                        Integer.parseInt(coords[3+i].trim()));
                break;
        }


    }

}




