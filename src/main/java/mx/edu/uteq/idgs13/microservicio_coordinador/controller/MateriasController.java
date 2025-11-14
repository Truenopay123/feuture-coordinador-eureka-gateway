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
@CrossOrigin(origins = "*")
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
}