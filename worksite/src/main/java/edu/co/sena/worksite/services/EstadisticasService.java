package edu.co.sena.worksite.services;

import edu.co.sena.worksite.dtos.EstadisticasResponseDto;
import edu.co.sena.worksite.respositories.OfertaRepository;
import edu.co.sena.worksite.respositories.PostulacionRepository;
import edu.co.sena.worksite.respositories.ResgistroUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class    EstadisticasService {

    @Autowired
    private ResgistroUsuarioRepository usuarioRepository;

    @Autowired
    private PostulacionRepository postulacionRepository;

    @Autowired
    private OfertaRepository ofertaRepository;

    public EstadisticasResponseDto getEstadisticas() {

        long totalUsuarios = usuarioRepository.count();
        long totalCandidatos = usuarioRepository.countByRolNombre_RolNombre("CANDIDATO");
        long totalEmpresas = usuarioRepository.countByRolNombre_RolNombre("EMPRESA");

        long totalPostulaciones = postulacionRepository.count();
        long aceptadas = postulacionRepository.countByEstadoPostulacion("ACEPTADO");
        long rechazadas = postulacionRepository.countByEstadoPostulacion("RECHAZADO");
        long pendientes = postulacionRepository.countByEstadoPostulacion("PENDIENTE");

        long totalOfertas = ofertaRepository.count();
        long ofertasActivas = ofertaRepository.countByEstado("activa");

        return EstadisticasResponseDto.builder()
                .totalUsuarios(totalUsuarios)
                .totalCandidatos(totalCandidatos)
                .totalEmpresas(totalEmpresas)
                .totalPostulaciones(totalPostulaciones)
                .postulacionesAceptadas(aceptadas)
                .postulacionesRechazadas(rechazadas)
                .postulacionesPendientes(pendientes)
                .totalOfertas(totalOfertas)
                .ofertasActivas(ofertasActivas)
                .build();
    }
}
