package com.examensoap.service.impl;

import com.examensoap.dao.SectorsRepository;
import com.examensoap.dto.SectorsDto;
import com.examensoap.entity.SectorsEntity;
import com.examensoap.exception.ServiceException;
import com.examensoap.mapper.SectorsMapper;
import com.examensoap.service.ISectorsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class SectorsService implements ISectorsService {
    private final SectorsRepository sectorsRepository;
    private final SectorsMapper sectorsMapper;

    public SectorsService(SectorsRepository sectorsRepository, SectorsMapper sectorsMapper) {
        this.sectorsRepository = sectorsRepository;
        this.sectorsMapper = sectorsMapper;
    }

    @Override
    public SectorsDto getSectorById(Long id) {
        return sectorsMapper.toSectorsDto(sectorsRepository
                .findById(id).orElseThrow(
                        () -> new ServiceException("Sector not found with id: " + id)
                ));
    }

    @Override
    public SectorsDto createSectors(SectorsDto sector) {
        try {
            SectorsEntity savedSector = sectorsRepository.save(sectorsMapper.toSectorsEntity(sector));
            return sectorsMapper.toSectorsDto(savedSector);
        } catch (Exception e) {
            throw new ServiceException("Error creating sector: " + e.getMessage(), e);
        }
    }

    @Override
    public List<SectorsDto> getAllSectors() {
        return StreamSupport.stream(sectorsRepository.findAll().spliterator(), false)
                .map(sectorsMapper::toSectorsDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteSectors(Long id) {
        sectorsRepository.findById(id).orElseThrow(
                () -> new ServiceException("Secteur introuvable avec ID: " + id)
        );
        try {
            sectorsRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("Erreur de suppression du secteur: " + e.getMessage(), e);
        }
    }

    @Override
    public SectorsDto updateSectors(Long id, SectorsDto sector) {
        try {
            sectorsRepository.findById(id).orElseThrow(
                    () -> new ServiceException("Secteur introuvable avec ID: " + id)
            );
            sector.setId(id);
            SectorsEntity updatedSector = sectorsRepository.save(sectorsMapper.toSectorsEntity(sector));
            return sectorsMapper.toSectorsDto(updatedSector);
        } catch (Exception e) {
            throw new ServiceException("SECTEUR DE MISE À JOUR ERREUR:" + e.getMessage(), e);
        }
    }
}
