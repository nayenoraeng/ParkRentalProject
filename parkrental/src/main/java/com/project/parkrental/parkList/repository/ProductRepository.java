package com.project.parkrental.parkList.repository;

import com.project.parkrental.parkList.model.ParkList;
import com.project.parkrental.parkList.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // parkId로 Product 리스트 조회
    List<Product> findByParkListParkId(Long parkId);

    // ParkList와 productName으로 Product 조회
    List<Product> findByParkListAndProductName(ParkList parkList, String productName);

    // ParkList로 Product 리스트 조회
    List<Product> findByParkList(ParkList park);

    // 네이티브 쿼리를 사용하여 특정 parkId의 유니크한 Product 리스트 조회
    @Query(value = "SELECT DISTINCT p.* FROM product p WHERE p.parkid = :parkId", nativeQuery = true)
    List<Product> findUniqueProductsByPark(@Param("parkId") Long parkId);

    // productNum과 parkId로 특정 제품과 공원 정보를 조회
    @Query("SELECT p FROM Product p JOIN FETCH p.parkList WHERE p.productNum = :productNum AND p.parkList.parkId = :parkId")
    Optional<Product> findProductWithParkList(@Param("productNum") String productNum, @Param("parkId") Long parkId);

}
