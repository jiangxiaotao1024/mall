package mall.itemweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ItemwebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemwebApplication.class, args);
    }

}
