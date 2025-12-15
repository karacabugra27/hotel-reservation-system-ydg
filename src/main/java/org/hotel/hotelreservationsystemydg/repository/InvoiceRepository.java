package org.hotel.hotelreservationsystemydg.repository;

import org.hotel.hotelreservationsystemydg.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
