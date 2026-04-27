package org.example.springsecurity.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/cards")
@Tag(name = "CARDS", description = "API CARD")
public class CardController {

    @GetMapping
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public String getCards() {
        return "CardController - getCards - require USER_VIEW";
    }

    @GetMapping("/{cardId}")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public String getCardById(@PathVariable String cardId) {
        return "CardController - getCardById: " + cardId + " - require USER_VIEW";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public String createCard() {
        return "CardController - createCard - require USER_CREATE";
    }

    @PutMapping("/{cardId}")
    @PreAuthorize("hasAuthority('USER_EDIT')")
    public String updateCard(@PathVariable String cardId) {
        return "CardController - updateCard: " + cardId + " - require USER_EDIT";
    }

    @DeleteMapping("/{cardId}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public String deleteCard(@PathVariable String cardId) {
        return "CardController - deleteCard: " + cardId + " - require USER_DELETE";
    }
}