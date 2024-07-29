package com.myshop.demo.service;

import com.myshop.demo.dto.ProductMyPriceRequestDto;
import com.myshop.demo.dto.ProductRequestDto;
import com.myshop.demo.dto.ProductResponseDto;
import com.myshop.demo.entity.Product;
import com.myshop.demo.entity.User;
import com.myshop.demo.entity.UserRoleEnum;
import com.myshop.demo.naver.dto.ItemDto;
import com.myshop.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public static final int MIN_MY_PRICE = 100;

    public ProductResponseDto createProduct(ProductRequestDto requestDto, User user) {
        Product product = productRepository.save(new Product(requestDto , user));
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
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProduct(User user, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction  direction = isAsc? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        UserRoleEnum userRoleEnum = user.getRole();
        Page<Product> productList;

        if(userRoleEnum == UserRoleEnum.USER){
            productList = productRepository.findAllByUser(user, pageable);
        }else {
            productList = productRepository.findAllByUser(user, pageable);
        }
        return  productList.map(ProductResponseDto::new);
    }

    @Transactional
    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new NullPointerException("해당 상품 없습ㅂ니다")
        );
        product.updateByItemDto(itemDto);
    }

}
