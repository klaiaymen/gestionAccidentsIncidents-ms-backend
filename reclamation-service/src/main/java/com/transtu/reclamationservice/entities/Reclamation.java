package com.transtu.reclamationservice.entities;

import com.transtu.reclamationservice.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="reclamation")
public class Reclamation {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate createAt;
    private String state;
    private String reportingSourceEtat;
    private String reportingSourceTel;
    private String reportingSourceNomPrenom;
    private String lieu;
    public LocalDate date=LocalDate.now();
    private String typeAccidentIncident;
    private String typeDegat;
    private String notes;
    @OneToMany(mappedBy = "reclamation", cascade = CascadeType.ALL)
    private List<PhotoReclamation> photos;
    @Transient
    private User user;
    private Long userId;
}
