package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.ClientDTO;
import com.ecommerce.ecommerce.model.Client;
import com.ecommerce.ecommerce.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/client")
public class ClientController {

    @Autowired
    private ClientRepository clienteRepository;

    @GetMapping
    @Cacheable(value = "client.getAll")
    public Page<ClientDTO> getAll(@RequestParam(required = false) String name,
                                  @PageableDefault(sort = "dateCreated", direction = Sort.Direction.DESC, page = 0, size = 10) Pageable pagination) {

        Page<Client> clientList = null;

        if (name != null) {
            clientList = clienteRepository.findByNameContains(name, pagination);
        } else {
            clientList = clienteRepository.findAll(pagination);
        }

        return ClientDTO.convert(clientList);
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "client.getAll", allEntries = true)
    public ResponseEntity<ClientDTO> create(@RequestBody @Valid ClientDTO clientDTO) {

    }
}
