package com.transtu.reclamationservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="photo_reclamation")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PhotoReclamation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fileName;
    private String url;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reclamation_id")
    private Reclamation reclamation;


}
