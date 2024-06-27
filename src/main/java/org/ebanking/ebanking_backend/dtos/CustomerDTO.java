package org.ebanking.ebanking_backend.dtos;

import lombok.Data;


@Data//Lombock pour les getter et setter
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
}
