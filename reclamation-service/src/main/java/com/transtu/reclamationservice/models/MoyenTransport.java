package com.transtu.reclamationservice.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MoyenTransport {
    private Long id;
    private String label;
    private String code;
    private String type;
    private District district;
}
