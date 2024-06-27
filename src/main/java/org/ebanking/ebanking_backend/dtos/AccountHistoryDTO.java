package org.ebanking.ebanking_backend.dtos;

import lombok.Data;

import java.util.List;

//On a besoin de faire la paginations car on a une centaines d'operation
@Data
public class AccountHistoryDTO {
    private String accountId;
    private double balance;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private List<AccountOperationDTO> accountOperationDTOs;
}
