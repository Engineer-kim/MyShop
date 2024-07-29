package com.myshop.demo.service;

import com.myshop.demo.dto.ProductMyPriceRequestDto;
import com.myshop.demo.dto.ProductRequestDto;
import com.myshop.demo.dto.ProductResponseDto;
import com.myshop.demo.entity.Product;
import com.myshop.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public static final int MIN_MY_PRICE = 100;

    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = productRepository.save(new Product(requestDto));
        return new ProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductMyPriceRequestDto requestDto) {
        int myprice = requestDto.getMyprice();
        if(myprice < MIN_MY_PRICE){
            throw new IllegalArgumentException("유효하지 않는 가격입니다   "+   MIN_MY_PRICE + "이상으로 입력하시오");
        }

        Product product = productRepository.findById(id).orElseThrow(() -> new NullPointerException("해당 상품 없음"));

        product.update(requestDto);

        return new ProductResponseDto(product);
    }

    public List<ProductResponseDto> getProduct() {
        List<Product> productList = productRepository.findAll();
        List<ProductResponseDto> responseDto = new ArrayList<>();
        for (Product product : productList) {
            responseDto.add(new ProductResponseDto(product));
        }
        return  responseDto;
    }
}
