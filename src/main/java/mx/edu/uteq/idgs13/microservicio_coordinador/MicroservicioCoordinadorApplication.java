package mx.edu.uteq.idgs13.microservicio_coordinador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MicroservicioCoordinadorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioCoordinadorApplication.class, args);
	}

}
