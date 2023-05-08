package com.example.springcqrs.cqrs.query;

import com.example.springcqrs.cqrs.model.PageResponse;

public interface IPageHandler<RESPONSE, REQUEST extends IPage<RESPONSE>> {
    PageResponse<RESPONSE> handle(REQUEST query);
}
