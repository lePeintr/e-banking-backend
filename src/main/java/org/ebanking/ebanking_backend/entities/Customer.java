package org.ebanking.ebanking_backend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data//Lombock pour les getter et setter
@NoArgsConstructor//Lombock Constructeur par defaut
@AllArgsConstructor // Lombock constructeur avec paramètre
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @OneToMany (mappedBy = "customer")//un client pour plusieurs comptes mappedBy (crée la clé etrangère ) = dans BankAccound il y'a un attribut qui s'appelle customer
    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY) //pour eviter de charger les listes de compte d'un customer quand on fait la recherche d'un customer via API REST
    private List<BankAccount> bankAccountList; //liste de compte d'un client

}
