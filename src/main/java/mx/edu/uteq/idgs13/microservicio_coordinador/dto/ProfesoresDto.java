package mx.edu.uteq.idgs13.microservicio_coordinador.dto;

import lombok.Data;

@Data
public class ProfesoresDto {
    private Long id;
    private String nombre;
    private String email;
    private boolean activo;
}
