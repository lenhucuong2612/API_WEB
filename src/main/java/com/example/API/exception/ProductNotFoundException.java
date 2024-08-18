package com.example.API.exception;

import com.example.API.repository.ProductRepository;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message){
        super(message);
    }
}
