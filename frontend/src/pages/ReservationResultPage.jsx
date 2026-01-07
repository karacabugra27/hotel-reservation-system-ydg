import React from "react";
import { Link, useLocation } from "react-router-dom";
import ReservationSummary from "../components/ReservationSummary";

function ReservationResultPage() {
  const location = useLocation();
  const result = location.state;
  const isSuccess = result?.success;
  const data = result?.data;
  const message =
    result?.message || "Sonuç bulunamadı. Lütfen yeniden deneyin.";

  return (
    <div className="space-y-6" data-testid="reservation-result-page">
      <div className="rounded-2xl border border-slate-200 bg-white p-8 shadow-sm">
        <h1
          className="text-3xl font-semibold text-slate-900"
          data-testid="reservation-result-title"
        >
          Rezervasyon Sonucu
        </h1>
        <p className="mt-2 text-base text-slate-600">
          Rezervasyon isteğinizin sonucu burada görüntülenir.
        </p>
      </div>

      {isSuccess ? (
        <div
          className="rounded-lg border border-emerald-200 bg-emerald-50 px-4 py-3 text-sm text-emerald-700"
          data-testid="reservation-success"
        >
          Rezervasyonunuz başarıyla alındı.
        </div>
      ) : (
        <div
          className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700"
          data-testid="reservation-error-result"
        >
          {message}
        </div>
      )}

      {isSuccess && (
        <div data-testid="reservation-details">
          <ReservationSummary reservation={data} />
        </div>
      )}

      <div className="flex flex-col gap-3 sm:flex-row">
        <Link
          className="inline-flex items-center justify-center rounded-lg border border-slate-300 px-4 py-2 text-sm font-semibold text-slate-700 transition hover:bg-slate-100"
          to="/rooms"
          data-testid="back-to-rooms"
        >
          Odalara Don
        </Link>
        <Link
          className="inline-flex items-center justify-center rounded-lg border border-indigo-200 px-4 py-2 text-sm font-semibold text-indigo-700 transition hover:bg-indigo-50"
          to="/reservation-lookup"
          data-testid="go-to-reservation-lookup"
        >
          Rezervasyon Sorgula
        </Link>
      </div>
    </div>
  );
}

export default ReservationResultPage;
