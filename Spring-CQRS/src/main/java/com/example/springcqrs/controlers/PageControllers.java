package com.example.springcqrs.controlers;

import com.example.springcqrs.cqrs.controller.PageController;
import com.example.springcqrs.cqrs.model.PageResponse;
import com.example.springcqrs.dto.PageRequests;
import com.example.springcqrs.dto.PageResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PageControllers extends PageController<PageResponses, PageRequests> {

    @Override
    @PostMapping("/page")
    protected ResponseEntity<PageResponse<PageResponses>> executesPage(@RequestBody PageRequests pageRequests) {
        return execute(pageRequests);
    }
}
