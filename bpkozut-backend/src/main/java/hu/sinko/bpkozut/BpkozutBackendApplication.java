package hu.sinko.bpkozut;

import hu.sinko.bpkozut.Repository.NewsRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = NewsRepository.class)
@ComponentScan(basePackages = {"hu.sinko.bpkozut.*"})
@EntityScan("hu.sinko.bpkozut.Model")
public class BpkozutBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BpkozutBackendApplication.class, args);
    }

}
