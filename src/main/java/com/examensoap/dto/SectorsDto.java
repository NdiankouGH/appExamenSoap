package com.examensoap.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectorsDto {

    private Long id;
    @NotNull(message = "Le nom du secteur ne doit pas être nul.")
    private String name;
}
