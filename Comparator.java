
import java.util.*;

public class Comparator extends Analyser {

    protected String fileName2;

    public Comparator(String fileName1, String fileName2) {
        super(fileName1);
        this.fileName2 = fileName2;
    }

    @Override
    public void analyse() {
        // Read two files and store play lines in List
        List<String> playLines1 = FileHandler.readFile(super.fileName);
        List<String> playLines2 = FileHandler.readFile(fileName2);

        // Process protagonist analysis for both scripts
        Protagonist protagonist1 = new Protagonist(playLines1);
        protagonist1.extractProtagonistsAndSpeeches();
        protagonist1.extractMainProtagonists();
        protagonist1.calculateWordFrequencyPerProtagonist();

        Protagonist protagonist2 = new Protagonist(playLines2);
        protagonist2.extractProtagonistsAndSpeeches();
        protagonist2.extractMainProtagonists();
        protagonist2.calculateWordFrequencyPerProtagonist();

        // Extract important details for comparison
        Play play1 = new Play(playLines1);
        play1.extractPlayDetails(protagonist1.getMainProtagonists());

        Play play2 = new Play(playLines2);
        play2.extractPlayDetails(protagonist2.getMainProtagonists());

        // Process lexical analysis
        LexicalAnalyser lexAnalyser1 = new LexicalAnalyser();
        lexAnalyser1.analyse(playLines1, protagonist1.getProtagonists());

        LexicalAnalyser lexAnalyser2 = new LexicalAnalyser();
        lexAnalyser2.analyse(playLines2, protagonist2.getProtagonists());

        // Compare script to another script lexically
        lexAnalyser1.compareWith(lexAnalyser2);

        String fileName = super.fileName + "_and_" + fileName2; // Create the corresponding fileName
        writeAnalysisResults(fileName, play1, play2, protagonist1, protagonist2, lexAnalyser1, lexAnalyser2); // Write analysis results to the fileName
    }

    public void getJaccardSimilarity() {
        // Read two files and store play lines in List
        List<String> playLines1 = FileHandler.readFile(super.fileName);
        List<String> playLines2 = FileHandler.readFile(fileName2);

        // Process protagonist analysis in both plays
        Protagonist protagonist1 = new Protagonist(playLines1);
        protagonist1.extractProtagonistsAndSpeeches();
        protagonist1.extractMainProtagonists();

        Protagonist protagonist2 = new Protagonist(playLines2);
        protagonist2.extractProtagonistsAndSpeeches();
        protagonist2.extractMainProtagonists();

        // Extract important details for comparison
        Play play1 = new Play(playLines1);
        play1.extractPlayDetails(protagonist1.getMainProtagonists());

        Play play2 = new Play(playLines2);
        play2.extractPlayDetails(protagonist2.getMainProtagonists());

        // Process lexical analysis
        LexicalAnalyser lexAnalyser1 = new LexicalAnalyser();
        lexAnalyser1.analyse(playLines1, protagonist1.getProtagonists());

        LexicalAnalyser lexAnalyser2 = new LexicalAnalyser();
        lexAnalyser2.analyse(playLines2, protagonist2.getProtagonists());

        // Compare script to another lexically
        lexAnalyser1.compareWith(lexAnalyser2);

        String fileName = super.fileName + "_and_" + fileName2;
        writeAnalysisResults(fileName, play1, play2, lexAnalyser1);
    }

    private void writeAnalysisResults(String fileName, Play play1, Play play2, LexicalAnalyser lexAnalyser) {
        String content = writeContent(play1, play2, lexAnalyser);
        FileHandler.writeFile(content, fileName);
    }

    private String writeContent(Play play1, Play play2, LexicalAnalyser lexAnalyser) {
        StringBuilder content = new StringBuilder();
        content.append("Play A: " + play1.getTitle() + "\n")
                .append("Play B: " + play2.getTitle())
                .append("\n\nJaccard Similarity: " + String.format("%.2f", lexAnalyser.getJaccardSimilairty()));
        return content.toString();
    }

