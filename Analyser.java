
import java.util.*;

public class Analyser {

    protected String fileName;

    public Analyser(String fileName) {
        this.fileName = fileName;
    }

    public void analyse() {
        // Read the file and store the lines in List
        List<String> playLines = FileHandler.readFile(fileName);

        // Process protagonist analysis
        Protagonist protagonist = new Protagonist(playLines);
        protagonist.extractProtagonistsAndSpeeches();
        protagonist.extractMainProtagonists();

        // Process play structure and details
        Play play = new Play(playLines);
        play.extractPlayDetails(protagonist.getMainProtagonists());

        // Process lexical analysis
        LexicalAnalyser lexAnalyser = new LexicalAnalyser();
        lexAnalyser.analyse(playLines, protagonist.getProtagonists());

        // Write analysis results
        writeAnalysisResults(fileName, play, protagonist, lexAnalyser);
    }

    public void getMainProtagonists() {
        // Read the file and store lines in List
        List<String> playLines = FileHandler.readFile(fileName);

        // Process protagonist analysis
        Protagonist protagonist = new Protagonist(playLines);
        protagonist.extractProtagonistsAndSpeeches();
        protagonist.extractMainProtagonists();

        // Process play structure and details
        Play play = new Play(playLines);

        // Write analysis results
        writeAnalysisResults(fileName, play, protagonist);
    }

    private void writeAnalysisResults(String fileName, Play play, Protagonist protagonist, LexicalAnalyser lexAnalyser) {
        // Build the analysis report content using writeContent method
        String content = writeContent(play, protagonist, lexAnalyser);
        // Write the generated content to a file
        FileHandler.writeFile(content, fileName);
    }

    private void writeAnalysisResults(String fileName, Play play, Protagonist protagonist) {
        String content = writeContent(play, protagonist);
        FileHandler.writeFile(content, fileName);
    }

    private String writeContent(Play play, Protagonist protagonist, LexicalAnalyser lexAnalyser) {
        StringBuilder content = new StringBuilder(); // Create a StringBuilder object to hold the content

        content.append("\nTitle of Play: ") 
                .append(play.getTitle()); // Generates the title and gets appended to content

        content.append("\n\nNumber of Protagonists: ");
        content.append(protagonist.getProtagonists().size()); // Generates size of list of protagonists

        content.append("\n\nProtagonists:\n");
        content.append(String.join("\n", protagonist.getProtagonists())); // Iterates through the protagonists list and appends each

        content.append("\n\nLocations:\n");
        content.append(String.join("\n", play.getLocations())); // Generates the list of locations in the play

        content.append("\n\nTop-tier main protagonists by frequency of speech turns:\n");
        for (Map.Entry<String, Integer> entry : protagonist.getTopSpeakers().entrySet()) { // For every entry in the topSpeakers map
            content.append(entry.getKey() + ": " + entry.getValue() + "\n"); // Generates the protagonist name and frequency of speech turn in descending order
        }

        content.append("\nTop-tier main protagonists by speech length:\n");
        for (Map.Entry<String, Integer> entry : protagonist.getTopLengthSpeakers().entrySet()) { // For every entry in topLengthSpeakers map
            content.append(entry.getKey() + ": " + entry.getValue() + "\n"); // Generates the protagonist name and speech length in descending order
        }

        content.append("\nOverall main protagonists:\n");
        content.append(String.join("\n", protagonist.getMainProtagonists())); // Generates overall main protagonists

        content.append("\n\nStructure of the Play:\n");
        content.append(play.getStructuredPlayString()); // Generates details of play in a hierarchical structure

        content.append("\n\n\nLEXICAL ANALYSIS:\n");
        content.append("\nWord count: " + lexAnalyser.getTotalWordCount() + "\n"); // Generates the total word count in the play
        
        content.append("\nFrequency of each letter:\n");
        for (Map.Entry<Character, Integer> entry : lexAnalyser.getLetterFrequency().entrySet()) { // For every entry in the letterFrequency map
            content.append(entry.getKey() + ": " + entry.getValue() + "\n"); // Generate each letter and its frequency in descending order
        }
        content.append("\nNumber of unique words: " + lexAnalyser.getTotalUniqueWordCount() + "\n"); // Generates the total unique word count

        content.append("\nThe 100 most frequent words in the play:\n");
        for (Map.Entry<String, Integer> entry : lexAnalyser.getWordFrequency().entrySet()) { // For every entry in wordFrequency map
            content.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n"); // Generate the word and its frequency in descending order
        }

        return content.toString(); // Converts content to String and returns the same
    }

    // Method to write content for getting the main protagonists ONLY
    private String writeContent(Play play, Protagonist protagonist) {
        StringBuilder content = new StringBuilder(); // Create a StringBuilder obj to store the content

        content.append("\nTitle of Play: ")
                .append(play.getTitle()); // Generates title of play

        content.append("\n\nMain protagonists:\n")
                .append(String.join("\n", protagonist.getMainProtagonists())); // Generates the list of main protagonists

        return content.toString(); // Converts content to String and returns it
    }

}
