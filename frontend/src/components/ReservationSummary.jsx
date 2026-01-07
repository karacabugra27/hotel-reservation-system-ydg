import React, { useState } from "react";
import { reservationStatusLabel, roomTypeLabel } from "../utils/roomLabels";

function ReservationSummary({ reservation }) {
  const [copyStatus, setCopyStatus] = useState("idle");
  if (!reservation) {
    return null;
  }

  const handleCopyCode = async () => {
    try {
      await navigator.clipboard.writeText(reservation.reservationCode || "");
      setCopyStatus("copied");
      window.setTimeout(() => setCopyStatus("idle"), 1500);
    } catch (error) {
      setCopyStatus("error");
      window.setTimeout(() => setCopyStatus("idle"), 2000);
    }
  };

  return (
    <div
      className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm"
      data-testid="reservation-summary"
    >
      <div className="grid gap-4">
        <div className="rounded-xl border border-indigo-100 bg-indigo-50 px-4 py-3">
          <p className="text-xs font-semibold uppercase tracking-[0.2em] text-indigo-600">
            Rezervasyon Kodu
          </p>
          <div className="mt-2 flex flex-wrap items-center gap-3">
            <p
              className="text-3xl font-semibold tracking-[0.2em] text-indigo-900"
              data-testid="reservation-code"
            >
              {reservation.reservationCode}
            </p>
            <button
              type="button"
              className="inline-flex items-center justify-center rounded-full border border-indigo-200 bg-white px-3 py-1 text-xs font-semibold text-indigo-700 transition hover:bg-indigo-100"
              onClick={handleCopyCode}
              data-testid="reservation-code-copy"
            >
              {copyStatus === "copied"
                ? "Kopyalandı"
                : copyStatus === "error"
                  ? "Kopyalanamadı"
                  : "Kodu Kopyala"}
            </button>
          </div>
        </div>

        <div className="grid gap-2 text-sm text-slate-700">
          <p>
            Oda: <strong>{reservation.roomNumber}</strong>
          </p>
          <p>
            Oda Tipi: <strong>{roomTypeLabel(reservation.roomTypeName)}</strong>
          </p>
          <p>
            Müşteri: <strong>{reservation.customerName}</strong>
          </p>
          <p>
            Tarihler:
            <span className="ml-2 inline-flex items-center gap-2 rounded-full border border-slate-200 bg-slate-50 px-3 py-1 text-xs font-semibold text-slate-700">
              <span>{reservation.checkInDate}</span>
              <span className="text-slate-400">→</span>
              <span>{reservation.checkOutDate}</span>
            </span>
          </p>
          <p>
            Durum:{" "}
            <strong>{reservationStatusLabel(reservation.status)}</strong>
          </p>
        </div>
      </div>
    </div>
  );
}

export default ReservationSummary;
