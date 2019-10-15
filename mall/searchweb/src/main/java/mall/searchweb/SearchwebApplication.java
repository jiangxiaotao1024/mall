package mall.searchweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SearchwebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchwebApplication.class, args);
    }

}
