package edu.co.sena.worksite.services;

import edu.co.sena.worksite.dtos.*;
import edu.co.sena.worksite.entities.ResgistroUsuarioEntity;
import edu.co.sena.worksite.entities.RolEntity;
import edu.co.sena.worksite.exceptions.ResourceNotFoundException;
import edu.co.sena.worksite.respositories.ResgistroUsuarioRepository;
import edu.co.sena.worksite.respositories.RolRepository;
import edu.co.sena.worksite.security.AuthUtils;
import edu.co.sena.worksite.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class ResgistroUsuarioService {

    @Autowired
    private ResgistroUsuarioRepository repository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // Se define en application.properties (o como variable de entorno), NUNCA
    // en el código ni en el frontend. Si no está configurado, el registro como
    // ADMIN queda bloqueado por seguridad (valor vacío nunca puede "coincidir").
    @Value("${worksite.admin.codigo:}")
    private String codigoAdminEsperado;

    /**
     * Si el registro pide el rol ADMIN, exige que venga un código secreto que
     * coincida con el configurado en el servidor (application.properties /
     * variable de entorno). Así, aunque alguien llame directo a la API (sin
     * pasar por el formulario), no puede crear una cuenta de administrador.
     */
    private void validarCodigoAdminSiAplica(ResgistroUsuarioRequestDto dto) {
        String rolSolicitado = dto.getRolNombre();
        if (rolSolicitado == null || !rolSolicitado.equalsIgnoreCase("ADMIN")) {
            return; // no es un registro de administrador, no aplica esta validación
        }

        if (codigoAdminEsperado == null || codigoAdminEsperado.isBlank()) {
            // No hay código configurado en el servidor: por seguridad, se bloquea
            // el registro como ADMIN en vez de permitirlo "por defecto".
            throw new AccessDeniedException("El registro como administrador no está habilitado");
        }

        if (dto.getCodigoAdmin() == null || !codigoAdminEsperado.equals(dto.getCodigoAdmin())) {
            throw new AccessDeniedException("Código de administrador incorrecto");
        }
    }

    public ResgistroUsuarioResponseDto create(ResgistroUsuarioRequestDto dto){
        validarCodigoAdminSiAplica(dto);

        ResgistroUsuarioEntity entity = repository.save(dtoToEntity(dto));

        String correoHtml = emailService.construirCorreoBienvenida(entity.getNombres());
        emailService.enviarCorreoHtml(
                entity.getCorreoElectronico(),
                "¡Bienvenido a Worksite!",
                correoHtml
        );

        String rolNombre = entity.getRolNombre() != null ? entity.getRolNombre().getRolNombre() : null;
        String token = jwtUtil.generarToken(entity.getId(), entity.getCorreoElectronico(), rolNombre);

        return ResgistroUsuarioResponseDto.builder()
                .isCreated(true)
                .id(entity.getId())
                .token(token)
                .correoElectronico(entity.getCorreoElectronico())
                .rolNombre(rolNombre)
                .build();
    }

    public List<ResgistroUsuarioListResponseDto> getAll(){
        List<ResgistroUsuarioListResponseDto> list = new ArrayList<>();
        for(ResgistroUsuarioEntity e: repository.findAll()){
            list.add(entityToDto(e));
        }
        return list;
    }

    public ResgistroUsuarioListResponseDto getDetail(long id){
        return entityToDto(validateIfExistsById(id));
    }

    public ResgistroUsuarioEntity validateIfExistsById(long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe candidato con id: " + id));
    }


    public ResgistroUsuarioEntity validateIfExistsByCorreo(String correo){
        return repository.findByCorreoElectronico(correo)
                .orElseThrow(() -> new ResourceNotFoundException("No existe candidato con correo: " + correo));
    }

    public ResgistroUsuarioListResponseDto login(String correo, String contrasenia) {
        ResgistroUsuarioEntity entity = repository
                .findByCorreoElectronico(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Correo o contraseña incorrectos"));

        if (!passwordEncoder.matches(contrasenia, entity.getContrasenia())) {
            throw new ResourceNotFoundException("Correo o contraseña incorrectos");
        }

        ResgistroUsuarioListResponseDto dto = entityToDto(entity);
        String token = jwtUtil.generarToken(entity.getId(), entity.getCorreoElectronico(), dto.getRolNombre());
        dto.setToken(token);

        return dto;
    }

    /**
     * Cambia la contraseña de un usuario, validando en el servidor que la
     * contraseña actual enviada coincide con el hash almacenado (nunca se
     * compara texto plano contra el hash desde el frontend).
     */
    public boolean updateContrasenia(long id, String contraseniaActual, String contraseniaNueva) {
        validateEsPropietario(id);

        ResgistroUsuarioEntity e = validateIfExistsById(id);

        if (!passwordEncoder.matches(contraseniaActual, e.getContrasenia())) {
            throw new AccessDeniedException("La contraseña actual es incorrecta");
        }

        e.setContrasenia(passwordEncoder.encode(contraseniaNueva));
        repository.save(e);
        return true;
    }

    public boolean updateFoto(long id, String foto) {
        validateEsPropietario(id);
        ResgistroUsuarioEntity e = validateIfExistsById(id);
        e.setFoto(foto);
        repository.save(e);
        return true;
    }

    public boolean updateCv(long id, String cv) {
        validateEsPropietario(id);
        ResgistroUsuarioEntity e = validateIfExistsById(id);
        e.setCv(cv);
        repository.save(e);
        return true;
    }

    private void validateEsPropietario(long id){
        if (AuthUtils.tieneRol("ADMIN")) return;

        Integer userId = AuthUtils.getCurrentUserId();
        if (userId == null || userId != id) {
            throw new AccessDeniedException("No tienes permiso para modificar esta cuenta");
        }
    }

    public boolean update(long id, ResgistroUsuarioRequestDto dto){
        validateEsPropietario(id);
        ResgistroUsuarioEntity e = validateIfExistsById(id);
        e.setNombres(dto.getNombres());
        e.setApellidos(dto.getApellidos());
        e.setTipoIdentificacion(dto.getTipoIdentificacion());
        e.setCedula(dto.getCedula());
        e.setFechaNacimiento(dto.getFechaNacimiento());
        e.setCorreoElectronico(dto.getCorreoElectronico());
        e.setGenero(dto.getGenero());
        e.setNumeroTelefonico(dto.getNumeroTelefonico());
        e.setAnosExperiencia(dto.getAnosExperiencia());

        if (dto.getContrasenia() != null && !dto.getContrasenia().isBlank()) {
            e.setContrasenia(passwordEncoder.encode(dto.getContrasenia()));
        }
        e.setCiudad(dto.getCiudad());
        e.setCargo(dto.getCargo());
        e.setEstudio(dto.getEstudio());
        e.setDescripcion(dto.getDescripcion());

        // Solo actualizamos la foto/cv si vienen valores nuevos (evita
        // borrarlos si el frontend no los envía en algún request puntual)
        if (dto.getFoto() != null && !dto.getFoto().isBlank()) {
            e.setFoto(dto.getFoto());
        }
        if (dto.getCv() != null && !dto.getCv().isBlank()) {
            e.setCv(dto.getCv());
        }

        repository.save(e);
        return true;
    }

    public void delete(long id){
        validateEsPropietario(id);
        repository.delete(validateIfExistsById(id));
    }


    public void deleteByCorreo(String correo){
        ResgistroUsuarioEntity e = validateIfExistsByCorreo(correo);
        validateEsPropietario(e.getId());
        repository.delete(e);
    }

    public ResgistroUsuarioListResponseDto entityToDto(ResgistroUsuarioEntity e){
        return ResgistroUsuarioListResponseDto.builder()
                .id(e.getId())
                .nombres(e.getNombres())
                .apellidos(e.getApellidos())
                .tipoIdentificacion(e.getTipoIdentificacion())
                .cedula(e.getCedula())
                .fechaNacimiento(e.getFechaNacimiento())
                .correoElectronico(e.getCorreoElectronico())
                .genero(e.getGenero())
                .numeroTelefonico(e.getNumeroTelefonico())
                .anosExperiencia(e.getAnosExperiencia())
                .contrasenia(null) // nunca se expone la contraseña/hash en las respuestas
                .Ciudad(e.getCiudad())
                .estudio(e.getEstudio())
                .Cargo(e.getCargo())
                .Descripcion(e.getDescripcion())
                .rolNombre(e.getRolNombre() != null ? e.getRolNombre().getRolNombre() : null)
                .foto(e.getFoto())
                .cv(e.getCv())
                .build();
    }

    public ResgistroUsuarioEntity dtoToEntity(ResgistroUsuarioRequestDto dto){
        RolEntity rol = rolRepository.findByRolNombre((String) dto.getRolNombre())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));
        return ResgistroUsuarioEntity.builder()
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .tipoIdentificacion(dto.getTipoIdentificacion())
                .cedula(dto.getCedula())
                .fechaNacimiento(dto.getFechaNacimiento())
                .correoElectronico(dto.getCorreoElectronico())
                .numeroTelefonico(dto.getNumeroTelefonico())
                .genero(dto.getGenero())
                .anosExperiencia(dto.getAnosExperiencia())
                .contrasenia(passwordEncoder.encode(dto.getContrasenia()))
                .ciudad(dto.getCiudad())
                .estudio(dto.getEstudio())
                .descripcion(dto.getDescripcion())
                .cargo(dto.getCargo())
                .foto(dto.getFoto())
                .cv(dto.getCv())
                .rolNombre(rol)
                .build();
    }
}
