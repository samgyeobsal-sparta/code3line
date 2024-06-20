package sparta.code3line;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Code3lineApplication {

    public static void main(String[] args) {
        SpringApplication.run(Code3lineApplication.class, args);
    }

}
