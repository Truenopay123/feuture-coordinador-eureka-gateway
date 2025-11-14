package mx.edu.uteq.idgs13.microservicio_coordinador.service;

import mx.edu.uteq.idgs13.microservicio_coordinador.client.ProfesorEntityClient;
import mx.edu.uteq.idgs13.microservicio_coordinador.dto.MateriasDto;
import mx.edu.uteq.idgs13.microservicio_coordinador.dto.ProfesorConMateriasDto;
import mx.edu.uteq.idgs13.microservicio_coordinador.dto.ProfesoresDto;
import mx.edu.uteq.idgs13.microservicio_coordinador.entity.MateriasEntity;
import mx.edu.uteq.idgs13.microservicio_coordinador.entity.ProfesorMateriasEntity;
import mx.edu.uteq.idgs13.microservicio_coordinador.repository.MateriasRepository;
import mx.edu.uteq.idgs13.microservicio_coordinador.repository.ProfesorMateriasRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MateriasService {

    @Autowired
    private MateriasRepository materiasRepository;

    @Autowired
    private ProfesorEntityClient profesorClient;

    @Autowired
    private ProfesorMateriasRepository profesorMateriasRepository;

    public List<MateriasDto> getAllMaterias() {
        List<MateriasEntity> materias = materiasRepository.findAll();
        return materias.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MateriasDto getMateriaById(Long id) {
        Optional<MateriasEntity> materia = materiasRepository.findById(id);
        return materia.map(this::convertToDto).orElse(null);
    }

    // NUEVO MÉTODO: Crear materia
    public MateriasDto createMateria(MateriasDto materiaDto) {
        MateriasEntity materia = new MateriasEntity();
        
        // Copiar propiedades del DTO a la entidad
        materia.setNombre(materiaDto.getNombre());
        materia.setDescripcion(materiaDto.getDescripcion());
        
        // Si no se especifica "activo", por defecto será true
        materia.setActivo(materiaDto.getActivo() != null ? materiaDto.getActivo() : true);
        
        // Guardar en la base de datos
        MateriasEntity materiaGuardada = materiasRepository.save(materia);
        
        // Convertir y retornar el DTO
        return convertToDto(materiaGuardada);
    }

    private MateriasDto convertToDto(MateriasEntity materia) {
        MateriasDto materiaDto = new MateriasDto();
        BeanUtils.copyProperties(materia, materiaDto);
        return materiaDto;
    }

    // MÉTODO: Obtener profesores con sus materias
    public List<ProfesorConMateriasDto> getProfesoresConMaterias() {
        // 1. Traer TODOS los profesores del microservicio administrador
        List<ProfesoresDto> profesores = profesorClient.getAllProfesores();

        // 2. Traer TODAS las asignaciones locales
        List<ProfesorMateriasEntity> asignaciones = profesorMateriasRepository.findAll();

        // 3. Mapear profesores con sus materias
        return profesores.stream()
                .map(profesor -> {
                    ProfesorConMateriasDto dto = new ProfesorConMateriasDto();
                    dto.setProfesorId(profesor.getId());
                    dto.setNombre(profesor.getNombre());
                    dto.setEmail(profesor.getEmail());

                    List<MateriasDto> materiasDelProf = asignaciones.stream()
                            .filter(a -> a.getProfesorId().equals(profesor.getId()))
                            .map(a -> convertToDto(a.getMateria()))
                            .toList();

                    dto.setMaterias(materiasDelProf);
                    return dto;
                })
                .filter(dto -> !dto.getMaterias().isEmpty())
                .toList();
    }
}