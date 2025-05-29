package com.apiDB.demo.repository;

import com.apiDB.demo.DTO.GetClientDTO;
import com.apiDB.demo.DTO.GetInteractionDTO;
import jooq.tables.Client;
import jooq.tables.Interactions;
import jooq.tables.records.ClientRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GetClientRepository {
    private final DSLContext create;
    public GetClientDTO getClientById(Long id) {
        Client client = Client.CLIENT;
        ClientRecord record = create.selectFrom(client)
                .where(client.CLIENT_ID.eq(id))
                .fetchOne();

        if (record == null) {
            return null;
        }

        Interactions interactions = Interactions.INTERACTIONS;
        List<GetInteractionDTO> interactionsList = create.selectFrom(interactions)
                .where(interactions.CLIENT_ID.eq(record.getClientId()))
                .fetch()
                .map(recordInteraction -> {
                    GetInteractionDTO dtoInteraction = new GetInteractionDTO();
                    dtoInteraction.setInteractionsId(recordInteraction.getInteractionsId());
                    dtoInteraction.setClientId(recordInteraction.getClientId());
                    dtoInteraction.setTypeInteractions(recordInteraction.getTypeInteractions());
                    dtoInteraction.setDate(Timestamp.valueOf(recordInteraction.getDate()));
                    dtoInteraction.setText(recordInteraction.getNotes());
                    return dtoInteraction;
                });


        GetClientDTO dto = new GetClientDTO();
        dto.setClientId(record.getClientId());
        dto.setClientFullName(record.getFullName());
        dto.setEmail(record.getEmail());
        dto.setPhoneNumber(record.getPhone());
        dto.setCompanyName(record.getCompany());
        dto.setStatus(record.getStatus());
        dto.setReadOnlyFlag(record.getReadOnlyFlag());
        dto.setInteractions(interactionsList);

        //return new GetClientDTO();

        return dto;
    }
}
