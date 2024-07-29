package com.myshop.demo.service;

import com.myshop.demo.dto.ProductRequestDto;
import com.myshop.demo.dto.ProductResponseDto;
import com.myshop.demo.entity.Product;
import com.myshop.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = productRepository.save(new Product(requestDto));
        return new ProductResponseDto(product);
    }
}
