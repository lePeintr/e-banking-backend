package org.ebanking.ebanking_backend.repositories;

import org.ebanking.ebanking_backend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    /*Customer createCustomer(String name, String email);
    Customer updateCustomer(Long customer_id);
    List<Customer> getAllCustomer();
    Customer getCustomerByEmail(String email);
    void deleteCustomer(Long customer_id);*/
}
