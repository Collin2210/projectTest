package Controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class ReadFiles
{

    public static String[] readFileAsString(String fileName)throws Exception
    {
        String[] values = new String[23];
        int i = 0;

        String content = new String(Files.readAllBytes(Paths.get(fileName)));

        Scanner scanner = new Scanner(content);
        while (scanner.hasNextLine() && i<23) {
            String line = scanner.nextLine();
            String[] output;
            output = line.split(" = ");
            values[i] = output[1];
            i++;
        }

        scanner.close();

        return values;
    }


}