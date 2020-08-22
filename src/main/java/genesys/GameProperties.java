package genesys;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "game")
public class GameProperties {
    private int discGoal;

    public int getDiscGoal() {
        return discGoal;
    }

    public void setDiscGoal(int discGoal) {
        this.discGoal = discGoal;
    }
}
