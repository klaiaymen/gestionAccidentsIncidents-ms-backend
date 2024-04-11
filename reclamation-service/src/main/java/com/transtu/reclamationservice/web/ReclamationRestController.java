package com.transtu.reclamationservice.web;

import com.transtu.reclamationservice.clients.UserRestClient;
import com.transtu.reclamationservice.entities.PhotoReclamation;
import com.transtu.reclamationservice.entities.Reclamation;
import com.transtu.reclamationservice.models.User;
import com.transtu.reclamationservice.repository.PhotoReclamationRepository;
import com.transtu.reclamationservice.repository.ReclamationRepository;
import com.transtu.reclamationservice.service.EmailService;
import com.transtu.reclamationservice.service.ReclamationService;
import com.transtu.reclamationservice.service.ReportReclamationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class ReclamationRestController {
    private UserRestClient userRestClient;
    private final ReclamationService reclamationService;
    private final ReclamationRepository reclamationRepository;
    private final EmailService emailService;
    private final PhotoReclamationRepository photoReclamationRepository;
    private final ReportReclamationService reportReclamationService;


    /*@GetMapping("/reclamations")
    public List<Reclamation> reclamationList(){
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        reclamationList.forEach(r->{
            r.setUser(userRestClient.findUserById(r.getUserId()));
        });
        return reclamationList;
    }*/

    /*@GetMapping("/reclamations/{id}")
    public Reclamation reclamationById(@PathVariable Long id){
        Reclamation reclamation= reclamationRepository.findById(id).get();
        User user=userRestClient.findUserById(reclamation.getUserId());
        reclamation.setUser(user);
        return reclamation;
    }*/

    //copier coller
    @PostMapping("/reclamation")
    @ResponseStatus(HttpStatus.CREATED)
    public Reclamation createReclamation(@RequestBody Reclamation reclamation){
        return reclamationService.createReclamation(reclamation);
    }

    @GetMapping("/reclamation")
    @ResponseStatus(HttpStatus.OK)
    public List<Reclamation> getAllReclamations() {
        return reclamationService.getAllReclamations();
    }
    @GetMapping(value="/reclamation/{id}")
    public ResponseEntity<Reclamation> getReclamationById(@PathVariable("id") Long id) {
        return reclamationRepository.findById(id)
                .map(reclamation -> ResponseEntity.ok().body(reclamation))
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/reclamation/{id}")
    public ResponseEntity<Reclamation> updateReclamation(@PathVariable Long id, @RequestBody Reclamation reclamation) {
        Reclamation updatedReclamation = reclamationService.editReclamation(id, reclamation);
        if (updatedReclamation != null) {
            return new ResponseEntity<>(updatedReclamation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping(value="/reclamation/{id}")
    public ResponseEntity<?> deleteReclamation(@PathVariable("id") Long id) {
        return reclamationRepository.findById(id)
                .map(reclamtion -> {
                    if (reclamtion.getState().equals("userValidated")) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("La réclamation ne peut pas être supprimée.");
                    }
                    else if(reclamtion.getState().equals("dispatcherValidated")){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("La réclamation ne peut pas être supprimée.");
                    }
                    else{
                        reclamationRepository.deleteById(id);
                        return ResponseEntity.ok().build();
                    }
                }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/reclamation/userValidate/{id}")
    public ResponseEntity<Reclamation> validateByUser(@PathVariable Long id) {
        Reclamation validatedReclamation = reclamationService.validateByUser(id);
        if (validatedReclamation != null) {
            return new ResponseEntity<>(validatedReclamation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/reclamation/dispatcherValidate/{id}")
    public ResponseEntity<Reclamation> validateByDispatcher(@PathVariable Long id) {
        Reclamation validatedReclamation = reclamationService.validateByDispatcher(id);
        if (validatedReclamation != null) {
            return new ResponseEntity<>(validatedReclamation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/reclamation-with-photos")
    public ResponseEntity<Reclamation> saveReclamationWithPhotos(@RequestPart("reclamation") Reclamation reclamation,
                                                                 @RequestPart("photos") List<MultipartFile> photos) throws IOException {
        Reclamation savedReclamation = reclamationService.saveReclamationWithPhotos(reclamation, photos);
        return ResponseEntity.ok(savedReclamation);
    }


    @PostMapping("/photo-reclamation/{reclamationId}")
    public ResponseEntity<PhotoReclamation> savePhotoReclamation(@PathVariable Long reclamationId, @RequestBody String photoUrl) {
        // Récupérer la réclamation associée
        Reclamation reclamation = reclamationRepository.findById(reclamationId).orElse(null);
        if (reclamation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Créer une nouvelle PhotoReclamation avec l'URL et l'ID de la réclamation
        PhotoReclamation photoReclamation = new PhotoReclamation();
        photoReclamation.setUrl(photoUrl);
        photoReclamation.setReclamation(reclamation);

        // Enregistrer la photo dans la base de données
        PhotoReclamation savedPhotoReclamation = photoReclamationRepository.save(photoReclamation);
        return new ResponseEntity<>(savedPhotoReclamation, HttpStatus.CREATED);
    }



    @GetMapping("/reclamation-report")
    public ResponseEntity<String> generateReport(@RequestParam String format, @RequestParam String query, @RequestParam String fromDate, @RequestParam String toDate,@RequestParam String typeAccidentIncident,@RequestParam String typeDegat) {
        try {
            // Convertir les chaînes de caractères en objets LocalDate
            LocalDate fromDateObj = LocalDate.parse(fromDate);
            LocalDate toDateObj = LocalDate.parse(toDate);

            String result = reportReclamationService.exportReport(format,query, fromDateObj, toDateObj,typeAccidentIncident,typeDegat);
            return ResponseEntity.ok(result);


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating report: " + e.getMessage());
        }
    }

    @GetMapping("/reclamation-report-without-date")
    public ResponseEntity<String> generateReportWithoutDate(@RequestParam String format, @RequestParam String query,@RequestParam String typeAccidentIncident,@RequestParam String typeDegat) {
        try {

            String result = reportReclamationService.exportReportWithoutDate(format,query,typeAccidentIncident,typeDegat);
            return ResponseEntity.ok(result);


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating report: " + e.getMessage());
        }
    }



    @GetMapping("/reclamation/searchGlobal")
    public ResponseEntity<List<Reclamation>> searchReclamationsByDateRangeAndTypes(
            @RequestParam String query,
            @RequestParam String fromDate,
            @RequestParam String toDate,
            @RequestParam String typeAccidentIncident,
            @RequestParam String typeDegat) {
        LocalDate fromDateObj = LocalDate.parse(fromDate);
        LocalDate toDateObj = LocalDate.parse(toDate);
        List<Reclamation> reclamations = reclamationRepository.searchReclamationsByDateRangeAndTypes(
                query,fromDateObj, toDateObj, typeAccidentIncident, typeDegat);
        return new ResponseEntity<>(reclamations, HttpStatus.OK);
    }



    @GetMapping("/reclamation/searchReclamationWithoutDateRange")
    public ResponseEntity<List<Reclamation>> searchReclamationsWithoutDateRange(
            @RequestParam String query,
            @RequestParam String typeAccidentIncident,
            @RequestParam String typeDegat) {
        List<Reclamation> reclamations = reclamationRepository.searchReclamationsWithoutDateRange(
                query, typeAccidentIncident, typeDegat);
        return new ResponseEntity<>(reclamations, HttpStatus.OK);
    }
}
