package edu.co.sena.worksite.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequestDto {

    private String nombre;
    private String sector;
    private String ubicacion;
    private String telefono;
    private String correo;
    private Integer idUsuario;
    private String foto;


}
