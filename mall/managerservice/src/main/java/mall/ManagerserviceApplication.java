package mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "mall.managerservice.mapper")
public class ManagerserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerserviceApplication.class, args);
    }
}
