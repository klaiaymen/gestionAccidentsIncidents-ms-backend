package com.transtu.reclamationservice.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class District {
    private Long id;
    private String label;
    private String adress;
    private String phoneNumber;
}
