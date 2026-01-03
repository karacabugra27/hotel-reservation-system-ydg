import React, { useEffect, useState } from "react";
import RoomCard from "../components/RoomCard";
import { getRooms } from "../services/roomService";
import { roomTypeLabel } from "../utils/roomLabels";

function RoomsPage() {
  const [rooms, setRooms] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");
  const [typeFilter, setTypeFilter] = useState("all");

  useEffect(() => {
    getRooms()
      .then((res) => setRooms(res.data))
      .catch(() => setErrorMessage("Odalar yuklenirken hata olustu."))
      .finally(() => setIsLoading(false));
  }, []);

  const roomTypes = Array.from(
    new Set(rooms.map((room) => room.roomTypeName).filter(Boolean))
  );

  const filteredRooms = rooms.filter((room) => {
    return typeFilter === "all" || room.roomTypeName === typeFilter;
  });

  return (
    <div className="space-y-6" data-testid="rooms-page">
      <div className="rounded-2xl border border-slate-200 bg-white p-8 shadow-sm">
        <h1
          className="text-3xl font-semibold text-slate-900"
          data-testid="rooms-title"
        >
          Odalarımız
        </h1>
        <p
          className="mt-2 text-base text-slate-600"
          data-testid="rooms-subtitle"
        >
          Odalarımızı inceleyin ve size uygun olanı seçin
        </p>
      </div>

      <div
        className="grid gap-4 rounded-2xl border border-slate-200 bg-white p-6 shadow-sm md:grid-cols-2"
        data-testid="rooms-filters"
      >
        <label className="grid gap-2 text-sm text-slate-700">
          <span className="font-semibold text-slate-900">Oda Tipi</span>
          <select
            className="rounded-lg border border-slate-200 px-3 py-2 text-sm"
            value={typeFilter}
            onChange={(event) => setTypeFilter(event.target.value)}
            data-testid="filter-room-type"
          >
            <option value="all">Tüm Tipler</option>
            {roomTypes.map((type) => (
              <option key={type} value={type}>
                {roomTypeLabel(type)}
              </option>
            ))}
          </select>
        </label>
      </div>

      {isLoading && (
        <div
          className="rounded-lg border border-slate-200 bg-white px-4 py-3 text-sm text-slate-700 shadow-sm"
          data-testid="rooms-loading"
        >
          Yukleniyor...
        </div>
      )}

      {!isLoading && errorMessage && (
        <div
          className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700"
          data-testid="rooms-error"
        >
          {errorMessage}
        </div>
      )}

      {!isLoading && !errorMessage && filteredRooms.length === 0 && (
        <div
          className="rounded-lg border border-slate-200 bg-white px-4 py-3 text-sm text-slate-700"
          data-testid="rooms-empty"
        >
          Musait oda bulunamadi.
        </div>
      )}

      {!isLoading && !errorMessage && filteredRooms.length > 0 && (
        <ul className="space-y-4" data-testid="rooms-list">
          {filteredRooms.map((room) => (
            <RoomCard key={room.id} room={room} />
          ))}
        </ul>
      )}
    </div>
  );
}

export default RoomsPage;
