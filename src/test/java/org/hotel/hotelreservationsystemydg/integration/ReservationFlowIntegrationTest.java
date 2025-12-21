package org.hotel.hotelreservationsystemydg.integration;

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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class ReservationFlowIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void cleanState() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        paymentRepository.deleteAll();
        reservationRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @TestConfiguration
    static class TestSecurityConfiguration {
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }

    @Test
    void rezervasyonOlusturuldugundaDtoAlanlariDonmeli() throws Exception {
        Customer customer = createCustomer("Ali", "Veli");
        Room room = roomRepository.findAll().get(0);

        String requestJson = buildReservationRequestJson(customer.getId(), room.getId());

        mockMvc.perform(post("/reservations/createReservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").isNumber())
                .andExpect(jsonPath("$.customerName").value("Ali Veli"))
                .andExpect(jsonPath("$.roomNumber").value(room.getRoomNumber()))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void odemeYapilmadanCheckInYapilamaz() throws Exception {
        Long reservationId = createReservationViaApi();

        String checkInRequestJson = "{\"reservationId\":%d}".formatted(reservationId);

        mockMvc.perform(post("/reservations/check-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(checkInRequestJson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void odemeSonrasiCheckInVeCheckOutAkisiBasarili() throws Exception {
        Long reservationId = createReservationViaApi();

        String paymentJson = """
                {"reservationId":%d,"price":400}
                """.formatted(reservationId);

        mockMvc.perform(post("/payment/make-payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));

        String checkInJson = "{\"reservationId\":%d}".formatted(reservationId);

        mockMvc.perform(post("/reservations/check-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(checkInJson))
                .andExpect(status().isOk());

        String checkOutJson = "{\"reservationId\":%d}".formatted(reservationId);

        mockMvc.perform(post("/reservations/check-out")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(checkOutJson))
                .andExpect(status().isOk());

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow();
        assertThat(reservation.getReservationStatus()).isEqualTo(ReservationStatus.COMPLETED);
    }

    private Long createReservationViaApi() throws Exception {
        Customer customer = createCustomer("Test", "Customer");
        Room room = roomRepository.findAll().get(0);

        String requestJson = buildReservationRequestJson(customer.getId(), room.getId());

        mockMvc.perform(post("/reservations/createReservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        return reservationRepository.findAll().get(0).getId();
    }

    private Customer createCustomer(String firstName, String lastName) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setNumber("55555");
        return customerRepository.save(customer);
    }

    private String buildReservationRequestJson(Long customerId, Long roomId) {
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
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
