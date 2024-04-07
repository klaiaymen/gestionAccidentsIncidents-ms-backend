package com.transtu.reclamationservice.clients;

import com.transtu.reclamationservice.models.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "GESTIONACCIDENTSINCIDENTS")
public interface UserRestClient {
    @GetMapping("/users/{id}")
    @CircuitBreaker(name = "GESTIONACCIDENTSINCIDENTS", fallbackMethod = "getDefaultUser")
    User findUserById(@PathVariable Long id);
    @GetMapping("/users")
    @CircuitBreaker(name = "GESTIONACCIDENTSINCIDENTS", fallbackMethod = "getAllUsers")
    List<User> allUsers();

    default User getDefaultUser(Long id, Exception exception){
        User user=new User();
        user.setId(id);
        user.setName("Not Vailable");
        user.setEmail("Not Vailable");
        return user;
    }
    default List<User> getAllUsers(Exception exception){
        return List.of();
    }
}
