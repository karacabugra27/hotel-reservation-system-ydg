import React, { useEffect, useState } from "react";
import { getRooms } from "../services/roomService";

function RoomsPage() {
  const [rooms, setRooms] = useState([]);

  useEffect(() => {
    getRooms()
      .then((res) => setRooms(res.data))
      .catch((err) => console.error(err));
  }, []);
  return (
    <div>
      <h2>Odalar</h2>
      {rooms.length === 0 && <p>Odalar bulunamadı</p>}

      <ul>
        {rooms.map((room) => (
          <li key={room.id}>
            Oda {room.roomNumber} - {room.roomStatus}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default RoomsPage;
