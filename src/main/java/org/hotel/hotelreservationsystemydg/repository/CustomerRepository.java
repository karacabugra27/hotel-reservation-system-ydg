package org.hotel.hotelreservationsystemydg.repository;

import org.hotel.hotelreservationsystemydg.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
