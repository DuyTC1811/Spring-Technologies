package org.example.springsecurity.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.springsecurity.models.CategoryInfo;

import java.util.List;

@Mapper
public interface ICategoryMapper {
    int insert(CategoryInfo category);

    int update(CategoryInfo category);

    int softDelete(@Param("categoryId") String categoryId);

    CategoryInfo findById(@Param("categoryId") String categoryId);

    List<CategoryInfo> findList(
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    long countList();

    boolean existsByName(@Param("name") String name);

    boolean existsByNameExcludeId(
            @Param("name") String name,
            @Param("categoryId") String categoryId
    );
}
