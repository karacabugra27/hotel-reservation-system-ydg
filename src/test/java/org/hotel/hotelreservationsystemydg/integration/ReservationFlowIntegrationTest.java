package org.hotel.hotelreservationsystemydg.integration;

import jakarta.transaction.Transactional;
import org.hotel.hotelreservationsystemydg.enums.ReservationStatus;
import org.hotel.hotelreservationsystemydg.integration.config.TestSecurityConfig;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@Transactional
@Rollback
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

    @Test
    void rezervasyonOlusturuldugundaDtoAlanlariDonmeli() throws Exception {
        Room room = roomRepository.findAll().get(0);

        String requestJson = buildReservationRequestJson(
                "Ali", "Veli", "5551112233", room.getId());

        mockMvc.perform(post("/reservations/createReservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").isNumber())
                .andExpect(jsonPath("$.reservationCode").isString())
                .andExpect(jsonPath("$.customerName").value("Ali Veli"))
                .andExpect(jsonPath("$.roomNumber").value(room.getRoomNumber()))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void rezervasyonKoduIleSorguBasarili() throws Exception {
        Room room = roomRepository.findAll().get(0);

        String requestJson = buildReservationRequestJson(
                "Ayse", "Kara", "5552223344", room.getId());

        mockMvc.perform(post("/reservations/createReservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        Reservation reservation = reservationRepository.findAll().get(0);
        String reservationCode = reservation.getReservationCode();

        mockMvc.perform(get("/reservations/code/{code}", reservationCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationCode").value(reservationCode))
                .andExpect(jsonPath("$.customerName").value("Ayse Kara"));
    }

    @Test
    void rezervasyonKoduBulunamazsaBadRequestDoner() throws Exception {
        mockMvc.perform(get("/reservations/code/{code}", "000000"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Rezervasyon kodu bulunamadı."));
    }

    @Test
    void odayaGoreRezervasyonlarIptalleriDislar() throws Exception {
        Room room = roomRepository.findAll().get(0);
        LocalDate firstCheckIn = LocalDate.now().plusDays(1);
        LocalDate firstCheckOut = firstCheckIn.plusDays(2);
        LocalDate secondCheckIn = firstCheckOut.plusDays(2);
        LocalDate secondCheckOut = secondCheckIn.plusDays(2);

        String firstRequest = buildReservationRequestJson(
                "Test", "One", "5554445566", room.getId(), firstCheckIn, firstCheckOut);

        mockMvc.perform(post("/reservations/createReservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(firstRequest))
                .andExpect(status().isOk());

        Long firstReservationId = reservationRepository.findAll().get(0).getId();

        String secondRequest = buildReservationRequestJson(
                "Test", "Two", "5554445567", room.getId(), secondCheckIn, secondCheckOut);

        mockMvc.perform(post("/reservations/createReservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(secondRequest))
                .andExpect(status().isOk());

        mockMvc.perform(post("/reservations/cancel/{reservationId}", firstReservationId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/reservations/room/{roomId}", room.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("CREATED"))
                .andExpect(jsonPath("$[0].checkInDate").value(secondCheckIn.toString()))
                .andExpect(jsonPath("$[0].checkOutDate").value(secondCheckOut.toString()));
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
        Room room = roomRepository.findAll().get(0);

        String requestJson = buildReservationRequestJson(
                "Test", "Customer", "5553334455", room.getId());

        mockMvc.perform(post("/reservations/createReservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        return reservationRepository.findAll().get(0).getId();
    }

    private String buildReservationRequestJson(String firstName, String lastName, String phone, Long roomId) {
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        return buildReservationRequestJson(firstName, lastName, phone, roomId, checkIn, checkOut);
    }

    private String buildReservationRequestJson(String firstName, String lastName, String phone, Long roomId,
                                               LocalDate checkIn, LocalDate checkOut) {
        return """
                {
                  "firstName": "%s",
                  "lastName": "%s",
                  "phone": "%s",
                  "roomId": %d,
                  "checkInDate": "%s",
                  "checkOutDate": "%s"
                }
                """.formatted(firstName, lastName, phone, roomId, checkIn, checkOut);
    }
}
