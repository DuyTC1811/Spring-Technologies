package com.example.springasync.entitys;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Employee {
    private int id;
    private String name;
    private String role;
}
