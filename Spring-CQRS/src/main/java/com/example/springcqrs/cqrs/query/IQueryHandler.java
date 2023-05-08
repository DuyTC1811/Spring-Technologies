package com.example.springcqrs.cqrs.query;

import com.example.springcqrs.cqrs.model.BaseResponse;

public interface IQueryHandler<RESPONSE, REQUEST extends IQuery<RESPONSE>> {
    BaseResponse<RESPONSE> handle(REQUEST query);
}
