package com.apiDB.demo.DTO;

import lombok.Data;

@Data
public class AddClientDTO {
    private Long clientId;
    private String clientFullName;
    private String email;
    private String phoneNumber;
    private Long companyName;
    private String status;
    private Boolean readOnlyFlag;
}
