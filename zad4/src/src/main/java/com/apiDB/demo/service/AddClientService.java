package com.apiDB.demo.service;

import com.apiDB.demo.DTO.AddClientDTO;
import com.apiDB.demo.repository.AddClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddClientService {
    private final AddClientRepository addClientRepository;

    @CachePut(value = "clientsDTO", key = "#addClientDTO", condition = "#addClientDTO.readOnlyFlag == true")
    public Long addClient(AddClientDTO addClientDTO) {
        Long clientId = addClientRepository.addClientRepository(addClientDTO);
        return clientId;
    }
}
