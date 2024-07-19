package org.ebanking.ebanking_backend.repositories;

import org.ebanking.ebanking_backend.entities.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
     List<AccountOperation> findByBankAccountId(String accountId);
   /*  pourquoi sans developper la fonction findByBankAccountIdOrderByOperationDateDesc et en changeant juste le nom de la methohde en ajoutant OrderBy...
      il comprend directement qu'il faut ordonner par date??
      --------------------------------Reponse------------------
     Héritage d'interfaces : JpaRepository hérite de PagingAndSortingRepository et CrudRepository, ce qui lui permet de fournir
     des méthodes de pagination et de tri.
     Méthodes dérivées : Vous pouvez ajouter des méthodes personnalisées en suivant une convention de nommage (par exemple, findByLastName).*/
     Page<AccountOperation> findByBankAccountIdOrderByOperationDateDesc(String accountId, Pageable pageable);
}
