package com.examensoap.dao;

import com.examensoap.entity.SectorsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorsRepository extends JpaRepository<SectorsEntity, Long> {

    @Query("SELECT s FROM SectorsEntity s LEFT JOIN FETCH s.classes")
    public SectorsEntity findAllWithClasses();
}
