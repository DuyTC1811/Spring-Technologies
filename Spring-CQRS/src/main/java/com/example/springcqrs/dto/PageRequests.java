package com.example.springcqrs.dto;

import com.example.springcqrs.cqrs.query.IPage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequests implements IPage<PageResponses> {
    private int limit;
    private int offset;
    private String name;
}
