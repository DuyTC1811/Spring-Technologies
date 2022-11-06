package com.example.springrediscache.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Range {
    private int from;
    private int to;
}
