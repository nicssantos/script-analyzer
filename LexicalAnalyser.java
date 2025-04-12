
import java.util.*;

public class LexicalAnalyser {

    private final Set<String> stopWords; // Holds the list of stop words
    private int totalWordCount = 0, totalUniqueWordCount = 0; // Initialize total count for all words and unique words to 0
    private Map<Character, Integer> letterFrequency = new HashMap<>(); // Map to store frequency of each letter
    private Map<String, Integer> wordFrequency = new HashMap<>(); // Map to store frequency of each word
    private Set<String> uniqueWords; // Set of unique words in the play
    // For comparisons:
    private Map<String, Integer> mergedWordFrequency = new HashMap<>(); // Map to store frequency of each word in two plays combined
    private Map<String, Integer> filteredWordFrequency = new HashMap<>(); // Map to store frequency of each word(excluding common words) in each play
    private double jaccardSimilarity; // Holds value for Jaccard Similarity

    /**
     * Constructor initializes the LexicalAnalyser by loading stop words from a
     * file.
     */
    public LexicalAnalyser() {
        String fileName = "StopWords-1"; // Give name of file where stop words are stored
        List<String> stopWords = new ArrayList<>();
        if (FileHandler.fileExists(fileName)) {
            stopWords = FileHandler.readFile(fileName); // Read the file, each word gets stored in a List
        } else {
            System.out.println("Eror reading file: " + fileName + " (File not found)");
        }
        this.stopWords = new HashSet<>(stopWords); // Store in a Set
    }

    /**
     * Returns the total word count in the play.
     */
    public int getTotalWordCount() {
        return totalWordCount;
    }

    /**
     * Returns the total count of unique words in the play.
     */
    public int getTotalUniqueWordCount() {
        return totalUniqueWordCount;
    }

    /**
     * Returns a sorted map of letter frequencies in descending order.
     */
    public Map<Character, Integer> getLetterFrequency() {
        List<Map.Entry<Character, Integer>> letterList = new ArrayList<>(letterFrequency.entrySet()); // Convert Map to List
        letterList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // Sort in descending order

        Map<Character, Integer> sortedLetterFreq = new LinkedHashMap<>(); // Create a new LinkedHashMap to maintain the insertion order after sorting
        for (Map.Entry<Character, Integer> entry : letterList) {
            sortedLetterFreq.put(entry.getKey(), entry.getValue()); // Store sorted values
        }

        return sortedLetterFreq;
    }

    /**
     * Returns a sorted map of word frequencies in descending order.
     */
    public Map<String, Integer> getWordFrequency() {
        // Sort the wordFrequency Map based on frequency in descending order
        List<Map.Entry<String, Integer>> wordList = new ArrayList<>(wordFrequency.entrySet());
        wordList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Create a new LinkedHashMap to maintain the insertion order after sorting
        Map<String, Integer> sortedWordFreq = new LinkedHashMap<>();
        // Add only the top 100 entries
        int count = 0;
        for (Map.Entry<String, Integer> entry : wordList) {
            if (count >= 100) {
                break;  // Stop after adding 100 entries
            }
            sortedWordFreq.put(entry.getKey(), entry.getValue());
            count++;
        }
        return sortedWordFreq;
    }

    /**
     * Returns a set of unique words found in the play.
     */
    public Set<String> getUniqueWords() {
        return this.uniqueWords;
    }

    /**
     * Returns a sorted map of word frequencies from two plays combined in
     * descending order.
     */
    public Map<String, Integer> getMergedWordFrequency() {
        // Sort the mergedWordFrequency Map based on frequency in descending order
        List<Map.Entry<String, Integer>> wordList = new ArrayList<>(mergedWordFrequency.entrySet());
        wordList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Create a new LinkedHashMap to maintain the insertion order after sorting
        Map<String, Integer> sortedMergedWordFreq = new LinkedHashMap<>();
        // Add only the top 100 entries
        int count = 0;
        for (Map.Entry<String, Integer> entry : wordList) {
            if (count >= 100) {
                break;  // Stop after adding 100 entries
            }
            sortedMergedWordFreq.put(entry.getKey(), entry.getValue());
            count++;
        }
        return sortedMergedWordFreq;

    }

