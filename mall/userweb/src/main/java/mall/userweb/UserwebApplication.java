package mall.userweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class UserwebApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserwebApplication.class, args);
    }

}
