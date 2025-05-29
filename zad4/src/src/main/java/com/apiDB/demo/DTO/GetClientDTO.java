package com.apiDB.demo.DTO;

import lombok.Data;

import java.util.List;

@Data
public class GetClientDTO {
    private Long clientId;
    private String clientFullName;
    private String email;
    private String phoneNumber;
    private Long companyName;
    private String status;
    private Boolean readOnlyFlag;

    private List<GetInteractionDTO> interactions;
}
