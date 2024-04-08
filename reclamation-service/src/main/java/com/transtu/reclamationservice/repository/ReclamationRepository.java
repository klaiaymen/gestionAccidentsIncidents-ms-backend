package com.transtu.reclamationservice.repository;

import com.transtu.reclamationservice.entities.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation,Long> {
    @Query("SELECT i FROM Reclamation i " +
            "WHERE (i.date BETWEEN :fromDate AND :toDate) " +
            "AND i.typeAccidentIncident LIKE %:typeAccidentIncident% " +
            "AND i.typeDegat LIKE %:typeDegat% " +
            "AND CONCAT(" +
            "i.date, ' ', i.id, ' ', i.lieu, ' ', i.notes, ' ', " +
            "i.reportingSourceEtat, ' ', i.reportingSourceNomPrenom, ' ', " +
            "i.reportingSourceTel, ' ', i.state, ' ', " +
            "i.typeAccidentIncident, ' ', i.typeDegat) LIKE %:query%"
    )
    List<Reclamation> searchReclamationsByDateRangeAndTypes(
            String query,
            LocalDate fromDate, LocalDate toDate,
            String typeAccidentIncident, String typeDegat);


    @Query("SELECT i FROM Reclamation i " +
            "WHERE (i.typeAccidentIncident LIKE %:typeAccidentIncident%) " +
            "AND i.typeDegat LIKE %:typeDegat% " +
            "AND CONCAT(" +
            "i.date, ' ', i.id, ' ', i.lieu, ' ', i.notes, ' ', " +
            "i.reportingSourceEtat, ' ', i.reportingSourceNomPrenom, ' ', " +
            "i.reportingSourceTel, ' ', i.state, ' ', " +
            "i.typeAccidentIncident, ' ', i.typeDegat) LIKE %:query%"
    )
    List<Reclamation> searchReclamationsWithoutDateRange(String query, String typeAccidentIncident, String typeDegat);
}
