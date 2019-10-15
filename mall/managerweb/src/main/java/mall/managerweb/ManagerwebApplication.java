package mall.managerweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ManagerwebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerwebApplication.class, args);
    }

}
