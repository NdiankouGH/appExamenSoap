package com.examensoap.mapper;

import com.examensoap.dto.ClassesDto;
import com.examensoap.entity.ClassesEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface ClassesMapper {

     ClassesDto toClassesDto(ClassesEntity classesEntity);

     ClassesEntity toClassesEntity(ClassesDto classesDto);
}
