package com.transtu.reclamationservice.service;


import com.transtu.reclamationservice.clients.UserRestClient;
import com.transtu.reclamationservice.entities.PhotoReclamation;
import com.transtu.reclamationservice.entities.Reclamation;
import com.transtu.reclamationservice.models.AppUser;
import com.transtu.reclamationservice.repository.PhotoReclamationRepository;
import com.transtu.reclamationservice.repository.ReclamationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReclamationService {
    private final ReclamationRepository reclamationRepository;
    private final PhotoReclamationRepository photoReclamationRepository;
    private final EmailService emailService;
    private final VonageSmsService vonageSmsService;
    private final UserRestClient userRestClient;

    public Optional<Reclamation> findById(Long id) {
        return reclamationRepository.findById(id);
    }


    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    public Reclamation createReclamation(Reclamation reclamation) {
        reclamation.setState("brouillon");
        return reclamationRepository.save(reclamation);
    }

    public Reclamation getReclamationById(Long reclamationId) {
        return reclamationRepository.findById(reclamationId).orElse(null);
    }

    public Reclamation editReclamation(Long id, Reclamation updatedReclamation) {
        Reclamation existingReclamation = reclamationRepository.findById(id).orElse(null);
        if (existingReclamation == null) {
            return null;
        }
        if (existingReclamation.getState().equals("brouillon")) {

            if (updatedReclamation.getNotes() != null) existingReclamation.setNotes(updatedReclamation.getNotes());
            if (updatedReclamation.getLieu() != null) existingReclamation.setLieu(updatedReclamation.getLieu());
            if (updatedReclamation.getTypeDegat() != null)
                existingReclamation.setTypeDegat(updatedReclamation.getTypeDegat());
            if (updatedReclamation.getTypeAccidentIncident() != null)
                existingReclamation.setTypeAccidentIncident(updatedReclamation.getTypeAccidentIncident());
            if (updatedReclamation.getReportingSourceEtat() != null)
                existingReclamation.setReportingSourceEtat(updatedReclamation.getReportingSourceEtat());
            if (updatedReclamation.getReportingSourceNomPrenom() != null)
                existingReclamation.setReportingSourceNomPrenom(updatedReclamation.getReportingSourceNomPrenom());
            if (updatedReclamation.getReportingSourceTel() != null)
                existingReclamation.setReportingSourceTel(updatedReclamation.getReportingSourceTel());
            existingReclamation.setState("userValidated");
            //if (updatedReclamation.getReclamationPhotos() != null) existingReclamation.setReclamationPhotos(updatedReclamation.getReclamationPhotos());
            return reclamationRepository.save(existingReclamation);
        } else {
            return null;
        }
    }

    public Reclamation validateByUser(Long id) {
        Reclamation reclamation = reclamationRepository.findById(id).orElse(null);
        if (reclamation != null && reclamation.getState().equals("brouillon")) {
            reclamation.setState("userValidated");
            return reclamationRepository.save(reclamation);
        } else {
            return null;
        }
    }

    public Reclamation validateByDispatcher(Long id) {
        Reclamation reclamation = reclamationRepository.findById(id).orElse(null);
        if (reclamation != null && reclamation.getState().equals("userValidated")) {

            // Envoyer l'e-mail avec les détails de la réclamation
            List<String> recipients = new ArrayList<>();
            recipients.add("klaiaymen58@gmail.com");
            recipients.add("klaiaymen88@gmail.com");
            String subject = reclamation.getNotes();
            String lieuLink ="https://www.bing.com/maps?cp="+reclamation.getLieu()+"&lvl=18.0";
            String text = "réclamation a été validée par le dispatcher.\n " +
                        "Reporteur : "+reclamation.getReportingSourceEtat()+"Nom et prenom : "+reclamation.getReportingSourceNomPrenom()+"Tel : "+reclamation.getReportingSourceTel()+"\n"+
                        "Type accident/incident :"+reclamation.getTypeAccidentIncident()+" type dégâts :"+reclamation.getTypeDegat()+" lieu :"+lieuLink+" description :"+reclamation.getNotes();
            emailService.sendEmail(recipients, subject, text);
            // Envoyer SMS twilio
            /*List<String> recipientPhoneNumbers = new ArrayList<>();
            recipientPhoneNumbers.add("+21625156773");
            recipientPhoneNumbers.add("+21629651709");
            String smsBody = text;
            smsService.sendSms(recipientPhoneNumbers, smsBody);*/

            // Envoyer SMS avec Vonage
            String[] recipientPhoneNumbers = {"+21625156773","+21654126993","21695302444","+21629651709"};
            String smsBody = text;
            vonageSmsService.sendSmsToMultipleRecipients(recipientPhoneNumbers, smsBody);

            reclamation.setState("dispatcherValidated");
            return reclamationRepository.save(reclamation);

        } else {
            return null;
        }
    }



    public Reclamation createReclamationWithPhotos(Reclamation reclamation, List<MultipartFile> photos) {
        reclamation.setState("brouillon");
        for (MultipartFile file : photos) {
            PhotoReclamation photo = processAndSavePhoto(file);
            if (photo != null) {
                photo.setReclamation(reclamation);
                reclamation.getPhotos().add(photo);
            } else {
                // Gérer les erreurs de traitement des photos
                // Par exemple, vous pouvez ignorer les photos non valides
            }
        }
        return reclamationRepository.save(reclamation);
    }

    private PhotoReclamation processAndSavePhoto(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            Files.write(Paths.get("C:/Users/lenovo/Desktop/" + fileName), file.getBytes());
            PhotoReclamation photo = new PhotoReclamation();
            photo.setFileName(fileName);
            photo = photoReclamationRepository.save(photo);
            return photo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*public Reclamation createReclamationWithPhotos(Reclamation reclamation, List<MultipartFile> photos) {
        reclamation.setState("brouillon");
        List<PhotoReclamation> photoReclamations = new ArrayList<>();
        for (MultipartFile file : photos) {
            PhotoReclamation photo = processAndSavePhoto(file);
            if (photo != null) {
                photoReclamations.add(photo);
            } else {
                // Gérer les erreurs de traitement des photos
                // Par exemple, vous pouvez ignorer les photos non valides
            }
        }
        reclamation.setPhotos(photoReclamations);
        return reclamationRepository.save(reclamation);
    }

    private PhotoReclamation processAndSavePhoto(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String fileUrl = cloudStorageService.uploadFile(file.getInputStream(), fileName);
            PhotoReclamation photo = new PhotoReclamation();
            photo.setFileName(fileName);
            photo.setUrl(fileUrl);
            // Enregistrer la photo dans la base de données si nécessaire
            photo = photoReclamationRepository.save(photo);
            return photo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public Reclamation saveReclamationWithPhotos(Reclamation reclamation, List<MultipartFile> photos) throws IOException {
        // Sauvegarde de la réclamation
        Reclamation savedReclamation = reclamationRepository.save(reclamation);

        // Sauvegarde des photos et création des URLs
        List<PhotoReclamation> savedPhotos = photos.stream()
                .map(photo -> {
                    String fileName = photo.getOriginalFilename();
                    String url = MvcUriComponentsBuilder.fromMethodName(PhotoReclamation.class, "getFile", fileName)
                            .build().toString();
                    try {
                        // Sauvegarde de la photo
                        byte[] data = photo.getBytes();
                        PhotoReclamation photoReclamation = new PhotoReclamation();
                        photoReclamation.setFileName(fileName);
                        photoReclamation.setUrl(url);
                        photoReclamation.setReclamation(savedReclamation);
                        // Sauvegarde de la photo dans la base de données
                        return photoReclamationRepository.save(photoReclamation);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Gérer l'erreur de manière appropriée
                        // Dans cet exemple, nous renvoyons simplement null
                        return null;
                    }
                })
                .collect(Collectors.toList());

        // Mise à jour de la réclamation avec les URLs des photos
        savedReclamation.setPhotos(savedPhotos);

        // Sauvegarde de la réclamation mise à jour
        return reclamationRepository.save(savedReclamation);
    }

    public AppUser getUserById(Long userId) {
        return userRestClient.findUserById(userId);
    }

    public AppUser getUserByUsername(String username) {
        return userRestClient.getUserByUsername(username);
    }

    public Map<String, Long> getReclamationCountByDistrict() {
        List<Object[]> result = reclamationRepository.getReclamationCountByDistrict();
        Map<String, Long> reclamationCountByDistrict = new HashMap<>();
        for (Object[] row : result) {
            String districtName = (String) row[0];
            Long reclamationCount = (Long) row[1];
            reclamationCountByDistrict.put(districtName, reclamationCount);
        }
        return reclamationCountByDistrict;
    }

    public Map<String, Long> getReclamationCountByMt() {
        List<Object[]> result = reclamationRepository.getReclamationCountByMt();
        Map<String, Long> reclamationCountByMt = new HashMap<>();
        for (Object[] row : result) {
            String mtName = (String) row[0];
            Long reclamationCount = (Long) row[1];
            reclamationCountByMt.put(mtName, reclamationCount);
        }
        return reclamationCountByMt;
    }

    public List<Reclamation> getReclamationByUser(Long id){
        //AppUser appUser=userRestClient.getUserByUsername(username);
        return reclamationRepository.findByAppUserUserId(id);
    }
}
