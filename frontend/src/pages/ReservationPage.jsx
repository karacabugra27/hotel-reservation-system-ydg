import React, { useEffect, useMemo, useState } from "react";
import { useLocation, useNavigate, useSearchParams } from "react-router-dom";
import { createReservation } from "../services/reservationService";
import { getAvailableRoomsByDates } from "../services/roomService";
import { roomStatusLabel, roomTypeLabel } from "../utils/roomLabels";

function ReservationPage() {
  const [formState, setFormState] = useState({
    firstName: "",
    lastName: "",
    phone: "",
    checkInDate: "",
    checkOutDate: "",
  });
  const [errorMessage, setErrorMessage] = useState("");
  const [availabilityMessage, setAvailabilityMessage] = useState("");
  const [availabilityStatus, setAvailabilityStatus] = useState("info");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isChecking, setIsChecking] = useState(false);
  const [availableRooms, setAvailableRooms] = useState([]);
  const navigate = useNavigate();
  const location = useLocation();
  const [searchParams] = useSearchParams();

  const initialRoomId = useMemo(() => {
    const stateRoomId = location.state?.roomId;
    const queryRoomId = searchParams.get("roomId");
    return stateRoomId || (queryRoomId ? Number(queryRoomId) : null);
  }, [location.state, searchParams]);

  const [selectedRoomId, setSelectedRoomId] = useState(initialRoomId);
  const selectedRoom = availableRooms.find(
    (room) => room.id === selectedRoomId
  );

  useEffect(() => {
    setSelectedRoomId(initialRoomId);
  }, [initialRoomId]);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormState((prev) => ({ ...prev, [name]: value }));
    if (name === "checkInDate" || name === "checkOutDate") {
      setAvailabilityMessage("");
      setAvailabilityStatus("info");
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setErrorMessage("");
    setAvailabilityMessage("");
    setAvailabilityStatus("info");

    if (!formState.checkInDate || !formState.checkOutDate) {
      setErrorMessage("Lütgen giriş ve çıkış tarihlerini girin.");
      return;
    }

    if (!selectedRoomId) {
      setErrorMessage(
        "Lütfen tarih seçip uygun odalar arasından oda belirleyin."
      );
      return;
    }

    if (formState.checkOutDate <= formState.checkInDate) {
      setErrorMessage("Çıkış tarihi giris tarihinden sonra olmalı.");
      return;
    }

    try {
      setIsSubmitting(true);
      let roomsToCheck = availableRooms;

      if (roomsToCheck.length === 0) {
        const availabilityResponse = await getAvailableRoomsByDates(
          formState.checkInDate,
          formState.checkOutDate
        );
        roomsToCheck = availabilityResponse.data;
      }

      const isAvailable = roomsToCheck.some(
        (room) => room.id === selectedRoomId
      );
      if (!isAvailable) {
        setAvailabilityMessage(
          "Seçilen oda bu tarihlerde müsait değil. Lütfen başka oda seçin."
        );
        setAvailabilityStatus("warning");
        setIsSubmitting(false);
        return;
      }

      const defaultCustomerId = Number(
        import.meta.env.VITE_DEFAULT_CUSTOMER_ID || 1
      );

      const payload = {
        customerId: defaultCustomerId,
        roomId: selectedRoomId,
        checkInDate: formState.checkInDate,
        checkOutDate: formState.checkOutDate,
      };

      const response = await createReservation(payload);
      navigate("/reserve/result", {
        state: { success: true, data: response.data },
      });
    } catch (error) {
      navigate("/reserve/result", {
        state: {
          success: false,
          message: "Rezervasyon oluşturulamadı. Lütfen tekrar deneyin.",
        },
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  useEffect(() => {
    const { checkInDate, checkOutDate } = formState;

    if (!checkInDate || !checkOutDate) {
      setAvailableRooms([]);
      return;
    }

    if (checkOutDate <= checkInDate) {
      setAvailableRooms([]);
      setAvailabilityMessage("");
      setAvailabilityStatus("info");
      setErrorMessage("Çıkış tarihi giriş tarihinden sonra olmalı.");
      return;
    }

    let active = true;
    setIsChecking(true);
    setErrorMessage("");

    getAvailableRoomsByDates(checkInDate, checkOutDate)
      .then((response) => {
        if (!active) {
          return;
        }
        setAvailableRooms(response.data);

        if (response.data.length === 0) {
          setAvailabilityMessage("Bu tarihlerde müsait oda bulunamadı.");
          setAvailabilityStatus("warning");
          setSelectedRoomId(null);
          return;
        }

        if (selectedRoomId) {
          const match = response.data.some(
            (room) => room.id === selectedRoomId
          );
          if (!match) {
            setAvailabilityMessage(
              "Seçili oda bu tarihlerde müsait değil. Lütfen başka oda seçin."
            );
            setAvailabilityStatus("warning");
            setSelectedRoomId(null);
          } else {
            setAvailabilityMessage("Seçili oda bu tarihlerde müsait.");
            setAvailabilityStatus("success");
          }
        }
      })
      .catch(() => {
        if (active) {
          setErrorMessage("Müsait odalar yüklenemedi.");
        }
      })
      .finally(() => {
        if (active) {
          setIsChecking(false);
        }
      });

    return () => {
      active = false;
    };
  }, [formState.checkInDate, formState.checkOutDate, selectedRoomId]);

  return (
    <div className="space-y-6" data-testid="reservation-page">
      <div className="rounded-2xl border border-slate-200 bg-white p-8 shadow-sm">
        <h1
          className="text-3xl font-semibold text-slate-900"
          data-testid="reservation-title"
        >
          Rezervasyon Talebi
        </h1>
        <p
          className="mt-2 text-base text-slate-600"
          data-testid="reservation-subtitle"
        >
          Tarihlerinizi seçin, uygun odayı belirleyin ve rezervasyonunuzu
          tamamlayın.
        </p>
      </div>

      {errorMessage && (
        <div
          className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700"
          data-testid="reservation-error"
        >
          {errorMessage}
        </div>
      )}

      {availabilityMessage && (
        <div
          className={`rounded-lg px-4 py-3 text-sm ${
            availabilityStatus === "success"
              ? "border border-emerald-200 bg-emerald-50 text-emerald-700"
              : "border border-amber-200 bg-amber-50 text-amber-700"
          }`}
          data-testid="reservation-availability-message"
        >
          {availabilityMessage}
        </div>
      )}

      <form
        className="grid gap-4 rounded-2xl border border-slate-200 bg-white p-6 shadow-sm"
        onSubmit={handleSubmit}
        data-testid="reservation-form"
      >
        <div className="grid gap-4 md:grid-cols-2">
          <label className="grid gap-2 text-sm text-slate-700">
            <span className="font-semibold text-slate-900">Giriş Tarihi</span>
            <input
              type="date"
              name="checkInDate"
              value={formState.checkInDate}
              onChange={handleChange}
              required
              className="rounded-lg border border-slate-200 px-3 py-2"
              data-testid="reservation-checkin"
            />
          </label>
          <label className="grid gap-2 text-sm text-slate-700">
            <span className="font-semibold text-slate-900">Çıkış Tarihi</span>
            <input
              type="date"
              name="checkOutDate"
              value={formState.checkOutDate}
              onChange={handleChange}
              required
              className="rounded-lg border border-slate-200 px-3 py-2"
              data-testid="reservation-checkout"
            />
          </label>
        </div>

        <div className="grid gap-3" data-testid="reservation-room-options">
          {availableRooms.length === 0 && (
            <div className="rounded-lg border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-600">
              {isChecking
                ? "Müsait odalar kontrol ediliyor..."
                : "Tarih seçimi yapınız."}
            </div>
          )}
          {availableRooms.map((room) => (
            <label
              key={room.id}
              className={`flex items-center justify-between rounded-lg border px-4 py-3 text-sm transition ${
                selectedRoomId === room.id
                  ? "border-indigo-500 bg-indigo-50 text-indigo-700"
                  : "border-slate-200 bg-white text-slate-700"
              }`}
              data-testid={`reservation-room-option-${room.id}`}
            >
              <div className="grid gap-1">
                <span className="font-semibold text-slate-900">
                  Oda {room.roomNumber} - {roomTypeLabel(room.roomTypeName)}
                </span>
                <span className="text-xs text-slate-500">
                  {room.capacity ?? "-"} kisi • {room.basePrice ?? "-"} TL •{" "}
                  {roomStatusLabel(room.status)}
                </span>
              </div>
              <input
                type="radio"
                name="roomSelection"
                value={room.id}
                checked={selectedRoomId === room.id}
                onChange={() => setSelectedRoomId(room.id)}
                data-testid={`reservation-room-radio-${room.id}`}
              />
            </label>
          ))}
        </div>

        <label className="grid gap-2 text-sm text-slate-700">
          <span className="font-semibold text-slate-900">Ad</span>
          <input
            type="text"
            name="firstName"
            value={formState.firstName}
            onChange={handleChange}
            required
            className="rounded-lg border border-slate-200 px-3 py-2"
            data-testid="reservation-first-name"
          />
        </label>
        <label className="grid gap-2 text-sm text-slate-700">
          <span className="font-semibold text-slate-900">Soyad</span>
          <input
            type="text"
            name="lastName"
            value={formState.lastName}
            onChange={handleChange}
            required
            className="rounded-lg border border-slate-200 px-3 py-2"
            data-testid="reservation-last-name"
          />
        </label>
        <label className="grid gap-2 text-sm text-slate-700">
          <span className="font-semibold text-slate-900">Telefon</span>
          <input
            type="tel"
            name="phone"
            value={formState.phone}
            onChange={handleChange}
            required
            inputMode="tel"
            placeholder="05XX XXX XX XX"
            className="rounded-lg border border-slate-200 px-3 py-2"
            data-testid="reservation-phone"
          />
        </label>

        <button
          type="submit"
          className="inline-flex items-center justify-center rounded-lg bg-indigo-600 px-4 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-indigo-500 disabled:cursor-not-allowed disabled:opacity-60"
          disabled={isSubmitting || !selectedRoomId}
          data-testid="reservation-submit"
        >
          {isSubmitting ? "Gönderiliyor..." : "Rezervasyonu Tamamla"}
        </button>
        {isSubmitting && (
          <div
            className="text-sm text-slate-500"
            data-testid="reservation-loading"
          >
            Rezervasyon talebiniz iletiliyor...
          </div>
        )}
      </form>
    </div>
  );
}

export default ReservationPage;
