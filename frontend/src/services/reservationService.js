import api from "./api";

export const createReservation = (payload) => {
  return api.post("/reservations/createReservation", payload);
};

export const getReservationsByRoomId = (roomId) => {
  return api.get(`/reservations/room/${roomId}`);
};

export const getReservationByCode = (reservationCode) => {
  return api.get(`/reservations/code/${reservationCode}`);
};
