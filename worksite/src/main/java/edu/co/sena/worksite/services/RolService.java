package edu.co.sena.worksite.services;

import edu.co.sena.worksite.dtos.*;
import edu.co.sena.worksite.entities.RolEntity;
import edu.co.sena.worksite.exceptions.ResourceNotFoundException;
import edu.co.sena.worksite.respositories.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RolService {

    @Autowired
    private RolRepository repository;

    public boolean create(RolRequestDto dto){
        repository.save(dtoToEntity(dto));
        return true;
    }

    public List<RolListResponseDto> getAll(){
        List<RolListResponseDto> list = new ArrayList<>();
        for(RolEntity e: repository.findAll()){
            list.add(entityToDto(e));
        }
        return list;
    }

    public RolListResponseDto getDetail(long id){
        return entityToDto(validateIfExistsById(id));
    }

    public RolEntity validateIfExistsById(long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe rol con id: " + id));
    }

    public boolean update(long id, RolRequestDto dto){
        RolEntity e = validateIfExistsById(id);
        e.setRolNombre(dto.getRolNombre());
        repository.save(e);
        return true;
    }

    public void delete(long id){
        repository.delete(validateIfExistsById(id));
    }

    public RolListResponseDto entityToDto(RolEntity e){
        return RolListResponseDto.builder()
                .id(e.getId())
                .getRolNombre(e.getRolNombre())
                .build();
    }

    public RolEntity dtoToEntity(RolRequestDto dto){
        return RolEntity.builder()
                .rolNombre(dto.getRolNombre())
                .build();
    }
}
