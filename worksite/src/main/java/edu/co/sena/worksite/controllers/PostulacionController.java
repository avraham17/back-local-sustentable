package edu.co.sena.worksite.controllers;

import edu.co.sena.worksite.dtos.*;
import edu.co.sena.worksite.services.PostulacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/postulacion")
public class PostulacionController {

    @Autowired
    PostulacionService service;

    @PreAuthorize("hasAnyRole('CANDIDATO', 'ADMIN')")
    @PostMapping
    public ResponseDto<GeneralResponseDto> crear(@RequestBody @Validated PostulacionRequestDto request) {
        boolean response = service.create(request);
        return ResponseDto.<GeneralResponseDto>builder()
                .data(GeneralResponseDto.builder()
                        .successful(response).build())
                .build();
    }

    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @GetMapping
    public ResponseDto<List<PostulacionListResponseDto>> getAll() {
        return ResponseDto.<List<PostulacionListResponseDto>>builder()
                .data(service.getAll())
                .build();
    }

    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @GetMapping("/oferta/{idOferta}")
    public ResponseDto<List<PostulacionListResponseDto>> getByOferta(@PathVariable long idOferta) {
        return ResponseDto.<List<PostulacionListResponseDto>>builder()
                .data(service.getByOferta(idOferta))
                .build();
    }

    @PreAuthorize("hasAnyRole('CANDIDATO', 'ADMIN')")
    @GetMapping("/candidato/{idCandidato}")
    public ResponseDto<List<PostulacionListResponseDto>> getByCandidato(@PathVariable long idCandidato) {
        return ResponseDto.<List<PostulacionListResponseDto>>builder()
                .data(service.getByCandidato(idCandidato))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseDto<PostulacionListResponseDto> getDetail(@PathVariable long id) {
        return ResponseDto.<PostulacionListResponseDto>builder()
                .data(service.getDetail(id))
                .build();
    }

    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseDto<GeneralResponseDto> update(@PathVariable long id,
                                                  @RequestBody @Validated PostulacionRequestDto dto) {
        boolean response = service.update(id, dto);
        return ResponseDto.<GeneralResponseDto>builder()
                .data(GeneralResponseDto.builder().successful(response).build())
                .build();
    }

    @PreAuthorize("hasAnyRole('EMPRESA', 'CANDIDATO', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseDto<GeneralResponseDto> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseDto.<GeneralResponseDto>builder()
                .data(GeneralResponseDto.builder().successful(true).build())
                .build();
    }
}