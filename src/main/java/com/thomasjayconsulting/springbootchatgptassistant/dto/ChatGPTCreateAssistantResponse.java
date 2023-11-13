package com.thomasjayconsulting.springbootchatgptassistant.dto;

import lombok.Data;

@Data
public class ChatGPTCreateAssistantResponse {
    private String id;
    private String name;
    private String model;
}
