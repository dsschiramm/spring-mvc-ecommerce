package com.ecommerce.dto;

import com.ecommerce.model.Client;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import java.util.Date;

@Getter
@Setter
public class ClientDTO {

    private Long id;

    private UserDTO userDTO;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private Date dateCreated;

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.userDTO.setEmail(client.getUser().getEmail());
        this.dateCreated = client.getDateCreated();
    }

    public static Page<ClientDTO> convert(Page<Client> clientList) {
        return clientList.map(ClientDTO::new);
    }
}
