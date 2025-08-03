package com.examensoap.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClassesDto {
    private Long id;
    @NotNull(message = "Le nom de classe ne peut pas être vide")
    private String className;
    private String description;
    private Long sectorId;
}
