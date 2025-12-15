package org.hotel.hotelreservationsystemydg.repository;

import org.hotel.hotelreservationsystemydg.enums.RoomStatus;
import org.hotel.hotelreservationsystemydg.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByStatus(RoomStatus status);

    boolean existByRoomNumber(String roomNumber);

}
