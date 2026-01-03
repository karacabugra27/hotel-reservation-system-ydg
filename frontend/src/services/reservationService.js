import api from "./api";

export const createReservation = (payload) => {
  return api.post("/reservations/createReservation", payload);
};

export const getReservations = () => {
  return api.get("/reservations");
};

export const cancelReservation = (reservationId) => {
  return api.post(`/reservations/cancel/${reservationId}`);
};
