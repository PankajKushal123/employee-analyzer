package org.example.parser;

import org.example.model.Employee;

import java.util.List;

public interface DataParser<T> {
    List<T> parse(String filePath) throws Exception;
}
