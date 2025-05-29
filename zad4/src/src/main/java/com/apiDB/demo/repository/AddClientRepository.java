package com.apiDB.demo.repository;

import com.apiDB.demo.DTO.AddClientDTO;
import jooq.tables.Client;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AddClientRepository {
    private final DSLContext create;
    public Long addClientRepository(AddClientDTO addClientDTO){
        Client client = Client.CLIENT;
        create.insertInto(client)
                .set(client.CLIENT_ID, addClientDTO.getClientId())
                .set(client.FULL_NAME, addClientDTO.getClientFullName())
                .set(client.EMAIL, addClientDTO.getEmail())
                .set(client.PHONE, addClientDTO.getPhoneNumber())
                .set(client.COMPANY, addClientDTO.getCompanyName())
                .set(client.STATUS, addClientDTO.getStatus())
                .set(client.READ_ONLY_FLAG, addClientDTO.getReadOnlyFlag())
                .execute();
        return addClientDTO.getClientId();
    }
}
