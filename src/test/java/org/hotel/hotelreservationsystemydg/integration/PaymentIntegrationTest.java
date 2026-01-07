package org.hotel.hotelreservationsystemydg.integration;

import jakarta.transaction.Transactional;
import org.hotel.hotelreservationsystemydg.enums.ReservationStatus;
import org.hotel.hotelreservationsystemydg.integration.config.TestSecurityConfig;
import org.hotel.hotelreservationsystemydg.model.Customer;
import org.hotel.hotelreservationsystemydg.model.Reservation;
import org.hotel.hotelreservationsystemydg.model.Room;
import org.hotel.hotelreservationsystemydg.repository.CustomerRepository;
import org.hotel.hotelreservationsystemydg.repository.PaymentRepository;
import org.hotel.hotelreservationsystemydg.repository.ReservationRepository;
import org.hotel.hotelreservationsystemydg.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@Transactional
@Rollback
class PaymentIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        paymentRepository.deleteAll();
        reservationRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void priceAlanBosOlursaValidationHatasiDoner() throws Exception {
        Reservation reservation = createReservation();

        String requestBody = """
                {
                  "reservationId": %d
                }
                """.formatted(reservation.getId());

        mockMvc.perform(post("/payment/make-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.price").value("ödeme tutar zorunlu"));
    }

    @Test
    void olmayanRezervasyonaOdemeIsConflictDoner() throws Exception {
        String requestBody = """
                {
                  "reservationId": 999999,
                  "price": 250
                }
                """;

        mockMvc.perform(post("/payment/make-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("rezervasyon bulunamadı"));
    }

    private Reservation createReservation() {
        Customer customer = new Customer();
        customer.setFirstName("Payment");
        customer.setLastName("Test");
        customer.setNumber("555-321");
        Customer savedCustomer = customerRepository.save(customer);

        Room room = roomRepository.findAll().get(0);

        Reservation reservation = new Reservation();
        reservation.setCustomer(savedCustomer);
        reservation.setRoom(room);
        reservation.setCheckIn(LocalDate.now().plusDays(1));
        reservation.setCheckOut(LocalDate.now().plusDays(2));
        reservation.setReservationCode("123456");
        reservation.setReservationStatus(ReservationStatus.CREATED);
        return reservationRepository.save(reservation);
    }
}
