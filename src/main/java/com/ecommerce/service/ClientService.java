package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.ClientDTO;
import com.ecommerce.ecommerce.model.Client;
import com.ecommerce.ecommerce.model.Usuario;
import com.ecommerce.ecommerce.repository.ClientRepository;
import com.ecommerce.ecommerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Page<Client> getAll(String name, Pageable pagination) {

        Page<Client> clientList = null;

        if (name != null) {
            clientList = clienteRepository.findByNameContains(name, pagination);
        } else {
            clientList = clienteRepository.findAll(pagination);
        }

        return clientList;
    }

    @Transactional
    public Client save(ClientDTO clientDTO) {

        Optional<Usuario> usuario = usuarioRepository.findByEmail(clientDTO.getEmail());

        Client client = new Client(usuario.get(), clientDTO.getName());

        clienteRepository.save(client);

        return client;
    }
}
