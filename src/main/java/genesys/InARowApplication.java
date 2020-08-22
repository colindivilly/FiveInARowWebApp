package genesys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({GridProperties.class, GameProperties.class})
public class InARowApplication {

    public static void main(String[] args) {
        SpringApplication.run(InARowApplication.class, args);
    }
}
