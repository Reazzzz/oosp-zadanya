package com.apiDB.demo.service;

import com.apiDB.demo.DTO.GetClientDTO;
import com.apiDB.demo.repository.GetClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetClientService {
    private final GetClientRepository getClientRepository;
    private final CacheManager cacheManager;

    //@Cacheable(value = "clientsDTO", key = "#id")
    public GetClientDTO getClient(Long id) {

        Cache cache = cacheManager.getCache("clientsDTO");
        GetClientDTO cached = cache.get(id, GetClientDTO.class);

        if (cached != null) {
            System.out.println("Из кэша");
            return cached;
        }
        GetClientDTO answer = getClientRepository.getClientById(id);

        if (answer.getReadOnlyFlag()) {
            System.out.println("Загрузка из БД...");
            cache.put(id, answer);
        }

        return answer;
    }
}
