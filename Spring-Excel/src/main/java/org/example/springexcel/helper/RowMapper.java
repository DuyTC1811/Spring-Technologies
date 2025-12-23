package org.example.springexcel.helper;

import org.dhatim.fastexcel.reader.Row;

@FunctionalInterface
public interface RowMapper<T> {
    T map(Row row);
}
