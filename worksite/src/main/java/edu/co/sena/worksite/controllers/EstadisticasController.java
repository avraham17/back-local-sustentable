package edu.co.sena.worksite.controllers;

import edu.co.sena.worksite.dtos.*;
import edu.co.sena.worksite.services.EstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {

    @Autowired
    EstadisticasService service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseDto<EstadisticasResponseDto> getEstadisticas() {
        return ResponseDto.<EstadisticasResponseDto>builder()
                .data(service.getEstadisticas())
                .build();
    }
}
