package edu.co.sena.worksite.services;

import edu.co.sena.worksite.dtos.*;
import edu.co.sena.worksite.entities.EmpresaEntity;
import edu.co.sena.worksite.entities.OfertaEntity;
import edu.co.sena.worksite.entities.ResgistroUsuarioEntity;
import edu.co.sena.worksite.exceptions.ResourceNotFoundException;
import edu.co.sena.worksite.respositories.EmpresaRepository;
import edu.co.sena.worksite.respositories.OfertaRepository;
import edu.co.sena.worksite.respositories.ResgistroUsuarioRepository;
import edu.co.sena.worksite.security.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OfertaService {

    @Autowired
    private OfertaRepository repository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ResgistroUsuarioRepository resgistroUsuarioRepository;

    @Autowired
    private EmailService emailService;

    public boolean create(OfertaRequestDto dto){
        OfertaEntity entity = repository.save(dtoToEntity(dto));

        EmpresaEntity empresa = entity.getEmpresa();
        if (empresa != null && empresa.getCorreo() != null) {
            String cuerpoEmpresa = EmailTemplateBuilder.construir(
                    "Oferta publicada con éxito",
                    "Hola <strong>" + empresa.getNombre() + "</strong>,",
                    "Tu oferta de empleo <strong>" + entity.getTitulo() + "</strong> ha sido publicada exitosamente en WorkSite y ya está visible para los candidatos.",
                    "#1e3a8a"
            );
            emailService.enviarCorreoHtml(empresa.getCorreo(), "Oferta publicada con éxito", cuerpoEmpresa);
        }

        notificarCandidatosCompatibles(entity);

        return true;
    }

    private void notificarCandidatosCompatibles(OfertaEntity oferta) {
        if (oferta.getTitulo() == null || oferta.getTitulo().isBlank()) return;

        String tituloOferta = oferta.getTitulo().toLowerCase();

        List<ResgistroUsuarioEntity> todosLosCandidatos = resgistroUsuarioRepository.findAll();

        for (ResgistroUsuarioEntity candidato : todosLosCandidatos) {
            if (candidato.getCargo() == null || candidato.getCorreoElectronico() == null) continue;

            String cargoCandidato = candidato.getCargo().trim().toLowerCase();
            if (cargoCandidato.isEmpty()) continue;

            if (tituloOferta.contains(cargoCandidato)) {
                String cuerpo = EmailTemplateBuilder.construir(
                        "Nueva oferta que podría interesarte",
                        "Hola <strong>" + candidato.getNombres() + "</strong>,",
                        "Hay una nueva oferta de empleo que coincide con tu perfil: <strong>" + oferta.getTitulo() + "</strong>" +
                                (oferta.getEmpresa() != null ? " en <strong>" + oferta.getEmpresa().getNombre() + "</strong>" : "") +
                                ". Ingresa a WorkSite para conocer más detalles y postularte.",
                        "#1e3a8a"
                );

                emailService.enviarCorreoHtml(
                        candidato.getCorreoElectronico(),
                        "Nueva oferta que podría interesarte: " + oferta.getTitulo(),
                        cuerpo
                );
            }
        }
    }

    public List<OfertaListResponseDto> getAll(){
        List<OfertaListResponseDto> list = new ArrayList<>();
        for(OfertaEntity e: repository.findAll()){
            list.add(entityToDto(e));
        }
        return list;
    }

    public List<OfertaListResponseDto> getByEmpresa(long idEmpresa){
        List<OfertaListResponseDto> list = new ArrayList<>();
        for(OfertaEntity e: repository.findByEmpresa_Id((int) idEmpresa)){
            list.add(entityToDto(e));
        }
        return list;
    }

    public OfertaListResponseDto getDetail(long id){
        return entityToDto(validateIfExistsById(id));
    }

    public OfertaEntity validateIfExistsById(long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe oferta con id: " + id));
    }

    public EmpresaEntity validateEmpresaExists(int idEmpresa){
        return empresaRepository.findById((long) idEmpresa)
                .orElseThrow(() -> new ResourceNotFoundException("No existe empresa con id: " + idEmpresa));
    }


    private void validateEsPropietario(OfertaEntity oferta){
        if (AuthUtils.tieneRol("ADMIN")) return;

        Integer userId = AuthUtils.getCurrentUserId();
        if (userId == null || oferta.getEmpresa() == null || oferta.getEmpresa().getIdUsuario() != userId) {
            throw new AccessDeniedException("No tienes permiso para modificar esta oferta");
        }
    }

    public boolean update(long id, OfertaRequestDto dto){
        OfertaEntity e = validateIfExistsById(id);
        validateEsPropietario(e);

        e.setTitulo(dto.getTitulo());
        e.setDescripcion(dto.getDescripcion());
        e.setEmpresa(validateEmpresaExists(dto.getIdEmpresa()));
        e.setSalario(dto.getSalario());
        e.setFechaDePublicacion(dto.getFechaDePublicacion());
        e.setUbicacion(dto.getUbicacion());
        e.setEstado(dto.getEstado());
        e.setSector(dto.getSector());
        e.setModalidad(dto.getModalidad());
        e.setResponsabilidades(dto.getResponsabilidades());
        e.setRequisitos(dto.getRequisitos());
        e.setJornada(dto.getJornada());
        e.setTipoDeContrato(dto.getTipoDeContrato());
        e.setExperiencia(dto.getExperiencia());
        e.setNivelEducativo(dto.getNivelEducativo());
        e.setNumOfertas(dto.getNumOfertas());
        e.setFechaDeCierre(dto.getFechaDeCierre());

        repository.save(e);
        return true;
    }

    public void delete(long id){
        OfertaEntity e = validateIfExistsById(id);
        validateEsPropietario(e);
        repository.delete(e);
    }

    public OfertaListResponseDto entityToDto(OfertaEntity e){
        return OfertaListResponseDto.builder()
                .id(e.getId())
                .titulo(e.getTitulo())
                .descripcion(e.getDescripcion())
                .empresa(e.getEmpresa() != null ? e.getEmpresa().getNombre() : null)
                .idEmpresa(e.getEmpresa() != null ? e.getEmpresa().getId() : 0)
                .salario(e.getSalario())
                .fechaDePublicacion(e.getFechaDePublicacion())
                .ubicacion(e.getUbicacion())
                .estado(e.getEstado())
                .sector(e.getSector())
                .modalidad(e.getModalidad())
                .responsabilidades(e.getResponsabilidades())
                .requisitos(e.getRequisitos())
                .jornada(e.getJornada())
                .tipoDeContrato(e.getTipoDeContrato())
                .experiencia(e.getExperiencia())
                .nivelEducativo(e.getNivelEducativo())
                .numOfertas(e.getNumOfertas())
                .fechaDeCierre(e.getFechaDeCierre())
                .build();
    }

    public OfertaEntity dtoToEntity(OfertaRequestDto dto){
        EmpresaEntity empresa = validateEmpresaExists(dto.getIdEmpresa());

        return OfertaEntity.builder()
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .empresa(empresa)
                .salario(dto.getSalario())
                .fechaDePublicacion(dto.getFechaDePublicacion())
                .ubicacion(dto.getUbicacion())
                .estado(dto.getEstado())
                .sector(dto.getSector())
                .modalidad(dto.getModalidad())
                .responsabilidades(dto.getResponsabilidades())
                .requisitos(dto.getRequisitos())
                .jornada(dto.getJornada())
                .tipoDeContrato(dto.getTipoDeContrato())
                .experiencia(dto.getExperiencia())
                .nivelEducativo(dto.getNivelEducativo())
                .numOfertas(dto.getNumOfertas())
                .fechaDeCierre(dto.getFechaDeCierre())
                .build();
    }
}