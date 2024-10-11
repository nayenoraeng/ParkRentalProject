package com.project.parkrental.parkList.service;

import com.project.parkrental.parkList.model.ParkList;
import com.project.parkrental.parkList.model.Product;
import com.project.parkrental.parkList.repository.ParkListRepository;
import com.project.parkrental.parkList.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ParkListRepository parkListRepository;

    // 모든 제품 목록을 반환하는 메서드
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 특정 공원의 제품 목록을 반환하는 메서드
    public List<Product> getProductsByParkId(Long parkId) {
        // 해당 공원의 정보를 가져옴
        ParkList park = parkListRepository.findById(parkId)
                .orElseThrow(() -> new RuntimeException("공원을 찾을 수 없습니다."));

        // 공원의 카테고리에 맞는 대여 불가능한 품목 목록을 가져옴
        List<String> unavailableProducts = getUnavailableProductsForCategory(park.getParkSe());

        // 대여 불가능한 품목을 제외한 제품 목록을 반환
        return productRepository.findByParkList(park).stream()
                .filter(product -> !unavailableProducts.contains(product.getProductName()))
                .collect(Collectors.toList());
    }

    // 제품 ID로 제품 정보를 가져오는 메서드 (제품 상세 페이지용)
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("해당 제품을 찾을 수 없습니다."));
    }

    // 대여 불가능한 제품 리스트를 반환하는 메서드
    public List<String> getUnavailableProductsForCategory(String parkCategory) {
        switch (parkCategory) {
            case "문화공원":
            case "역사공원":
            case "주제공원":
                return List.of("자전거", "배드민턴세트"); // 운동용품은 대여 불가능
            default:
                return List.of();
        }
    }

    // 특정 물품 ID로 재고 차감
    public boolean reduceProductQuantity(Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null && product.getQuantity() >= quantity) {
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);
            return true;
        }
        return false;
    }
}
