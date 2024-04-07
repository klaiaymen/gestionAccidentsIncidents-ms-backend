package com.transtu.reclamationservice.entities;

import com.transtu.reclamationservice.models.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.*;

import java.time.LocalDate;

@Entity @Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reclamation {
    @Id
    private Long id;
    private LocalDate createAt;
    private String description;
    @Transient
    private User user;
    private Long userId;
}
