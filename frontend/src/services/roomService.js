import api from "./api";

export const getRooms = () => {
  return api.get("/room/getAvailableRooms");
};

export const getRoomById = (roomId) => {
  return api.get(`/room/${roomId}`);
};

export const getAvailableRoomsByDates = (checkIn, checkOut) => {
  return api.get("/room/available", {
    params: {
      checkIn,
      checkOut,
    },
  });
};
