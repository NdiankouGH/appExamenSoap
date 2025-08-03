package com.examensoap.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectorsEntity {
    @OneToMany(mappedBy = "sectors", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<ClassesEntity> classes = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

}
