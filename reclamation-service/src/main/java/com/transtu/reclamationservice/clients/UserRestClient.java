package com.transtu.reclamationservice.clients;

import com.transtu.reclamationservice.models.AppUser;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "GESTIONACCIDENTSINCIDENTS")
public interface UserRestClient {
    @GetMapping(path="/users/{id}")
    @CircuitBreaker(name = "GESTIONACCIDENTSINCIDENTS", fallbackMethod = "getDefaultUser")
    AppUser findUserById(@PathVariable Long id);
    @GetMapping(path="/users")
    @CircuitBreaker(name = "GESTIONACCIDENTSINCIDENTS", fallbackMethod = "getAllUsers")
    List<AppUser> allUsers();

    default AppUser getDefaultUser(Long id, Exception exception){
        AppUser user=new AppUser();
        user.setUserId(id);
        user.setUsername("Not Vailable");
        user.setEmail("Not Vailable");
        user.setTel("Not Vailable");
        user.setPassword("Not Vailable");
        return user;
    }
    default List<AppUser> getAllUsers(Exception exception){
        return List.of();
    }
    /*@GetMapping("/users/{userId}")
    AppUser findUserById(@PathVariable("userId") Long userId);*/

    @GetMapping("/users/username/{username}")
    AppUser getUserByUsername(@PathVariable String username);
}

