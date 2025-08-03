package com.examensoap.dao;

import com.examensoap.entity.ClassesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassesRepository extends JpaRepository<ClassesEntity, Long> {

    @Query("SELECT c FROM ClassesEntity c  JOIN FETCH c.sectors WHERE c.id = :id")
    ClassesEntity findByIdWithSectors(Long id);

    List<ClassesEntity> findBySectorsId(Long sectorsId);


}
