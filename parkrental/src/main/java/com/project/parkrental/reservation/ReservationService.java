package com.project.parkrental.reservation;

import com.project.parkrental.parkList.model.Product;
import com.project.parkrental.security.DTO.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation createReservation(String username, String reserveNum, LocalDate reserveDate, LocalTime reserveTime, Long costAll, Product product, Seller seller) {
        Reservation reservation = new Reservation();
        reservation.setUsername(username);
        reservation.setReserveNum(reserveNum);
        reservation.setReserveDate(reserveDate);
        reservation.setReserveTime(reserveTime);
        reservation.setCostAll(costAll);
        reservation.setProduct(product);
        reservation.setSeller(seller);
        reservation.setIsPaid(0); // 기본 결제 상태는 결제 안됨으로 설정

        return reservationRepository.save(reservation);
    }

    // 결제 상태 업데이트 로직 (결제 완료 처리)
    public void updatePaymentStatus(String reserveNum, int isPaid) {
        Reservation reservation = reservationRepository.findByReserveNum(reserveNum);
        if (reservation != null) {
            reservation.setIsPaid(isPaid);
            reservationRepository.save(reservation);
        }
    }
}
