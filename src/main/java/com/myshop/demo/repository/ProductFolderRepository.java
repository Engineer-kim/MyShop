package com.myshop.demo.repository;

import com.myshop.demo.entity.ProductFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductFolderRepository extends JpaRepository<ProductFolder, Long>{

}
