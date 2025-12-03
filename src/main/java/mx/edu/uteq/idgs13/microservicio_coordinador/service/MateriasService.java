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

    private MateriasDto convertToDto(MateriasEntity materia) {
        MateriasDto materiaDto = new MateriasDto();
        BeanUtils.copyProperties(materia, materiaDto);
        return materiaDto;
    }

    // NUEVO MÉTODO: Obtener profesores con sus materias
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
                    .filter(a -> a.getProfesorId() != null && a.getProfesorId().equals(profesor.getId()))
                    .map(a -> convertToDto(a.getMateria()))
                    .collect(java.util.stream.Collectors.toList());

                    dto.setMaterias(materiasDelProf);
                    return dto;
                })
            .filter(dto -> dto.getMaterias() != null && !dto.getMaterias().isEmpty()) // opcional: solo profesores con materias
            .collect(java.util.stream.Collectors.toList());
    }

    // Crear materia
    public MateriasDto crearMateria(MateriasDto dto) {
        MateriasEntity entity = new MateriasEntity();
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setActivo(Boolean.TRUE.equals(dto.getActivo()));
        MateriasEntity saved = materiasRepository.save(entity);
        return convertToDto(saved);
    }

    // Editar materia
    public MateriasDto editarMateria(MateriasDto dto) {
        Optional<MateriasEntity> opt = materiasRepository.findById(dto.getId());
        if (opt.isEmpty()) return null;
        MateriasEntity entity = opt.get();
        if (dto.getNombre() != null) entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setActivo(Boolean.TRUE.equals(dto.getActivo()));
        MateriasEntity saved = materiasRepository.save(entity);
        return convertToDto(saved);
    }

    // Eliminar materia
    public boolean eliminarMateria(Long id) {
        if (!materiasRepository.existsById(id)) return false;
        // Borrar asignaciones relacionadas primero para evitar referencias
        List<ProfesorMateriasEntity> asignaciones = profesorMateriasRepository.findByMateriaId(id);
        if (asignaciones != null && !asignaciones.isEmpty()) {
            profesorMateriasRepository.deleteAll(asignaciones);
        }
        materiasRepository.deleteById(id);
        return true;
    }

    // Actualizar estado activo/inactivo
    public MateriasDto actualizarEstado(Long id, boolean activo) {
        Optional<MateriasEntity> opt = materiasRepository.findById(id);
        if (opt.isEmpty()) return null;
        MateriasEntity entity = opt.get();
        entity.setActivo(activo);
        MateriasEntity saved = materiasRepository.save(entity);
        return convertToDto(saved);
    }

    // Asignar profesor a materia
    public MateriasDto asignarProfesor(Long materiaId, Long profesorId) {
        Optional<MateriasEntity> opt = materiasRepository.findById(materiaId);
        if (opt.isEmpty()) return null;

        // Validar que el profesor exista vía cliente (opcional)
        try {
            List<ProfesoresDto> profesores = profesorClient.getAllProfesores();
            boolean existe = profesores.stream().anyMatch(p -> p.getId().equals(profesorId));
            if (!existe) return null;
        } catch (Exception e) {
            // Si falla la validación remota, continuar bajo responsabilidad del coordinador
        }

        // Crear/actualizar relación en tabla profesor_materias
        // Regla: una materia puede tener un profesor asignado (última asignación válida)
        // Limpiar asignaciones previas de esta materia
        List<ProfesorMateriasEntity> previas = profesorMateriasRepository.findByMateriaId(materiaId);
        if (previas != null && !previas.isEmpty()) {
            profesorMateriasRepository.deleteAll(previas);
        }
        ProfesorMateriasEntity relacion = new ProfesorMateriasEntity();
        relacion.setProfesorId(profesorId);
        relacion.setMateria(opt.get());
        relacion.setActivo(true);
        profesorMateriasRepository.save(relacion);

        return convertToDto(opt.get());
    }
}