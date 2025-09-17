package com.github.inventorysaleshub.dto;

public class KpiDTO {
    private String name;   // KPI name
    private Double value;  // KPI value

    // Default constructor
    public KpiDTO() {}

    // All-args constructor
    public KpiDTO(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    // Getters and setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }
    public void setValue(Double value) {
        this.value = value;
    }
}
