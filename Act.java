
import java.util.*;

public class Act {

    private String actNumber;
    protected List<Scene> scenes;

    public Act(String actNumber) {
        this.actNumber = actNumber;
        this.scenes = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Act " + actNumber + "\n");
        for (Scene scene : scenes) {
            sb.append(scene);
        }
        return sb.toString();
    }
}
