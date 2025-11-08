// src/main/java/mx/edu/uteq/idgs13/microservicio_coordinador/dto/ProfesorConMateriasDto.java
package mx.edu.uteq.idgs13.microservicio_coordinador.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProfesorConMateriasDto {
    private Long profesorId;
    private String nombre;
    private String email;
    private List<MateriasDto> materias;
}