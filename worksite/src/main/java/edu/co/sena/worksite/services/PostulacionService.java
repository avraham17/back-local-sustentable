package edu.co.sena.worksite.services;

import edu.co.sena.worksite.dtos.*;
import edu.co.sena.worksite.entities.OfertaEntity;
import edu.co.sena.worksite.entities.PostulacionEntity;
import edu.co.sena.worksite.entities.ResgistroUsuarioEntity;
import edu.co.sena.worksite.exceptions.DuplicatePostulacionException;
import edu.co.sena.worksite.exceptions.ResourceNotFoundException;
import edu.co.sena.worksite.respositories.OfertaRepository;
import edu.co.sena.worksite.respositories.PostulacionRepository;
import edu.co.sena.worksite.respositories.ResgistroUsuarioRepository;
import edu.co.sena.worksite.security.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostulacionService {

    @Autowired
    private PostulacionRepository repository;

    @Autowired
    private OfertaRepository ofertaRepository;

    @Autowired
    private ResgistroUsuarioRepository candidatoRepository;

    @Autowired
    private EmailService emailService;

    public boolean create(PostulacionRequestDto dto){
        OfertaEntity oferta = validateOfertaExists(dto.getIdOferta());
        ResgistroUsuarioEntity candidato = validateCandidatoExists(dto.getIdCandidato());

        if (repository.existsByOfertaAndCandidato(oferta, candidato)) {
            throw new DuplicatePostulacionException(
                    "Ya te has postulado a esta oferta. No puedes postularte dos veces a la misma vacante."
            );
        }

        PostulacionEntity entity = PostulacionEntity.builder()
                .oferta(oferta)
                .candidato(candidato)
                .fechaPostulacion(dto.getFechaPostulacion())
                .estadoPostulacion(dto.getEstadoPostulacion())
                .build();

        repository.save(entity);

        String cuerpoCandidato = EmailTemplateBuilder.construir(
                "Postulacion recibida",
                "Hola <strong>" + candidato.getNombres() + "</strong>,",
                "Hemos recibido tu postulacion a la oferta: <strong>" + oferta.getTitulo() + "</strong>. Te notificaremos por este medio cuando haya novedades.",
                "#1e3a8a"
        );

        emailService.enviarCorreoHtml(candidato.getCorreoElectronico(), "Postulación recibida", cuerpoCandidato);

        // Correo nuevo: notificar a la empresa
        if (oferta.getEmpresa() != null && oferta.getEmpresa().getCorreo() != null) {
            String cuerpoEmpresa = EmailTemplateBuilder.construir(
                    "Nuevo candidato postulado",
                    "Hola <strong>" + oferta.getEmpresa().getNombre() + "</strong>,",
                    "El candidato <strong>" + candidato.getNombres() + " " + candidato.getApellidos() + "</strong> se ha postulado a tu oferta: <strong>" + oferta.getTitulo() + "</strong>. Ingresa a WorkSite para revisar su perfil.",
                    "#1e3a8a"
            );
            emailService.enviarCorreoHtml(oferta.getEmpresa().getCorreo(), "Nuevo candidato postulado", cuerpoEmpresa);
        }
        return true;
    }

    public List<PostulacionListResponseDto> getAll(){
        List<PostulacionListResponseDto> list = new ArrayList<>();
        for(PostulacionEntity e: repository.findAll()){
            list.add(entityToDto(e));
        }
        return list;
    }

    public List<PostulacionListResponseDto> getByOferta(long idOferta){
        List<PostulacionListResponseDto> list = new ArrayList<>();
        for(PostulacionEntity e: repository.findByOferta_Id((int) idOferta)){
            list.add(entityToDto(e));
        }
        return list;
    }

    public List<PostulacionListResponseDto> getByCandidato(long idCandidato){
        List<PostulacionListResponseDto> list = new ArrayList<>();
        for(PostulacionEntity e: repository.findByCandidato_Id((int) idCandidato)){
            list.add(entityToDto(e));
        }
        return list;
    }

    public PostulacionListResponseDto getDetail(long id){
        return entityToDto(validateIfExistsById(id));
    }

    public PostulacionEntity validateIfExistsById(long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe postulacion con id: " + id));
    }

    public OfertaEntity validateOfertaExists(int idOferta){
        return ofertaRepository.findById((long) idOferta)
                .orElseThrow(() -> new ResourceNotFoundException("No existe oferta con id: " + idOferta));
    }

    public ResgistroUsuarioEntity validateCandidatoExists(int idCandidato){
        return candidatoRepository.findById(idCandidato)
                .orElseThrow(() -> new ResourceNotFoundException("No existe candidato con id: " + idCandidato));
    }

    public boolean update(long id, PostulacionRequestDto dto){
        PostulacionEntity e = validateIfExistsById(id);
        validateEsPropietarioDeOferta(e);

        String estadoAnterior = e.getEstadoPostulacion();
        String estadoNuevo = dto.getEstadoPostulacion();
        e.setEstadoPostulacion(estadoNuevo);
        repository.save(e);

        if (estadoNuevo != null && !estadoNuevo.equalsIgnoreCase(estadoAnterior)) {
            notificarCambioEstado(e, estadoNuevo);
        }

        return true;
    }

    private void notificarCambioEstado(PostulacionEntity e, String estadoNuevo) {
        String correo = e.getCandidato().getCorreoElectronico();
        String nombre = e.getCandidato().getNombres();
        String tituloOferta = e.getOferta().getTitulo();

        if ("ACEPTADO".equalsIgnoreCase(estadoNuevo)) {
            emailService.enviarCorreoHtml(
                    correo,
                    "¡Felicidades! Fuiste seleccionado",
                    "Hola " + nombre + ",\n\n" +
                            "Has sido aceptado para el puesto: " + tituloOferta + ".\n" +
                            "Pronto se pondrán en contacto contigo con más detalles.\n\n" +
                            "Saludos,\nEl equipo de Worksite"
            );
        } else if ("RECHAZADO".equalsIgnoreCase(estadoNuevo)) {
            emailService.enviarCorreoHtml(
                    correo,
                    "Actualización sobre tu postulación",
                    "Hola " + nombre + ",\n\n" +
                            "Gracias por tu interés en el puesto: " + tituloOferta + ".\n" +
                            "En esta ocasión no continuarás en el proceso, pero te invitamos a seguir postulando.\n\n" +
                            "Saludos,\nEl equipo de Worksite"
            );
        }
    }


    private void validateEsPropietarioDeOferta(PostulacionEntity postulacion){
        if (AuthUtils.tieneRol("ADMIN")) return;

        Integer userId = AuthUtils.getCurrentUserId();
        var empresa = postulacion.getOferta() != null ? postulacion.getOferta().getEmpresa() : null;

        if (userId == null || empresa == null || empresa.getIdUsuario() != userId) {
            throw new AccessDeniedException("No tienes permiso para modificar esta postulación");
        }
    }

    public void delete(long id){
        repository.delete(validateIfExistsById(id));
    }

    public PostulacionListResponseDto entityToDto(PostulacionEntity e){
        return PostulacionListResponseDto.builder()
                .idPostulacion(e.getIdPostulacion())
                .oferta(e.getOferta() != null ? e.getOferta().getTitulo() : null)
                .idOferta(e.getOferta() != null ? e.getOferta().getId() : 0)
                .candidato(e.getCandidato() != null ? e.getCandidato().getNombres() + " " + e.getCandidato().getApellidos() : null)
                .correoCandidato(e.getCandidato() != null ? e.getCandidato().getCorreoElectronico() : null)
                .idCandidato(e.getCandidato() != null ? e.getCandidato().getId() : 0)
                .fechaPostulacion(e.getFechaPostulacion())
                .estadoPostulacion(e.getEstadoPostulacion())
                .build();
    }
}