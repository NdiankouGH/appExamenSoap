package com.examensoap.service.impl;

import com.examensoap.dao.ClassesRepository;
import com.examensoap.dao.SectorsRepository;
import com.examensoap.dto.ClassesDto;
import com.examensoap.entity.ClassesEntity;
import com.examensoap.entity.SectorsEntity;
import com.examensoap.exception.ServiceException;
import com.examensoap.mapper.ClassesMapper;
import com.examensoap.service.IClassesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassesService implements IClassesService {

    private final ClassesRepository classesRepository;
    private final ClassesMapper classesMapper;
    SectorsRepository sectorsRepository;

    public ClassesService(ClassesRepository classesRepository, ClassesMapper classesMapper,
                          SectorsRepository sectorsRepository) {
        this.sectorsRepository = sectorsRepository;
        this.classesRepository = classesRepository;
        this.classesMapper = classesMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ClassesDto> getAllClasses() {
        return classesRepository.findAll().stream()
                .map(classesMapper::toClassesDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClassesDto getClasseById(Long id) {
        return classesMapper.toClassesDto(classesRepository.findById(id).orElseThrow(
                () -> new ServiceException("Classe avec l'ID " + id + " n'existe pas")
        ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassesDto> getClassesBySector(Long sectorId) {
        return classesRepository.findBySectorsId(sectorId).stream()
                .map(classesMapper::toClassesDto)
                .toList();
    }

    @Override
    public ClassesDto createClasse(ClassesDto classesDto) {
        try {
            SectorsEntity sector = sectorsRepository.findById(classesDto.getSectorId())
                    .orElseThrow(() -> new ServiceException("Secteur non trouvé avec l'ID: " + classesDto.getSectorId()));

            // Création et sauvegarde
            ClassesEntity classe = classesMapper.toClassesEntity(classesDto);
            classe.setSectors(sector);
            return classesMapper.toClassesDto(classesRepository.save(classe));
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la création de la classe: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ClassesDto updateClasse(Long id, ClassesDto classesDto) {
        // Validation existence
        ClassesEntity existingClasse = classesRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Classe non trouvée avec l'ID: " + id));


        // Récupération du secteur si changé
        if (!existingClasse.getSectors().getId().equals(classesDto.getSectorId())) {
            SectorsEntity sector = sectorsRepository.findById(classesDto.getSectorId())
                    .orElseThrow(() -> new ServiceException("Secteur non trouvé avec l'ID: " + classesDto.getSectorId()));
            existingClasse.setSectors(sector);
        }

        // Mise à jour
        existingClasse.setClassName(classesDto.getClassName());
        existingClasse.setDescription(classesDto.getDescription());

        ClassesEntity updatedClasse = classesRepository.save(existingClasse);
        return classesMapper.toClassesDto(updatedClasse);
    }

    @Override
    @Transactional
    public void deleteClasse(Long id) {
        if (!classesRepository.existsById(id)) {
            throw new ServiceException("Classe non trouvée avec l'ID: " + id);
        }
        classesRepository.deleteById(id);
    }
}
