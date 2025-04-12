
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileHandler {

    /**
     * Checks whether file exists or not.
     * @param fileName The file to check existence of.
     * @return True or false values depending if file exists.
     */
    public static boolean fileExists(String fileName) {
        File file = Paths.get(fileName + ".txt").toFile(); // Retrieves the file
        return file.exists(); // Checks whether it exists
    }

    /**
     * Reads script file and stores lines in a List.
     * @param fileName The file to read lines from.
     * @return List of lines in the play.
     */
    public static List<String> readFile(String fileName) {
        try {
            List<String> lines = new ArrayList<>(); // Initialize a List to store the lines 
            File file = Paths.get(fileName + ".txt").toFile(); // Retrieve the file
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) { // Read line
                lines.add(line); // Add line to the List
            }
            reader.close(); // Close the reader
            return lines;
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage()); // Print the error message
            return null;
        }
    }

    /**
     * Writes content (analysis report) to file.
     * @param content The content to be written.
     * @param fileName The file with analysis report written.
     */
    public static void writeFile(String content, String fileName) {
        Path path = Paths.get(fileName + "_Analysis_Report.txt"); // Get file path
        //System.out.println(path);
        try (FileWriter writer = new FileWriter(path.toFile())) { // Create a FileWriter obj and pass the file name
            writer.write(content); // Write content to file
            System.out.println("Report successfully written to file: " + fileName + "_Analysis_Report.txt"); // Print successful operation message
        } catch (IOException e) {
            System.out.println("Error writing the file: " + e.getMessage()); // Print error message
        }
    }

}
