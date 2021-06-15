package com.walt.dao;

import com.walt.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Repository
@RestController
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @GetMapping("/findCustomerByName")
    Customer findByName(String name);

    @GetMapping("/findAllCustomers")
    List<Customer> findAll();

    List<Customer> findByCityId(String cityId);
}
