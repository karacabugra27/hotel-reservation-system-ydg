import React from "react";
import { reservationStatusLabel, roomTypeLabel } from "../utils/roomLabels";

function ReservationSummary({ reservation }) {
  if (!reservation) {
    return null;
  }

  return (
    <div
      className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm"
      data-testid="reservation-summary"
    >
      <div className="grid gap-2 text-sm text-slate-700">
        <p className="font-semibold text-slate-900">
          Rezervasyon ID:{" "}
          <span data-testid="reservation-id">{reservation.reservationId}</span>
        </p>
        <p>
          Oda: <strong>{reservation.roomNumber}</strong>
        </p>
        <p>
          Oda Tipi: <strong>{roomTypeLabel(reservation.roomTypeName)}</strong>
        </p>
        <p>
          Musteri: <strong>{reservation.customerName}</strong>
        </p>
        <p>
          Tarihler:{" "}
          <strong>
            {reservation.checkInDate} - {reservation.checkOutDate}
          </strong>
        </p>
        <p>
          Durum: <strong>{reservationStatusLabel(reservation.status)}</strong>
        </p>
      </div>
    </div>
  );
}

export default ReservationSummary;
