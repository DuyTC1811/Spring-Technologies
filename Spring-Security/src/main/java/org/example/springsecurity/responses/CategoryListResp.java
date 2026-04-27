package org.example.springsecurity.responses;

import org.example.springsecurity.models.CategoryInfo;

import java.util.List;

public record CategoryListResp(List<CategoryInfo> items) {
}
