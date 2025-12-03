// src/main/java/mx/edu/uteq/idgs13/microservicio_coordinador/repository/ProfesorMateriasRepository.java
package mx.edu.uteq.idgs13.microservicio_coordinador.repository;

import mx.edu.uteq.idgs13.microservicio_coordinador.entity.ProfesorMateriasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesorMateriasRepository extends JpaRepository<ProfesorMateriasEntity, Long> {
	@Query("SELECT p FROM ProfesorMateriasEntity p WHERE p.materia.id = :materiaId")
	java.util.List<ProfesorMateriasEntity> findByMateriaId(Long materiaId);
}