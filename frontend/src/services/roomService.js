import api from "./api";

export const getRooms = () => {
  return api.get("/room/getAvailableRooms");
};
