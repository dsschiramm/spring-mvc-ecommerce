package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.ClientDTO;
import com.ecommerce.ecommerce.model.Client;
import com.ecommerce.ecommerce.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    @Cacheable(value = "client.getAll")
    public Page<ClientDTO> getAll(@RequestParam(required = false) String name,
                                  @PageableDefault(sort = "dateCreated", direction = Sort.Direction.DESC, page = 0, size = 10) Pageable pagination) {

        Page<Client> clientList = clientService.getAll(name, pagination);

        return ClientDTO.convert(clientList);
    }

    @PostMapping
    @CacheEvict(value = "client.getAll", allEntries = true)
    public ResponseEntity<ClientDTO> create(@RequestBody @Valid ClientDTO clientDTO, UriComponentsBuilder uriBuilder) {

        Client client = clientService.save(clientDTO);

        URI uri = uriBuilder.path("/client/${id}").buildAndExpand(client.getId()).toUri();
        return ResponseEntity.created(uri).body(new ClientDTO(client));
    }
}
