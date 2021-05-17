package com.ecommerce.service;

import com.ecommerce.dto.ClientDTO;
import com.ecommerce.model.Client;
import com.ecommerce.model.User;
import com.ecommerce.repository.ClientRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Client save(ClientDTO clientDTO) {

        Optional<User> user = userRepository.findByEmail(clientDTO.getEmail());

        Client client = new Client(user.get(), clientDTO.getName());

        clientRepository.save(client);

        return client;
    }

    @Transactional
    public Client update(Long id, ClientDTO clientDTO) {

        Optional<Client> optional = clientRepository.findById(id);

        if (!optional.isPresent()) {
            return null;
        }

        Client client = optional.get();
        client.setName(clientDTO.getName());
        clientRepository.save(client);

        User user = client.getUser();
        user.setEmail(clientDTO.getEmail());
        userRepository.save(user);

        return client;
    }

    @Transactional
    public boolean delete(Long id) {

        Optional<Client> optional = clientRepository.findById(id);

        if (!optional.isPresent()) {
            return false;
        }

        Client client = optional.get();
        User user = client.getUser();

        clientRepository.delete(client);
        userRepository.delete(user);

        return true;
    }
}
