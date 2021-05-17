package com.ecommerce.controller;

import com.ecommerce.dto.ClientDTO;
import com.ecommerce.model.Client;
import com.ecommerce.repository.ClientRepository;
import com.ecommerce.service.ClientService;
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
import java.util.Optional;

@RestController
@RequestMapping(value = "/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    @Cacheable(value = "client.getAll")
    public Page<ClientDTO> getAll(@RequestParam(required = false) String name,
                                  @PageableDefault(sort = "dateCreated", direction = Sort.Direction.DESC, page = 0, size = 10) Pageable pagination) {

        Page<Client> clientList = null;

        if (name != null) {
            clientList = clientRepository.findByNameContains(name, pagination);
        } else {
            clientList = clientRepository.findAll(pagination);
        }

        return ClientDTO.convert(clientList);
    }

    @PostMapping
    @CacheEvict(value = "client.getAll", allEntries = true)
    public ResponseEntity<ClientDTO> create(@RequestBody @Valid ClientDTO clientDTO, UriComponentsBuilder uriBuilder) {

        Client client = clientService.save(clientDTO);

        URI uri = uriBuilder.path("/client/${id}").buildAndExpand(client.getId()).toUri();
        return ResponseEntity.created(uri).body(new ClientDTO(client));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> get(@PathVariable Long id) {

        Optional<Client> optional = clientRepository.findById(id);

        if (optional.isPresent()) {
            return ResponseEntity.ok(new ClientDTO(optional.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "client.getAll", allEntries = true)
    public ResponseEntity<ClientDTO> update(@PathVariable Long id, @RequestBody @Valid ClientDTO clientDTO) {

        Client client = clientService.update(id, clientDTO);

        if (client != null) {
            return ResponseEntity.ok(new ClientDTO(client));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "client.getAll", allEntries = true)
    public ResponseEntity<?> delete(@PathVariable Long id) {

        boolean found = clientService.delete(id);

        if (found) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
