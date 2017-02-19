package sec.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import sec.project.domain.Signup;

@SpringBootApplication
public class CyberSecurityBaseProjectApplication {

    public static void main(String[] args) throws Throwable {
        System.setProperty("spring.template.cache", "false");
        System.setProperty("spring.thymeleaf.cache", "false");
        
        SpringApplication.run(CyberSecurityBaseProjectApplication.class);
    }
}
