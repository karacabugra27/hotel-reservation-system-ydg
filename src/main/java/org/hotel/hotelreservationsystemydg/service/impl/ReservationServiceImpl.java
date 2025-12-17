package org.hotel.hotelreservationsystemydg.service.impl;

import org.hotel.hotelreservationsystemydg.dto.CheckInRequestDto;
import org.hotel.hotelreservationsystemydg.dto.CheckOutRequestDto;
import org.hotel.hotelreservationsystemydg.dto.ReservationRequestDto;
import org.hotel.hotelreservationsystemydg.dto.ReservationResponseDto;
import org.hotel.hotelreservationsystemydg.enums.PaymentStatus;
import org.hotel.hotelreservationsystemydg.enums.ReservationStatus;
import org.hotel.hotelreservationsystemydg.model.Customer;
import org.hotel.hotelreservationsystemydg.model.Payment;
import org.hotel.hotelreservationsystemydg.model.Reservation;
import org.hotel.hotelreservationsystemydg.model.Room;
import org.hotel.hotelreservationsystemydg.repository.CustomerRepository;
import org.hotel.hotelreservationsystemydg.repository.PaymentRepository;
import org.hotel.hotelreservationsystemydg.repository.ReservationRepository;
import org.hotel.hotelreservationsystemydg.repository.RoomRepository;
import org.hotel.hotelreservationsystemydg.service.ReservationService;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;


    public ReservationServiceImpl(ReservationRepository reservationRepository, RoomRepository roomRepository, CustomerRepository customerRepository, PaymentRepository paymentRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public ReservationResponseDto createReservation(ReservationRequestDto dto) {
        //? oda kontrolü --> müşteri kontrolü
        //?

        boolean roomOccupied = reservationRepository.existsByRoom_IdAndCheckOutAfterAndCheckInBefore(
                dto.getRoomId(), dto.getCheckOutDate(), dto.getCheckInDate()
        );

        if (roomOccupied) {
            throw new IllegalStateException("Belirtilen tarihlerde bu oda doludur!");
        }

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new IllegalStateException("Bu oda kaydı yok"));
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new IllegalStateException("Bu müşteri kaydı yok"));

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setRoom(room);
        reservation.setCheckIn(dto.getCheckInDate());
        reservation.setCheckOut(dto.getCheckOutDate());
        reservation.setReservationStatus(ReservationStatus.CREATED);

        reservationRepository.save(reservation);

        ReservationResponseDto response = new ReservationResponseDto();
        response.setReservationId(reservation.getId());
        response.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
        response.setCheckInDate(reservation.getCheckIn());
        response.setCheckOutDate(reservation.getCheckOut());
        response.setRoomNumber(room.getRoomNumber());
        response.setRoomTypeName(room.getRoomType().getName());

        return response;
    }

    @Override
    public void checkIn(CheckInRequestDto request) {
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(()-> new IllegalStateException("reservasyon bulunamadı"));

        Payment payment = paymentRepository.findByReservationId(request.getReservationId())
                .orElseThrow(()-> new IllegalStateException("ödeme yapılmamış"));

        if(payment.getPaymentStatus() != PaymentStatus.PAID) {
            throw new IllegalStateException("ödeme tamamlanmamış");
        }

        reservation.setReservationStatus(ReservationStatus.CHECKED_IN);
        reservationRepository.save(reservation);
    }

    @Override
    public void checkOut(CheckOutRequestDto request) {
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new IllegalStateException("reservasyon bulunamadı"));

        reservation.setReservationStatus(ReservationStatus.COMPLETED);
        reservationRepository.save(reservation);

    }
}
