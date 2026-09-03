package edu.co.sena.worksite.controllers;

import edu.co.sena.worksite.dtos.*;
import edu.co.sena.worksite.entities.EmpresaEntity;
import edu.co.sena.worksite.services.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/empresa")
public class EmpresaController {

    @Autowired
    EmpresaService service;

    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @PostMapping
    public ResponseDto<EmpresaGeneralResponseDto> crear(@RequestBody @Validated EmpresaRequestDto request) {
        EmpresaEntity empresa = service.create(request);
        return ResponseDto.<EmpresaGeneralResponseDto>builder()
                .data(EmpresaGeneralResponseDto.builder()
                        .successful(true)
                        .id(empresa.getId())
                        .build())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseDto<List<EmpresaListResponseDto>> getAll() {
        return ResponseDto.<List<EmpresaListResponseDto>>builder()
                .data(service.getAll())
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseDto<EmpresaListResponseDto> getByUsuario(@PathVariable long idUsuario) {
        return ResponseDto.<EmpresaListResponseDto>builder()
                .data(service.getByUsuario(idUsuario))
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseDto<EmpresaListResponseDto> getDetail(@PathVariable long id) {
        return ResponseDto.<EmpresaListResponseDto>builder()
                .data(service.getDetail(id))
                .build();
    }

    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseDto<GeneralResponseDto> update(@PathVariable long id,
                                                  @RequestBody @Validated EmpresaRequestDto dto) {
        boolean response = service.update(id, dto);
        return ResponseDto.<GeneralResponseDto>builder()
                .data(GeneralResponseDto.builder().successful(response).build())
                .build();
    }

    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @PutMapping("/{id}/foto")
    public ResponseDto<GeneralResponseDto> updateFoto(@PathVariable long id,
                                                      @RequestBody UpdateFotoRequestDto dto) {
        boolean response = service.updateFoto(id, dto.getFoto());
        return ResponseDto.<GeneralResponseDto>builder()
                .data(GeneralResponseDto.builder().successful(response).build())
                .build();
    }

    @PreAuthorize("hasAnyRole('EMPRESA', 'ADMIN')")
    @DeleteMapping("/correo/{correo}")
    public ResponseDto<GeneralResponseDto> deleteByCorreo(@PathVariable("correo") String correo) {
        this.service.deleteByCorreo(correo);
        return ResponseDto.<GeneralResponseDto>builder()
                .data(GeneralResponseDto.builder()
                        .successful(true)
                        .build())
                .build();
    }
}