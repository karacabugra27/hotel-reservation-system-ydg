import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { getReservationByCode } from "../services/reservationService";

function ReservationLookupPage() {
  const [reservationCode, setReservationCode] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    const trimmedCode = reservationCode.trim();
    setErrorMessage("");

    if (!trimmedCode) {
      setErrorMessage("Lütfen rezervasyon kodunu girin.");
      return;
    }

    if (!/^[0-9]{6}$/.test(trimmedCode)) {
      setErrorMessage("Rezervasyon kodu 6 haneli olmalı.");
      return;
    }

    try {
      setIsSubmitting(true);
      const response = await getReservationByCode(trimmedCode);
      navigate("/reserve/result", {
        state: { success: true, data: response.data },
      });
    } catch (error) {
      const message =
        error?.response?.data?.error ||
        "Rezervasyon bulunamadı. Lütfen kodu kontrol edin.";
      setErrorMessage(message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="space-y-6" data-testid="reservation-lookup-page">
      <div className="rounded-2xl border border-slate-200 bg-white p-8 shadow-sm">
        <h1 className="text-3xl font-semibold text-slate-900">
          Rezervasyon Sorgula
        </h1>
        <p className="mt-2 text-base text-slate-600">
          Size verilen rezervasyon kodunu girerek sonucu görüntüleyin.
        </p>
      </div>

      {errorMessage && (
        <div
          className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700"
          data-testid="reservation-lookup-error"
        >
          {errorMessage}
        </div>
      )}

      <form
        className="grid gap-4 rounded-2xl border border-slate-200 bg-white p-6 shadow-sm"
        onSubmit={handleSubmit}
        data-testid="reservation-lookup-form"
      >
        <label className="grid gap-2 text-sm text-slate-700">
          <span className="font-semibold text-slate-900">Rezervasyon Kodu</span>
          <input
            type="text"
            inputMode="numeric"
            value={reservationCode}
            onChange={(event) => setReservationCode(event.target.value)}
            placeholder="6 haneli kodu giriniz"
            className="rounded-lg border border-slate-200 px-3 py-2"
            data-testid="reservation-lookup-input"
          />
        </label>
        <button
          type="submit"
          className="inline-flex items-center justify-center rounded-lg bg-indigo-600 px-4 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-indigo-500 disabled:cursor-not-allowed disabled:opacity-60"
          disabled={isSubmitting}
          data-testid="reservation-lookup-submit"
        >
          {isSubmitting ? "Sorgulanıyor..." : "Rezervasyonu Bul"}
        </button>
      </form>
    </div>
  );
}

export default ReservationLookupPage;
