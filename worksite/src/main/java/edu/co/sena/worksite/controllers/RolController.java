package edu.co.sena.worksite.controllers;

import edu.co.sena.worksite.dtos.*;
import edu.co.sena.worksite.services.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/rol")
public class RolController {

    @Autowired
    RolService service;

    @PostMapping
    public ResponseDto<GeneralResponseDto> crear(@RequestBody @Validated RolRequestDto request) {
        boolean response = service.create(request);
        return ResponseDto.<GeneralResponseDto>builder()
                .data(GeneralResponseDto.builder().successful(response).build())
                .build();
    }

    @GetMapping
    public ResponseDto<List<RolListResponseDto>> getAll() {
        return ResponseDto.<List<RolListResponseDto>>builder()
                .data(service.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ResponseDto<RolListResponseDto> getDetail(@PathVariable long id) {
        return ResponseDto.<RolListResponseDto>builder()
                .data(service.getDetail(id))
                .build();
    }

    @PutMapping("/{id}")
    public ResponseDto<GeneralResponseDto> update(@PathVariable long id,
                                                 @RequestBody @Validated RolRequestDto dto) {
        boolean response = service.update(id, dto);
        return ResponseDto.<GeneralResponseDto>builder()
                .data(GeneralResponseDto.builder().successful(response).build())
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseDto<GeneralResponseDto> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseDto.<GeneralResponseDto>builder()
                .data(GeneralResponseDto.builder().successful(true).build())
                .build();
    }
}
