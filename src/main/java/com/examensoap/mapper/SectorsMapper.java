package com.examensoap.mapper;

import com.examensoap.dto.SectorsDto;
import com.examensoap.entity.SectorsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SectorsMapper {
    public SectorsDto toSectorsDto(SectorsEntity sectorsEntity);

    public SectorsEntity toSectorsEntity(SectorsDto sectorsDto);
}
