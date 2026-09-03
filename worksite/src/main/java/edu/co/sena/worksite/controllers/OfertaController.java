package edu.co.sena.worksite.controllers;

import edu.co.sena.worksite.dtos.*;
import edu.co.sena.worksite.services.OfertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/oferta")
public class OfertaController {

    @Autowired
    OfertaService service;

    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @PostMapping
    public ResponseDto<GeneralResponseDto> crear(@RequestBody @Validated OfertaRequestDto request) {
        boolean response = service.create(request);
        return ResponseDto.<GeneralResponseDto>builder()
                .data(GeneralResponseDto.builder().successful(response).build())
                .build();
    }

    // Pública: cualquiera puede ver las vacantes, con o sin sesión
    @GetMapping
    public ResponseDto<List<OfertaListResponseDto>> getAll() {
        return ResponseDto.<List<OfertaListResponseDto>>builder()
                .data(service.getAll())
                .build();
    }

    @GetMapping("/empresa/{idEmpresa}")
    public ResponseDto<List<OfertaListResponseDto>> getByEmpresa(@PathVariable long idEmpresa) {
        return ResponseDto.<List<OfertaListResponseDto>>builder()
                .data(service.getByEmpresa(idEmpresa))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseDto<OfertaListResponseDto> getDetail(@PathVariable long id) {
        return ResponseDto.<OfertaListResponseDto>builder()
                .data(service.getDetail(id))
                .build();
    }

    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseDto<GeneralResponseDto> update(@PathVariable long id,
                                                  @RequestBody @Validated OfertaRequestDto dto) {
        boolean response = service.update(id, dto);
        return ResponseDto.<GeneralResponseDto>builder()
                .data(GeneralResponseDto.builder().successful(response).build())
                .build();
    }

    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseDto<GeneralResponseDto> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseDto.<GeneralResponseDto>builder()
                .data(GeneralResponseDto.builder().successful(true).build())
                .build();
    }
}