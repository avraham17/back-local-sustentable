package edu.co.sena.worksite.services;

import edu.co.sena.worksite.dtos.*;
import edu.co.sena.worksite.entities.EmpresaEntity;
import edu.co.sena.worksite.exceptions.ResourceNotFoundException;
import edu.co.sena.worksite.respositories.EmpresaRepository;
import edu.co.sena.worksite.security.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository repository;

    public EmpresaEntity create(EmpresaRequestDto dto) {
        return repository.save(dtoToEntity(dto));
    }

    public List<EmpresaListResponseDto> getAll(){
        List<EmpresaListResponseDto> list = new ArrayList<>();
        for(EmpresaEntity e: repository.findAll()){
            list.add(entityToDto(e));
        }
        return list;
    }

    public EmpresaListResponseDto getDetail(long id){
        return entityToDto(validateIfExistsById(id));
    }

    public EmpresaListResponseDto getByUsuario(long idUsuario){
        EmpresaEntity e = repository.findByIdUsuario((int) idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Este usuario no tiene un perfil empresarial creado"));
        return entityToDto(e);
    }

    public EmpresaEntity validateIfExistsById(long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe empresa con id: " + id));
    }

    /** Verifica que el usuario autenticado sea dueño de esta empresa (o sea ADMIN). */
    private void validateEsPropietario(EmpresaEntity empresa){
        if (AuthUtils.tieneRol("ADMIN")) return;

        Integer userId = AuthUtils.getCurrentUserId();
        if (userId == null || empresa.getIdUsuario() != userId) {
            throw new AccessDeniedException("No tienes permiso para modificar este perfil empresarial");
        }
    }

    public boolean update(long id, EmpresaRequestDto dto){
        EmpresaEntity e = validateIfExistsById(id);
        validateEsPropietario(e);

        e.setNombre(dto.getNombre());
        e.setSector(dto.getSector());
        e.setUbicacion(dto.getUbicacion());
        e.setTelefono(dto.getTelefono());
        e.setCorreo(dto.getCorreo());

        repository.save(e);
        return true;
    }

    public void delete(long id){
        EmpresaEntity e = validateIfExistsById(id);
        validateEsPropietario(e);
        repository.delete(e);
    }

    public boolean updateFoto(long id, String foto){
        EmpresaEntity e = validateIfExistsById(id);
        validateEsPropietario(e);
        e.setFoto(foto);
        repository.save(e);
        return true;
    }

    public EmpresaListResponseDto entityToDto(EmpresaEntity e){
        return EmpresaListResponseDto.builder()
                .id(e.getId())
                .nombre(e.getNombre())
                .sector(e.getSector())
                .ubicacion(e.getUbicacion())
                .telefono(e.getTelefono())
                .correo(e.getCorreo())
                .idUsuario(e.getIdUsuario())
                .foto(e.getFoto())
                .build();
    }

    public EmpresaEntity dtoToEntity(EmpresaRequestDto dto){
        return EmpresaEntity.builder()
                .nombre(dto.getNombre())
                .sector(dto.getSector())
                .ubicacion(dto.getUbicacion())
                .telefono(dto.getTelefono())
                .correo(dto.getCorreo())
                .idUsuario(dto.getIdUsuario() != null ? dto.getIdUsuario() : 0)
                .build();
    }

    public void deleteByCorreo(String correo){
        repository.delete(validateIfExistsByCorreo(correo));
    }

    public EmpresaEntity validateIfExistsByCorreo(String correo){
        EmpresaEntity e = repository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("No existe empresa con correo: " + correo));
        validateEsPropietario(e);
        return e;
    }
}