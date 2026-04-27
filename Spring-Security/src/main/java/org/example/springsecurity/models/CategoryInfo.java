package org.example.springsecurity.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryInfo {
    private String categoryId;
    private String name;
    private String description;
    private Integer displayOrder;
    private Boolean enabled;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
