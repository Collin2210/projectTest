package base;


import java.util.Scanner;

public class FileParser2 {

    public static Variables parser(Scanner scanner) {
        Variables v = new Variables();
        int numberLines = 1;
        while (scanner.hasNextLine()) {
            //
            numberLines++;
        }
        return v;
    }

//    public static void parseVariables(Variables v, String nextVariable, int numberLines) {
//        try (Scanner scan = new Scanner(nextVariable)) {
//            scan.useDelimiter("=");
//            if (scan.hasNext()) {
//                String id = scan.next();
//                String value = scan.next();
//                value = unnecessaryLines(value);
//
//                id = id.trim();
//                value = value.trim();
//                String[] coords = value.split(" ");
//                switch (id){
//
//                    case "height":
//                        v.setHeight(Integer.parseInt(value));
//                        break;
//                    case "width":
//                        v.setWidth(Integer.parseInt(value));
//                        break;
//                    case "scaling":
//                        v.setScaling((float) Double.parseDouble(value));
//                        break;
//                    case "numGuards":
//                        v.setNumberOfGuards(Integer.parseInt(value));
//                        break;
//                    case "numIntruders":
//                        v.setNumberOfIntruders(Integer.parseInt(value));
//                        break;
//                    case "baseSpeedIntruder":
//                        v.setWalkingSpeedIntruder((float) Double.parseDouble(value));
//                        break;
//                    case "sprintSpeedIntruder":
//                        //v.setSprintingSpeedIntruder(Double.parseDouble(value));
//                        break;
//                    case "baseSpeedGuard":
//                        v.setWalkingSpeedGuard((float) Double.parseDouble(value));
//                        break;
//                    case "wall":
//                        v.setWalls();
//                    case "teleport":
//                        v.setTeleport(Integer.parseInt(value));
//                    case "spawnAreaGuards":
//                        v.setSpawnAreaGuards();
//
//                }
//            }
//        }
//    }

    private static String unnecessaryLines(String s) {
        if (s.contains("//")) {
            return s.split("//")[0];
        }
        return s;
    }
}





