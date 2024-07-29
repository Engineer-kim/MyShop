package com.myshop.demo.controller;


import com.myshop.demo.dto.ProductMyPriceRequestDto;
import com.myshop.demo.dto.ProductRequestDto;
import com.myshop.demo.dto.ProductResponseDto;
import com.myshop.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;


    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto){
        return  productService.createProduct(requestDto);
    }
    @PutMapping("/products/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id , @RequestBody ProductMyPriceRequestDto productMyPriceRequestDto){
        return  productService.updateProduct(id,productMyPriceRequestDto);
    }

    @GetMapping("/products")
    public List<ProductResponseDto> getProduct(){
        return  productService.getProduct();
    }

}
