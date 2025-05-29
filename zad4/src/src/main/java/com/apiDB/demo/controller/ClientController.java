package com.apiDB.demo.controller;

import com.apiDB.demo.DTO.AddClientDTO;
import com.apiDB.demo.DTO.GetClientDTO;
import com.apiDB.demo.service.AddClientService;
import com.apiDB.demo.service.GetClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client")
public class ClientController {
    private final AddClientService addClientService;
    private final GetClientService getClientService;

    @GetMapping("/get/{id}")
    public GetClientDTO getClient(@PathVariable Long id) {
        GetClientDTO getClientDTO = getClientService.getClient(id);
        return getClientDTO;
    }

    @PostMapping("/add")
    public String addClient(@RequestBody AddClientDTO client) {
        Long request = addClientService.addClient(client);
        return "Добавлен клиент с id " + request;
    }
}
