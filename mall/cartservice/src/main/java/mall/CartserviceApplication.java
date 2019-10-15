package mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "mall.cartService.mapper")
public class CartserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartserviceApplication.class, args);
    }

}
