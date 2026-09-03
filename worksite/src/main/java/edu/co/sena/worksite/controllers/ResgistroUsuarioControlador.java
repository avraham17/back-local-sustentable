package edu.co.sena.worksite.controllers;

import edu.co.sena.worksite.dtos.*;
import edu.co.sena.worksite.services.ResgistroUsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ResgistroUsuario")

public class ResgistroUsuarioControlador {

    @Autowired
    ResgistroUsuarioService service;

    @PostMapping()
    public ResponseDto<ResgistroUsuarioResponseDto>
    crear(
            @RequestBody @Validated
            ResgistroUsuarioRequestDto request
    ) {
        ResgistroUsuarioResponseDto response = service.create(request);

        return ResponseDto.<ResgistroUsuarioResponseDto>builder()
                .data(response)
                .build();
    }
    
    @PostMapping("/login")
    public ResponseDto<ResgistroUsuarioListResponseDto> login(@RequestBody @Validated DtoLogin dto) {
        return ResponseDto.<ResgistroUsuarioListResponseDto>builder()
                .data(service.login(dto.getCorreo(), dto.getContrasenia()))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseDto<List<ResgistroUsuarioListResponseDto>> getAll() {

        List<ResgistroUsuarioListResponseDto> response = this.service.getAll();

        return ResponseDto.<List<ResgistroUsuarioListResponseDto>>builder()
                .data(response)
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseDto<ResgistroUsuarioListResponseDto> getDetail(@PathVariable("id") long id) {
        ResgistroUsuarioListResponseDto response = this.service.getDetail(id);
        return ResponseDto.<ResgistroUsuarioListResponseDto>builder()
                .data(response)
                .build();

    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseDto<ResgistroUsuarioResponseDto> update
            (@PathVariable("id") long id, @RequestBody @Validated ResgistroUsuarioRequestDto dto) {

        boolean response = this.service.update(id, dto);

        return ResponseDto.<ResgistroUsuarioResponseDto>builder()
                .data(ResgistroUsuarioResponseDto.builder()
                        .isCreated(response)
                        .build())
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/contrasenia")
    public ResponseDto<ResgistroUsuarioResponseDto> updateContrasenia
            (@PathVariable("id") long id, @RequestBody @Validated UpdateContraseniaRequestDto dto) {

        boolean response = this.service.updateContrasenia(id, dto.getContraseniaActual(), dto.getContraseniaNueva());

        return ResponseDto.<ResgistroUsuarioResponseDto>builder()
                .data(ResgistroUsuarioResponseDto.builder()
                        .isCreated(response)
                        .build())
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/foto")
    public ResponseDto<ResgistroUsuarioResponseDto> updateFoto
            (@PathVariable("id") long id, @RequestBody UpdateFotoRequestDto dto) {

        boolean response = this.service.updateFoto(id, dto.getFoto());

        return ResponseDto.<ResgistroUsuarioResponseDto>builder()
                .data(ResgistroUsuarioResponseDto.builder()
                        .isCreated(response)
                        .build())
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/cv")
    public ResponseDto<ResgistroUsuarioResponseDto> updateCv
            (@PathVariable("id") long id, @RequestBody UpdateCvRequestDto dto) {

        boolean response = this.service.updateCv(id, dto.getCv());

        return ResponseDto.<ResgistroUsuarioResponseDto>builder()
                .data(ResgistroUsuarioResponseDto.builder()
                        .isCreated(response)
                        .build())
                .build();
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseDto<GeneralResponseDto> delete(@PathVariable("id") long id) {
        this.service.delete(id);
        return ResponseDto.<GeneralResponseDto>builder()
                .data(GeneralResponseDto.builder()
                        .successful(true)
                        .build())
                .build();
    }

    @PreAuthorize("isAuthenticated()")
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
