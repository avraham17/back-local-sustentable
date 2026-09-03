package edu.co.sena.worksite.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaListResponseDto {
    private int id;
    private String nombre;
    private String sector;
    private String ubicacion;
    private String telefono;
    private String correo;
    private int idUsuario;
    private String foto;


}
