package edu.co.sena.worksite.services;

import edu.co.sena.worksite.dtos.*;
import edu.co.sena.worksite.entities.EmpresaEntity;
import edu.co.sena.worksite.entities.OfertaEntity;
import edu.co.sena.worksite.entities.ResgistroUsuarioEntity;
import edu.co.sena.worksite.respositories.EmpresaRepository;
import edu.co.sena.worksite.respositories.OfertaRepository;
import edu.co.sena.worksite.respositories.PostulacionRepository;
import edu.co.sena.worksite.respositories.ResgistroUsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminPerfilesService {


    @Autowired
    private PostulacionRepository postulacionRepository;

    public List<PostulacionResumenDto> listarPostulaciones() {
        return postulacionRepository.listarPostulaciones();
    }

    @Autowired
    private ResgistroUsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private OfertaRepository ofertaRepository;

    public List<CandidatoResumenDto> listarCandidatos() {
        return usuarioRepository.listarCandidatos();
    }

    public CandidatoDetalleDto obtenerCandidato(int id) {
        ResgistroUsuarioEntity u = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidato no encontrado"));

        return CandidatoDetalleDto.builder()
                .id(u.getId())
                .nombres(u.getNombres())
                .apellidos(u.getApellidos())
                .correoElectronico(u.getCorreoElectronico())
                .numeroTelefonico(u.getNumeroTelefonico())
                .ciudad(u.getCiudad())
                .cargo(u.getCargo())
                .estudio(u.getEstudio())
                .descripcion(u.getDescripcion())
                .anosExperiencia(u.getAnosExperiencia())
                .foto(u.getFoto())
                .build();
    }

    public List<EmpresaResumenDto> listarEmpresas() {
        return empresaRepository.listarEmpresas();
    }

    public EmpresaEntity obtenerEmpresa(long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada"));
    }

    public List<OfertaResumenDto> listarOfertas() {
        return ofertaRepository.listarOfertas();
    }

    public OfertaDetalleDto obtenerOferta(long id) {
        OfertaEntity e = ofertaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Oferta no encontrada"));

        OfertaDetalleDto.OfertaDetalleDtoBuilder dto = OfertaDetalleDto.builder()
                .id(e.getId())
                .titulo(e.getTitulo())
                .ubicacion(e.getUbicacion())
                .descripcion(e.getDescripcion())
                .salario(e.getSalario())
                .fechaDePublicacion(e.getFechaDePublicacion())
                .estado(e.getEstado())
                .sector(e.getSector())
                .modalidad(e.getModalidad())
                .responsabilidades(e.getResponsabilidades())
                .requisitos(e.getRequisitos())
                .jornada(e.getJornada())
                .tipoDeContrato(e.getTipoDeContrato())
                .experiencia(e.getExperiencia())
                .nivelEducativo(e.getNivelEducativo())
                .fechaDeCierre(e.getFechaDeCierre())
                .postulaciones(e.getPostulaciones() != null ? e.getPostulaciones().size() : 0);

        if (e.getEmpresa() != null) {
            dto.idEmpresa(e.getEmpresa().getId())
                    .nombreEmpresa(e.getEmpresa().getNombre())
                    .sectorEmpresa(e.getEmpresa().getSector())
                    .ubicacionEmpresa(e.getEmpresa().getUbicacion())
                    .telefonoEmpresa(e.getEmpresa().getTelefono())
                    .correoEmpresa(e.getEmpresa().getCorreo())
                    .fotoEmpresa(e.getEmpresa().getFoto());
        }

        return dto.build();
    }

}
