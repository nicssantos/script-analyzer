
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class Protagonist {

    private final List<String> playLines;
    private final Map<String, Integer> speechCount = new HashMap<>();
    private final Map<String, Integer> speechLength = new HashMap<>();
    private final Set<String> protagonists = new LinkedHashSet<>();
    private Map<String, Integer> topSpeakers, topLengthSpeakers;
    private Set<String> mainProtagonists = new HashSet<>();
    private Map<String, List<String>> protagonistSpeeches = new HashMap<>();
    private Map<String, Map<String, Integer>> wordFrequencyPerProtagonist = new HashMap<>();

    public Protagonist(List<String> playLines) {
        this.playLines = playLines;
    }

    public Set<String> getProtagonists() {
        //System.out.println("List of Protagonists: " + this.protagonists);
        return protagonists;
    }

    public Map<String, Integer> getTopSpeakers() {
        return topSpeakers;
    }

    public Map<String, Integer> getTopLengthSpeakers() {
        return topLengthSpeakers;
    }

    public Set<String> getMainProtagonists() {
        return mainProtagonists;
    }

    public Map<String, Map<String, Integer>> getWordFrequencyPerProtagonist() {
        Map<String, Map<String, Integer>> wordFreqPerProtagonist = new HashMap<>();

        for (String protagonist : wordFrequencyPerProtagonist.keySet()) {
            Map<String, Integer> freqMap = wordFrequencyPerProtagonist.get(protagonist);

            // Convert to a sorted map (descending order)
            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(freqMap.entrySet());
            sortedEntries.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

            // Keep only the top 30 words
            Map<String, Integer> sortedFreqMap = new LinkedHashMap<>();
            int count = 0;
            for (Map.Entry<String, Integer> entry : sortedEntries) {
                if (count >= 30) {
                    break;
                }
                sortedFreqMap.put(entry.getKey(), entry.getValue());
                count++;
            }

            wordFreqPerProtagonist.put(protagonist, sortedFreqMap);
        }

        return wordFreqPerProtagonist;
    }

    public void extractProtagonistsAndSpeeches() {
        // Regex pattern to detect lines that contain only uppercase letters (assumed to be speaker names)
        Pattern namePattern = Pattern.compile("^[A-Z][A-Z\\s]+$");
        List<String> currentSpeakers = new ArrayList<>(); // Holds the current speaker(s) for each speech block

        // Loop through every line of the play
        for (String line : playLines) {
            line = line.trim(); // Remove leading and trailing whitespace
            Matcher matcher = namePattern.matcher(line); // Check if line matches the name pattern

            // If the line is a speaker's name and not an ACT or SCENE header
            if (matcher.matches() && !line.startsWith("ACT") && !line.startsWith("SCENE")) {
                currentSpeakers.clear(); // Reset current speakers for the new speech block

                // Ignore "ALL" speaker lines completely
                if (line.equals("ALL")) {
                    continue; // Skip this line and go to the next
                }

                String[] names = line.split("\\s+"); // Split line into words (in case of multi-word names)
                boolean allExistSeparately = true; // Flag to check if all names are known separately

                // Check if each word in the line is already a known protagonist
                for (String name : names) {
                    if (!this.protagonists.contains(name)) {
                        allExistSeparately = false; // One or more names are unknown separately
                        break;
                    }
                }

                // If it's a duo speaker (e.g., "ROMEO JULIET") and both names exist separately
                if (names.length > 1 && allExistSeparately) {
                    for (String name : names) {
                        this.protagonists.add(name); // Add each name to the set of protagonists
                        speechCount.put(name, speechCount.getOrDefault(name, 0) + 1); // Count a new speech
                        currentSpeakers.add(name); // Add to current speakers list
                    }
                } else {
                    // Treat the full name line as a single speaker (e.g., "KING CLAUDIUS")
                    String fullName = line;
                    this.protagonists.add(fullName); // Add the full name to the set of protagonists
                    speechCount.put(fullName, speechCount.getOrDefault(fullName, 0) + 1); // Count a new speech
                    currentSpeakers.add(fullName); // Add to current speakers list
                }

            } // If the line is not a speaker name and someone is currently speaking
            else if (!currentSpeakers.isEmpty() && !line.isEmpty()) {
                // Get lowercase set of all protagonist names to filter them out of speeches
                Set<String> protagonistNames = getProtagonists().stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet());

                // Count the number of words in the line (raw speech length)
                int wordLength = line.split("\\s+").length;

                // Clean and tokenize the speech line: remove punctuation, lowercase, filter out names
                List<String> wordList = Arrays.stream(
                        line.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase().split("\\s+")
                )
                        .filter(word -> !protagonistNames.contains(word)) // Remove words that are character names
                        .collect(Collectors.toList());

                // Update stats for each current speaker
                for (String speaker : currentSpeakers) {
                    // Update total words spoken
                    speechLength.put(speaker, speechLength.getOrDefault(speaker, 0) + wordLength);

                    // Store the words spoken in the protagonistSpeeches map
                    protagonistSpeeches.putIfAbsent(speaker, new ArrayList<>());
                    protagonistSpeeches.get(speaker).addAll(wordList);
                }
            }
        }
    }

    public void calculateWordFrequencyPerProtagonist() {
        for (String protagonist : getMainProtagonists()) {
            List<String> words = protagonistSpeeches.getOrDefault(protagonist, new ArrayList<>());

            // Compute word frequency directly
            Map<String, Integer> frequencyMap = new HashMap<>();
            for (String word : words) {
                frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
            }

            wordFrequencyPerProtagonist.put(protagonist, frequencyMap);
        }
    }

    public void extractMainProtagonists() {
        // Identify top 1/3 protagonists by speech count & speech length
        topSpeakers = extractTopProtagonists(speechCount);
        topLengthSpeakers = extractTopProtagonists(speechLength);

        // Find protagonists that are in both lists
        mainProtagonists = new HashSet<>(topSpeakers.keySet());
        mainProtagonists.retainAll(topLengthSpeakers.keySet());
    }

    private static Map<String, Integer> extractTopProtagonists(Map<String, Integer> map) {
        return map.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(map.size() / 3 + 1) // Top 1/3 speakers
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // In case of duplicates, just keep the first one (should not happen)
                        LinkedHashMap::new // Maintain insertion order
                ));
    }
}
