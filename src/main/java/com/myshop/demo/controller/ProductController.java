package com.myshop.demo.controller;


import com.myshop.demo.dto.ProductMyPriceRequestDto;
import com.myshop.demo.dto.ProductRequestDto;
import com.myshop.demo.dto.ProductResponseDto;
import com.myshop.demo.security.UserDetailsImpl;
import com.myshop.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;


    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto , @AuthenticationPrincipal UserDetailsImpl userDetails){
        return  productService.createProduct(requestDto , userDetails.getUser());
    }
    @PutMapping("/products/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id , @RequestBody ProductMyPriceRequestDto productMyPriceRequestDto){
        return  productService.updateProduct(id,productMyPriceRequestDto);
    }

    @GetMapping("/products")
    public List<ProductResponseDto> getProduct(@AuthenticationPrincipal UserDetailsImpl userDetails){

        return  productService.getProduct(userDetails.getUser());
    }
    @GetMapping("/admin/products")
    public List<ProductResponseDto> getAllProduct(){

        return  productService.getAllProduct();
    }

}
