package org.ebanking.ebanking_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ebanking.ebanking_backend.enums.AccountStatus;

import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)//JPA pour stcoker tous les objets qui vont hériter de cette objet dans une seule table
@DiscriminatorColumn(name = "TYPE",length = 4)//Lorsqu'on choisi SINGLE TABLE il y'aune
// colonne qui s'ajoute et cet annotatiion permet de donner
// le nom à cette colonne dans la table,sa taille,et son type(string,char,integer) La valeur par defaut eest String
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    @Id
    private String id;


    private Date createdAt;


    private double balance;


    private String currency;

    @Enumerated(EnumType.STRING)//afficher l'énumération en String dans la base de donnée
    private AccountStatus status;

    @ManyToOne //plusieurs compte pour un client
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount", fetch=FetchType.LAZY)//un compte à plusieurs opérations  @OneToMany(mappedBy = "bankAccount", fetch=FetchType.EAGER)//un compte à plusieurs opérations
    private List<AccountOperation> accountOperationList;
    //Dans le relation OneToMany
    //fetch=FetchType.LAZY ne charge pas la liste des opérations quand on charge un compte methode par defaut
    // et le chargement de la  liste des operation uniquement à la demande
    //fetch=FetchType.EAGER charge  la liste des opérations quand on charge un compte (ce qui peut etre lent car parfois on peut juste avoir besoin des informations d'un compte sans
    // avoir besoin des informations des opérations du compte) : Ce n'est pas la bonne méthode

}
