package com.project.parkrental.controller;

import com.project.parkrental.parkList.service.ProductService;
import com.project.parkrental.reservation.ReservationService;
import com.project.parkrental.security.Service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SellerService sellerService;

//    @PostMapping("/create")
//    public ResponseEntity<?> createReservation(@RequestBody Map<String, Object> payload) {
//        try {
//            String username = SecurityContextHolder.getContext().getAuthentication().getName();
//            String reserveNum = (String) payload.get("reserveNum");
//            LocalDate reserveDate = LocalDate.parse((String) payload.get("reserveDate"));
//            LocalTime reserveTime = LocalTime.parse((String) payload.get("reserveTime"));
//            Long costAll = Long.parseLong(payload.get("costAll").toString());
//
//            // 상품 및 판매자 정보 가져오기 (예시)
//            Product product = productService.findByName((String) payload.get("productName"));
//            Seller seller = sellerService.findByBusinessName((String) payload.get("businessName"));
//
//            Reservation reservation = reservationService.createReservation(username, reserveNum, reserveDate, reserveTime, costAll, product, seller);
//
//            return ResponseEntity.ok(reservation);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 처리 중 오류 발생");
//        }
//    }

    // 결제 상태 업데이트 API
    @PostMapping("/update-payment")
    public ResponseEntity<?> updatePayment(@RequestBody Map<String, Object> payload) {
        try {
            String reserveNum = (String) payload.get("reserveNum");
            int isPaid = Integer.parseInt(payload.get("isPaid").toString());

            reservationService.updatePaymentStatus(reserveNum, isPaid);
            return ResponseEntity.ok("결제 상태가 업데이트되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 상태 업데이트 중 오류 발생");
        }
    }
}
