package mx.edu.uteq.idgs13.microservicio_coordinador.client;

import java.util.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import mx.edu.uteq.idgs13.microservicio_coordinador.dto.ProfesoresDto;

@FeignClient(name = "microservicio-administrador")
public interface ProfesorEntityClient {

    // Usa GET y parámetros en query string
    @GetMapping("/api/usuarios/profesores")
    List<ProfesoresDto> getAllProfesores();
}
