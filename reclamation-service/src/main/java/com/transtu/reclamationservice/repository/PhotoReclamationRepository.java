package com.transtu.reclamationservice.repository;

import com.transtu.reclamationservice.entities.PhotoReclamation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoReclamationRepository extends JpaRepository<PhotoReclamation,Long> {
}
