package umg.programacionIII.Repo_Spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"umg.programacionIII.Repo_Spring", "umg.programacionIII.service"})
@EntityScan("umg.programacionIII.model")
@EnableJpaRepositories("umg.programacionIII.repository")
public class RepoSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepoSpringApplication.class, args);
	}
}