package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Page<Client> findByNameContains(String name, Pageable pagination);
}
