package com.transtu.reclamationservice.repository;

import com.transtu.reclamationservice.entities.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReclamationRepository extends JpaRepository<Reclamation,Long> {
}
