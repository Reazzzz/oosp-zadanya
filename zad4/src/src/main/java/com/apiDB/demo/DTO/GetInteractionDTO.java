package com.apiDB.demo.DTO;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class GetInteractionDTO {
    private Long interactionsId;
    private Long clientId;
    private String typeInteractions;
    private Timestamp date;
    private String text;
}