    private void writeAnalysisResults(String fileName, Play play1, Play play2, Protagonist protagonist1, Protagonist protagonist2, LexicalAnalyser lexAnalyser1, LexicalAnalyser lexAnalyser2) {
        String content = writeContent(play1, play2, protagonist1, protagonist2, lexAnalyser1, lexAnalyser2);
        FileHandler.writeFile(content, fileName);
    }

    private String writeContent(Play play1, Play play2, Protagonist protagonist1, Protagonist protagonist2, LexicalAnalyser lexAnalyser1, LexicalAnalyser lexAnalyser2) {
        StringBuilder content = new StringBuilder();

        content.append("\nComparison of Plays\n")
                .append("\nPlay A: " + play1.getTitle() + "\nPlay B: " + play2.getTitle())
                .append("\n\nNo. of protagonists in Play A: " + protagonist1.getProtagonists().size())
                .append("\nNo. of protagonists in Play B: " + protagonist2.getProtagonists().size())
                .append("\n\nMain protagonists in Play A:\n")
                .append(String.join("\n", protagonist1.getMainProtagonists()))
                .append("\n\nMain protagonists in Play B:\n")
                .append(String.join("\n", protagonist2.getMainProtagonists()));

        content.append("\n\nThe 30 most frequent words main protagonists in Play A say:\n");

        for (Map.Entry<String, Map<String, Integer>> entry : protagonist1.getWordFrequencyPerProtagonist().entrySet()) {
            String protagonist = entry.getKey();
            Map<String, Integer> frequencies = entry.getValue();

            content.append(protagonist + ": [");

            List<String> wordFreqList = new ArrayList<>();
            for (Map.Entry<String, Integer> wordEntry : frequencies.entrySet()) {
                wordFreqList.add(wordEntry.getKey() + ": " + wordEntry.getValue());
            }

            content.append(String.join(", ", wordFreqList));
            content.append("]\n");
        }
        
        content.append("\n\nThe 30 most frequent words main protagonists in Play B say:\n");

        for (Map.Entry<String, Map<String, Integer>> entry : protagonist2.getWordFrequencyPerProtagonist().entrySet()) {
            String protagonist = entry.getKey();
            Map<String, Integer> frequencies = entry.getValue();

            content.append(protagonist + ": [");

            List<String> wordFreqList = new ArrayList<>();
            for (Map.Entry<String, Integer> wordEntry : frequencies.entrySet()) {
                wordFreqList.add(wordEntry.getKey() + ": " + wordEntry.getValue());
            }

            content.append(String.join(", ", wordFreqList));
            content.append("]\n");
        }
        
        content.append("\n\nThe 100 most frequent words in Play A:\n");
        for (Map.Entry<String, Integer> entry : lexAnalyser1.getWordFrequency().entrySet()) {
            content.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        content.append("\nThe 100 most frequent words in Play B:\n");
        for (Map.Entry<String, Integer> entry : lexAnalyser2.getWordFrequency().entrySet()) {
            content.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        content.append("\nThe 100 most frequent words in both plays combined:\n");
        for (Map.Entry<String, Integer> entry : lexAnalyser1.getMergedWordFrequency().entrySet()) {
            content.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        content.append("\nMost frequent words excluding words common to both lists in Play A:\n");
        for (Map.Entry<String, Integer> entry : lexAnalyser1.getFilteredWordFrequency().entrySet()) {
            content.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        content.append("\nMost frequent words excluding words common to both lists in Play B:\n");
        for (Map.Entry<String, Integer> entry : lexAnalyser2.getFilteredWordFrequency().entrySet()) {
            content.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        content.append("\nUnique words in Play A:\n");
        content.append(String.join(", ", lexAnalyser1.getUniqueWords()));

        content.append("\n\nUnique words in Play B:\n");
        content.append(String.join(", ", lexAnalyser2.getUniqueWords()));

        content.append("\n\nJaccard Similarity: " + String.format("%.2f", lexAnalyser1.getJaccardSimilairty()));

        return content.toString();
    }

}
