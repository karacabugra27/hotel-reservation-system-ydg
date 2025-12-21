package org.hotel.hotelreservationsystemydg.integration;

import org.hotel.hotelreservationsystemydg.integration.config.TestSecurityConfig;
import org.hotel.hotelreservationsystemydg.model.Customer;
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
class ReservationErrorIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoomRepository roomRepository;

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
    void ayniOdaIcinCakisanRezervasyonIsConflictDoner() throws Exception {
        Customer customer = createCustomer("Cakisan", "Rezervasyon");
        Room room = roomRepository.findAll().get(0);
        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = checkIn.plusDays(4);

        String firstRequest = buildReservationRequest(customer.getId(), room.getId(), checkIn, checkOut);

        mockMvc.perform(post("/reservations/createReservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(firstRequest))
                .andExpect(status().isOk());

        String overlappingRequest = buildReservationRequest(customer.getId(), room.getId(),
                checkIn.plusDays(1), checkOut.minusDays(1));

        mockMvc.perform(post("/reservations/createReservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(overlappingRequest))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Belirtilen tarihlerde bu oda doludur!"));
    }

    @Test
    void olmayanRezervasyonCheckOutIsConflictDoner() throws Exception {
        String requestBody = """
                {"reservationId":999999}
                """;

        mockMvc.perform(post("/reservations/check-out")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("reservasyon bulunamadı"));
    }

    private Customer createCustomer(String firstName, String lastName) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setNumber("555-123");
        return customerRepository.save(customer);
    }

    private String buildReservationRequest(Long customerId, Long roomId, LocalDate checkIn, LocalDate checkOut) {
        return """
                {
                  "customerId": %d,
                  "roomId": %d,
                  "checkInDate": "%s",
                  "checkOutDate": "%s"
                }
                """.formatted(customerId, roomId, checkIn, checkOut);
    }
}
