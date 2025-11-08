package mx.edu.uteq.idgs13.microservicio_coordinador.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "profesor_materias")
@Data
public class ProfesorMateriasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profesor_id")
    private Long profesorId;  // ← AÑADIDO

    @ManyToOne
    @JoinColumn(name = "materias_id")
    private MateriasEntity materia;  // ← CAMBIADO DE entityMaterias a materia

    @Column(name = "activo")
    private Boolean activo = true;
}
