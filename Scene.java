
import java.util.*;

public class Scene {

    private String sceneNumber;
    private String location;
    protected Set<String> mainProtagonists;

    public Scene(String sceneNumber, String location) {
        this.sceneNumber = sceneNumber;
        this.location = location;
        this.mainProtagonists = new LinkedHashSet<>();
    }

    @Override
    public String toString() {
        return "  Scene " + sceneNumber + " - " + location + "\n"
                + "    Main Protagonists: " + mainProtagonists + "\n";
    }
}
