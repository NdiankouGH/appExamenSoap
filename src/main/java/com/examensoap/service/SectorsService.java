package com.examensoap.service;


import com.examensoap.model.Sectors;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class SectorsService {
    private final Map<Long, Sectors> sectors = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);


    @PostConstruct
    public void init() {

        Sectors sectors1 = new Sectors();
        sectors1.setId(idGenerator.getAndIncrement());
        sectors1.setName("Informatique");

        Sectors sectors2 = new Sectors();
        sectors2.setId(idGenerator.getAndIncrement());
        sectors2.setName("Réseaux Telecommunication");

        sectors.put(sectors1.getId(), sectors1);
        sectors.put(sectors2.getId(), sectors2);
    }

    //Recuperer une filiere par son id
    public Sectors getSectorById(Long id) {
        Sectors sectors1 = sectors.get(id);
        if (sectors1 == null) {
            throw new IllegalArgumentException("La filiere avec l'ID " + id + " n'existe pas");
        }
        return sectors1;
    }

    //Ajouter une nouvelle filiere
    public Sectors createSectors(Sectors sector) {
        Long id = idGenerator.getAndIncrement();
        sector.setId(id);
        sectors.put(id, sector);
        return sector;

    }

    //Recuperer la liste des filieres
    public Map<Long, Sectors> getAllSectors() {
        return new HashMap<>(sectors);
    }

    public void deleteSectors(Long id) {
        if (!sectors.containsKey(id)) {
            throw new IllegalArgumentException("La filiere avec l'ID " + id + " n'existe pas");
        }
        sectors.remove(id);
    }
}
