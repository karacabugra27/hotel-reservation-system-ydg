import React, { useEffect, useState } from "react";
import { cancelReservation, getReservations } from "../services/reservationService";
import { reservationStatusLabel } from "../utils/roomLabels";

function ReservationListPage() {
  const [reservations, setReservations] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    getReservations()
      .then((response) => setReservations(response.data))
      .catch(() => setErrorMessage("Rezervasyonlar yuklenemedi."))
      .finally(() => setIsLoading(false));
  }, []);

  const handleCancel = async (reservationId) => {
    try {
      const response = await cancelReservation(reservationId);
      setReservations((prev) =>
        prev.map((reservation) =>
          reservation.reservationId === reservationId ? response.data : reservation
        )
      );
    } catch (error) {
      setErrorMessage("Rezervasyon iptal edilemedi.");
    }
  };

  return (
    <div className="space-y-6" data-testid="reservations-page">
      <div className="rounded-2xl border border-slate-200 bg-white p-8 shadow-sm">
        <h1 className="text-3xl font-semibold text-slate-900" data-testid="reservations-title">
          Rezervasyon Kayitlari
        </h1>
        <p className="mt-2 text-base text-slate-600">
          Tum rezervasyonlarinizin guncel durumunu goruntuleyin.
        </p>
      </div>

      {isLoading && (
        <div
          className="rounded-lg border border-slate-200 bg-white px-4 py-3 text-sm text-slate-700 shadow-sm"
          data-testid="reservations-loading"
        >
          Yukleniyor...
        </div>
      )}

      {!isLoading && errorMessage && (
        <div
          className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700"
          data-testid="reservations-error"
        >
          {errorMessage}
        </div>
      )}

      {!isLoading && !errorMessage && reservations.length === 0 && (
        <div
          className="rounded-lg border border-slate-200 bg-white px-4 py-3 text-sm text-slate-700 shadow-sm"
          data-testid="reservations-empty"
        >
          Henuz rezervasyon bulunamadi.
        </div>
      )}

      {!isLoading && !errorMessage && reservations.length > 0 && (
        <div
          className="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm"
          data-testid="reservations-table"
        >
          <table className="min-w-full text-left text-sm">
            <thead className="bg-slate-50 text-xs uppercase tracking-wide text-slate-500">
              <tr>
                <th className="px-4 py-3">ID</th>
                <th className="px-4 py-3">Oda</th>
                <th className="px-4 py-3">Tarih</th>
                <th className="px-4 py-3">Durum</th>
                <th className="px-4 py-3">Islem</th>
              </tr>
            </thead>
            <tbody>
              {reservations.map((reservation) => {
                const isCancelled = reservation.status === "CANCELLED";
                const isCompleted = reservation.status === "COMPLETED";
                return (
                  <tr
                    key={reservation.reservationId}
                    className="border-t border-slate-100"
                    data-testid={`reservation-row-${reservation.reservationId}`}
                  >
                    <td className="px-4 py-3">{reservation.reservationId}</td>
                    <td className="px-4 py-3">{reservation.roomNumber}</td>
                    <td className="px-4 py-3">
                      {reservation.checkInDate} - {reservation.checkOutDate}
                    </td>
                    <td className="px-4 py-3">
                      {reservationStatusLabel(reservation.status)}
                    </td>
                    <td className="px-4 py-3">
                      <button
                        type="button"
                        className="inline-flex items-center justify-center rounded-lg border border-red-200 px-3 py-1 text-xs font-semibold text-red-600 transition hover:bg-red-50 disabled:cursor-not-allowed disabled:opacity-60"
                        data-testid={`reservation-cancel-${reservation.reservationId}`}
                        onClick={() => handleCancel(reservation.reservationId)}
                        disabled={isCancelled || isCompleted}
                      >
                        Iptal Et
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default ReservationListPage;
