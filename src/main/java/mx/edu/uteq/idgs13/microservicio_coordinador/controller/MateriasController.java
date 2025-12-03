package mx.edu.uteq.idgs13.microservicio_coordinador.controller;

import mx.edu.uteq.idgs13.microservicio_coordinador.dto.MateriasDto;
import mx.edu.uteq.idgs13.microservicio_coordinador.dto.ProfesorConMateriasDto;
import mx.edu.uteq.idgs13.microservicio_coordinador.service.MateriasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
public class MateriasController {

    @Autowired
    private MateriasService materiasService;

    @GetMapping
    public ResponseEntity<List<MateriasDto>> getAllMaterias() {
        List<MateriasDto> materias = materiasService.getAllMaterias();
        return ResponseEntity.ok(materias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MateriasDto> getMateriaById(@PathVariable Long id) {
        MateriasDto materia = materiasService.getMateriaById(id);
        return materia != null ? ResponseEntity.ok(materia) : ResponseEntity.notFound().build();
    }

    // NUEVO: Crear materia
    @PostMapping
    public ResponseEntity<MateriasDto> createMateria(@RequestBody MateriasDto materiaDto) {
        MateriasDto nuevaMateria = materiasService.createMateria(materiaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMateria);
    }

    // ENDPOINT: Obtener profesores con sus materias
    @GetMapping("/profesores-con-materias")
    public ResponseEntity<List<ProfesorConMateriasDto>> getProfesoresConMaterias() {
        return ResponseEntity.ok(materiasService.getProfesoresConMaterias());
    }

    // Crear materia
    @PostMapping
    public ResponseEntity<MateriasDto> crearMateria(@RequestBody MateriasDto materiaDto) {
        MateriasDto creada = materiasService.crearMateria(materiaDto);
        return ResponseEntity.status(201).body(creada);
    }

    // Editar materia
    @PutMapping("/{id}")
    public ResponseEntity<MateriasDto> editarMateria(@PathVariable Long id, @RequestBody MateriasDto materiaDto) {
        MateriasDto existente = materiasService.getMateriaById(id);
        if (existente == null) return ResponseEntity.notFound().build();
        materiaDto.setId(id);
        MateriasDto editada = materiasService.editarMateria(materiaDto);
        return ResponseEntity.ok(editada);
    }

    // Eliminar materia
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMateria(@PathVariable Long id) {
        boolean ok = materiasService.eliminarMateria(id);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // Habilitar/Deshabilitar materia
    @PatchMapping("/{id}/status")
    public ResponseEntity<MateriasDto> actualizarEstado(@PathVariable Long id, @RequestParam boolean activo) {
        MateriasDto actualizada = materiasService.actualizarEstado(id, activo);
        return actualizada != null ? ResponseEntity.ok(actualizada) : ResponseEntity.notFound().build();
    }

    // Asignar materia a un profesor
    @PostMapping("/{id}/asignar")
    public ResponseEntity<MateriasDto> asignarProfesor(@PathVariable Long id, @RequestParam Long profesorId) {
        MateriasDto resultado = materiasService.asignarProfesor(id, profesorId);
        return resultado != null ? ResponseEntity.ok(resultado) : ResponseEntity.badRequest().build();
    }
}