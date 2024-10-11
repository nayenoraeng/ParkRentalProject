package com.project.parkrental.parkList.repository;

import com.project.parkrental.parkList.model.ParkList;
import com.project.parkrental.parkList.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByParkListParkId(Long parkId);
    List<Product> findByParkListAndProductName(ParkList parkList, String productName);
    List<Product> findByParkList(ParkList park);
}

