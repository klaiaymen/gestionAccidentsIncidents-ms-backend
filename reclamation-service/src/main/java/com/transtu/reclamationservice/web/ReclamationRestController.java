package com.transtu.reclamationservice.web;

import com.transtu.reclamationservice.clients.UserRestClient;
import com.transtu.reclamationservice.entities.Reclamation;
import com.transtu.reclamationservice.models.User;
import com.transtu.reclamationservice.repository.ReclamationRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ReclamationRestController {
    private ReclamationRepository reclamationRepository;
    private UserRestClient userRestClient;


    @GetMapping("/reclamations")
    public List<Reclamation> reclamationList(){
        List<Reclamation> reclamationList = reclamationRepository.findAll();
        reclamationList.forEach(r->{
            r.setUser(userRestClient.findUserById(r.getUserId()));
        });
        return reclamationList;
    }
    @GetMapping("/reclamations/{id}")
    public Reclamation reclamationById(@PathVariable Long id){
        Reclamation reclamation= reclamationRepository.findById(id).get();
        User user=userRestClient.findUserById(reclamation.getUserId());
        reclamation.setUser(user);
        return reclamation;
    }
}
