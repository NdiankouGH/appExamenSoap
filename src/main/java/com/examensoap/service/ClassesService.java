package com.examensoap.service;


import com.examensoap.model.Classes;
import com.examensoap.model.Sectors;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ClassesService {

    private final Map<Long, Classes> students = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        // Initialisation de la base de données fictive avec quelques classes
        Sectors sectors = new Sectors();
        sectors.setId(idGenerator.getAndIncrement());
        sectors.setName("Informatique");

        Classes class1 = new Classes();
        class1.setId(idGenerator.getAndIncrement());
        class1.setClassName("Classe A");
        class1.setSectors(1L);

        students.put(class1.getId(), class1);

        Classes class2 = new Classes();
        class2.setId(idGenerator.getAndIncrement());
        class2.setClassName("Classe B");
        class2.setSectors(1L);
        students.put(class2.getId(), class2);
    }

    //Recuperer la classe par son ID
    public Classes getClassById(Long id) {
        Classes classe = students.get(id);
        if (classe == null) {
            throw new IllegalArgumentException("La classe avec l'ID " + id + " n'est pas trouvée");
        }
        return classe;
    }

    //Ajouter une nouvelle classe
    public Classes createClass(Classes classe) {
        Long id = idGenerator.getAndIncrement();
        classe.setId(id);
        students.put(id, classe);
        return classe;
    }

    //Recuperer toutes les classes
    public Map<Long, Classes> listAllClasses() {
        return new HashMap<>(students);
    }

    //Supprimer une classe par son ID
    public void deleteClass(Long id) {
        if (!students.containsKey(id)) {
            throw new IllegalArgumentException("La classe avec l'ID " + id + " n'existe pas");
        }
        students.remove(id);
    }
}
