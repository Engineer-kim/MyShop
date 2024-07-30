package com.myshop.demo.service;

import com.myshop.demo.dto.ProductMyPriceRequestDto;
import com.myshop.demo.dto.ProductRequestDto;
import com.myshop.demo.dto.ProductResponseDto;
import com.myshop.demo.entity.*;
import com.myshop.demo.naver.dto.ItemDto;
import com.myshop.demo.repository.FolderRepository;
import com.myshop.demo.repository.ProductFolderRepository;
import com.myshop.demo.repository.ProductRepository;
import com.myshop.demo.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductFolderRepository productFolderRepository;

    private final FolderRepository folderRepository;

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

    public void addFolders(Long productId, Long folderId ,User user) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new NullPointerException("해당상품이 존재 하지 않음"));

        Folder folder = folderRepository.findById(folderId).orElseThrow(() ->
                new NullPointerException("해당 폴더 존재 안함"));

        if(!product.getUser().getId().equals(user.getId()) ||
        !folder.getUser().getId().equals(user.getId())){
             throw  new IllegalArgumentException("회원님의 관심상품 아님 , 회원님 폴더가 아님");
        }

        Optional<ProductFolder> overlapFolder = productRepository.findByProductAndFolder(product, folder);

        if(overlapFolder.isPresent()){
            throw new IllegalArgumentException("중복된 폴더");
        }

        productFolderRepository.save(new ProductFolder(product, folder));

    }
}
