package com.thomasjayconsulting.springbootchatgptassistant.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ChatGPTCreateAssistantRequest {
    private String name;
    private String instructions;
    private List<Map<String, String>> tools = new ArrayList<>();
    private String model;
}
