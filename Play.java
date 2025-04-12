
import java.util.*;
import java.util.regex.*;

public class Play {

    public List<String> playLines;
    private Set<String> locations = new LinkedHashSet<>();
    private final Map<String, Act> acts = new LinkedHashMap<>();
    private final Pattern actPattern = Pattern.compile("^ACT ([IVXLCDM]+)$");
    private final Pattern scenePattern = Pattern.compile("^SCENE ([IVXLCDM]+)\\. (.+)$");
    private final Pattern characterPattern = Pattern.compile("^[A-Z][A-Z]+(?: [A-Z]+)?$");
    private final Pattern locationPattern = Pattern.compile("^SCENE [IVXLCDM]+\\. (.+?)\\.?$");

    public Play(List<String> playLines) {
        this.playLines = playLines;
    }

    public String getTitle() {
        return playLines.get(1);
    }

    public Set<String> getLocations() {

        for (String line : playLines) {
            line = line.trim();
            Matcher matcher = locationPattern.matcher(line);

            if (matcher.matches()) {
                String location = matcher.group(1).trim(); // Extract location part
                locations.add(location);
            }
        }

        return locations;
    }

    public void extractPlayDetails(Set<String> mainProtagonists) {
        String currentAct = "";
        String currentScene = "";
        String currentLocation = "";
        Scene currentSceneObj = null;
        Act currentActObj = null;

        for (String line : playLines) {
            line = line.trim();

            Matcher actMatcher = actPattern.matcher(line);
            if (actMatcher.matches()) {
                currentAct = actMatcher.group(1);
                currentActObj = new Act(currentAct);
                acts.put(currentAct, currentActObj);
                continue;
            }

            Matcher sceneMatcher = scenePattern.matcher(line);
            if (sceneMatcher.matches()) {
                currentScene = sceneMatcher.group(1);
                currentLocation = sceneMatcher.group(2).replaceAll("\\.$", "");
                currentSceneObj = new Scene(currentScene, currentLocation);

                if (currentActObj != null) {
                    currentActObj.scenes.add(currentSceneObj);
                }
                continue;
            }

            Matcher characterMatcher = characterPattern.matcher(line);
            if (characterMatcher.matches() && mainProtagonists.contains(line)) {
                if (currentSceneObj != null) {
                    currentSceneObj.mainProtagonists.add(line);
                }
            }
        }
    }

    public String getStructuredPlayString() {
        StringBuilder structure = new StringBuilder();
        for (Act act : acts.values()) {
            structure.append(act);
        }

        return structure.toString().trim();
    }

}
