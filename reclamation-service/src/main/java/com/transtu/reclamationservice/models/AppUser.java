package com.transtu.reclamationservice.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String tel;
    private List<AppRole> roles;
    private MoyenTransport moyenTransport;
}
