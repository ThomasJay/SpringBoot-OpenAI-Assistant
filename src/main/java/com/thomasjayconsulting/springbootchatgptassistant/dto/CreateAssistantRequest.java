package com.thomasjayconsulting.springbootchatgptassistant.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CreateAssistantRequest {
    private String name;
    private String instructions;
    private List<Map<String, String>> tools;
}
