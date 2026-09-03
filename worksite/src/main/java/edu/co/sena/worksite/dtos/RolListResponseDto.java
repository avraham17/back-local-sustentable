package edu.co.sena.worksite.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolListResponseDto {
    private int id;
    private String getRolNombre;

}
