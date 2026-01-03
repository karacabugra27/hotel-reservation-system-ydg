import React from "react";
import { Link } from "react-router-dom";
import { roomImageByType, roomTypeLabel } from "../utils/roomLabels";

function RoomCard({ room }) {
  const imageSrc = roomImageByType(room.roomTypeName);

  return (
    <li
      className="flex flex-col gap-4 rounded-xl border border-slate-200 bg-white p-5 shadow-sm transition hover:-translate-y-0.5 hover:shadow-md"
      data-testid={`room-card-${room.id}`}
    >
      <img
        src={imageSrc}
        alt={`Oda ${room.roomNumber}`}
        className="h-40 w-full rounded-lg object-cover"
        loading="lazy"
        data-testid={`room-image-${room.id}`}
      />
      <div className="flex items-center justify-between gap-4">
        <div>
          <p className="text-xs uppercase tracking-[0.2em] text-slate-400">
            Oda Tipi • Oda Numarası
          </p>
          <p
            className="mt-1 text-lg font-semibold text-slate-900"
            data-testid="room-type"
          >
            {roomTypeLabel(room.roomTypeName)} • {room.roomNumber}
          </p>
        </div>
        <Link
          to={`/rooms/${room.id}`}
          className="inline-flex items-center justify-center rounded-lg border border-slate-300 px-4 py-2 text-sm font-semibold text-slate-700 transition hover:bg-slate-100"
          data-testid={`room-detail-button-${room.id}`}
        >
          Detay
        </Link>
      </div>
    </li>
  );
}

export default RoomCard;
