package edu.co.sena.worksite.controllers;

import edu.co.sena.worksite.dtos.*;
import edu.co.sena.worksite.entities.EmpresaEntity;
import edu.co.sena.worksite.entities.OfertaEntity;
import edu.co.sena.worksite.entities.ResgistroUsuarioEntity;
import edu.co.sena.worksite.services.AdminPerfilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPerfilesController {

    @Autowired
    private AdminPerfilesService adminPerfilesService;

    @GetMapping("/candidatos")
    public List<CandidatoResumenDto> listarCandidatos()
    {
        return adminPerfilesService.listarCandidatos();
    }

    @GetMapping("/candidatos/{id}")
    public CandidatoDetalleDto verCandidato(@PathVariable int id)
    {
        return adminPerfilesService.obtenerCandidato(id);
    }

    @GetMapping("/empresas")
    public List<EmpresaResumenDto> listarEmpresas()
    {
        return adminPerfilesService.listarEmpresas();
    }

    @GetMapping("/empresas/{id}")
    public EmpresaEntity verEmpresa(@PathVariable int id)
    {
        return adminPerfilesService.obtenerEmpresa(id);
    }

    @GetMapping("/ofertas")
    public List<OfertaResumenDto> listarOfertas()
    {
        return adminPerfilesService.listarOfertas();
    }

    @GetMapping("/ofertas/{id}")
    public OfertaDetalleDto verOferta(@PathVariable int id)
    {
        return adminPerfilesService.obtenerOferta(id);
    }

    @GetMapping("/postulaciones")
    public List<PostulacionResumenDto> listarPostulaciones()
    {
        return adminPerfilesService.listarPostulaciones();
    }
}