    /**
     * Returns a map of word frequencies after filtering out common words.
     */
    public Map<String, Integer> getFilteredWordFrequency() {
        // Sort the filteredWordFrequency Map based on frequency in descending order
        List<Map.Entry<String, Integer>> wordList = new ArrayList<>(filteredWordFrequency.entrySet());
        wordList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Create a new LinkedHashMap to maintain the insertion order after sorting
        Map<String, Integer> sortedFilteredWordFreq = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : wordList) {
            sortedFilteredWordFreq.put(entry.getKey(), entry.getValue());
        }
        return sortedFilteredWordFreq;
    }

    /**
     * Returns the Jaccard similarity score between two plays.
     */
    public double getJaccardSimilairty() {
        return jaccardSimilarity;
    }

    /**
     * Analyses the play, calculates word and letter frequencies, filter
     * protagonists names and stop words, and extracts unique words.
     *
     * @param playLines List of play lines.
     * @param protagonists Set of protagonists names to be filtered out.
     */
    public void analyse(List<String> playLines, Set<String> protagonists) {
        // Get the play length in words
        List<String> words = tokenizeText(playLines); // Tokenize each lines
        totalWordCount = words.size(); // Get size of List to get the count of words

        // Calculate frequency of each letter in the play
        letterFrequency = calculateLetterFrequency(words);

        // Calculate frequency of each word in the play
        words.removeAll(stopWords); // First, remove stop words from the list of words
        words = removeProtagonists(protagonists, words); // As well as protagonists names
        wordFrequency = calculateWordFrequency(words); // Then, calculate frequency of each word

        // Get unique words in the play
        uniqueWords = new HashSet<>(words); // Make a Set of all words in the play
        uniqueWords.removeIf(word -> wordFrequency.getOrDefault(word, 0) > 1); // Remove word if it occurs more than once
        totalUniqueWordCount = uniqueWords.size(); // Get count of unique words
    }

    /**
     * Compares this play with another using word frequencies and computes
     * Jaccard similarity.
     *
     * @param otherAnalyser Another LexicalAnalyser instance to compare with.
     */
    public void compareWith(LexicalAnalyser otherAnalyser) {
        // Get merged word frequencies
        mergedWordFrequency = new HashMap<>(this.getWordFrequency());
        otherAnalyser.getWordFrequency().forEach((key, value)
                -> mergedWordFrequency.merge(key, value, Integer::sum));

        // Get the list of most frequent words in each play excluding the common words found in mergedWordFrequency and list of most frequent words (each play)
        calculateFilteredWordFrequency(otherAnalyser);

        // Calculate the Jaccard Similarity of two plays
        jaccardSimilarity = computeJaccardSimilarity(otherAnalyser);
    }

    /**
     * Calculates word frequencies excluding common words found in 100 most
     * frequent words and 100 most frequent words in 2 plays combined.
     *
     * @param otherAnalyser Another LexicalAnalyser instance to get word
     * frequency from.
     */
    private void calculateFilteredWordFrequency(LexicalAnalyser otherAnalyser) {
        //System.out.println("calculateFilteredWordFrequency() called");
        // Get common words for one play
        Set<String> commonWords1 = new HashSet<>(this.getWordFrequency().keySet());
        commonWords1.retainAll(getMergedWordFrequency().keySet());

        // Get common words for the other play
        Set<String> commonWords2 = new HashSet<>(otherAnalyser.getWordFrequency().keySet());
        commonWords2.retainAll(getMergedWordFrequency().keySet());

        // Create a Map to store the filtered words and their frequencies for each play
        filteredWordFrequency = new HashMap<>(this.getWordFrequency());
        otherAnalyser.filteredWordFrequency = new HashMap<>(otherAnalyser.getWordFrequency());

        // Remove common words from the lists
        for (String word : commonWords1) {
            filteredWordFrequency.remove(word);
        }
        for (String word : commonWords2) {
            otherAnalyser.filteredWordFrequency.remove(word);
        }
    }

    /**
     * Calculates the Jaccard similarity score of this play to another.
     *
     * @param otherAnalyser Another LexicalAnalyser instance to compare with.
     * @return Jaccard similarity score.
     */
    private double computeJaccardSimilarity(LexicalAnalyser otherAnalyser) {
        Set<String> words1 = new HashSet<>(this.getWordFrequency().keySet());
        Set<String> words2 = new HashSet<>(otherAnalyser.getWordFrequency().keySet());

        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2);

        Set<String> union = new HashSet<>(words1);
        union.addAll(words2);

        return union.isEmpty() ? 0 : (double) intersection.size() / union.size();
    }

    /**
     * Tokenizes the text from the given play lines.
     *
     * @param playLines A List of strings representing lines from a play.
     * @return A List of words extracted from the text
     */
    private List<String> tokenizeText(List<String> playLines) {
        List<String> words = new ArrayList<>(); // Initialize List to store words
        StringTokenizer tokenizer;
        for (String line : playLines) { // Iterate through each line in the play
            tokenizer = new StringTokenizer(line); // Tokenize the line into words
            while (tokenizer.hasMoreTokens()) { // Process each word in the line
                String token = tokenizer.nextToken().toLowerCase(); // Conver word to lowercase
                token = token.replaceAll("[^a-zA-Z]", ""); // Remove punctuation and non-letter characters
                if (!token.isEmpty()) { // Add word to list if token is not empty
                    words.add(token);
                }
            }
        }
        return words;
    }

    /**
     * Calculates the frequency of each letter in a given list of words.
     *
     * @param words A List of words extracted from the text.
     * @return A Map where keys are letters (characters) and values are their
     * frequencies.
     */
    private Map<Character, Integer> calculateLetterFrequency(List<String> words) {
        Map<Character, Integer> letterFreq = new HashMap<>(); // Initialise Map to store letter frequencies
        for (String word : words) { // Iterate through each word
            for (char ch : word.toCharArray()) { // Iterate through each character in the word
                if (Character.isLetter(ch)) { // Ensure it's a letter
                    letterFreq.put(ch, letterFreq.getOrDefault(ch, 0) + 1); // Increment count in Map
                }
            }
        }

        return letterFreq;
    }

    /**
     * Calculates the frequency of each word in a given list.
     *
     * @param words A List of words extracted from the text.
     * @return A Map where keys are words and values are their frequencies.
     */
    private Map<String, Integer> calculateWordFrequency(List<String> words) {
        Map<String, Integer> wordFreq = new HashMap<>(); // Initialise Map to store word frequencies
        for (String word : words) { // Iterate through each word
            wordFreq.put(word, wordFreq.getOrDefault(word, 0) + 1); // Increment word count in Map
        }

        return wordFreq;
    }

    /**
     * Removes protagonist names from the list of words. Supports removing
     * multi-word names by matching the first two words.
     *
     * @param protagonists A Set of protagonist names to be removed.
     * @param words A List of words from which protagonist names should be
     * removed.
     * @return A List of words with protagonist names filtered out.
     */
    private List<String> removeProtagonists(Set<String> protagonists, List<String> words) {
        List<String> filteredWords = new ArrayList<>(words); // Make a copy of original list of words

        for (String protagonist : protagonists) { // Iterate through each protagonist's names
            String[] nameParts = protagonist.split(" "); // Split protagonist's names if having two or more words
            int maxWordsToMatch = Math.max(1, Math.min(2, nameParts.length)); // Match first 2 words if applicable

            // Step 1: Remove the common reference (first two words of name)
            for (int i = 0; i < filteredWords.size() - maxWordsToMatch + 1; i++) {
                boolean match = true;
                for (int j = 0; j < maxWordsToMatch; j++) { // Check if the words match the protagonist's name
                    if (!filteredWords.get(i + j).equalsIgnoreCase(nameParts[j])) {
                        match = false;
                        break;
                    }
                }
                if (match) { // Remove matched words
                    for (int j = 0; j < maxWordsToMatch; j++) {
                        filteredWords.remove(i);
                    }
                    i -= maxWordsToMatch; // Adjust index to avoid skipping words
                }
            }

            // Step 2: Remove any remaining parts of the protagonist’s name appearing separately
            for (String namePart : nameParts) {
                filteredWords.removeIf(word -> word.equalsIgnoreCase(namePart)); // remove individual parts of the name
            }
        }

        return filteredWords;
    }

}
