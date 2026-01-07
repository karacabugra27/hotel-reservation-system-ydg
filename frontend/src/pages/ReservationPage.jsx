import React, {
  useCallback,
  useEffect,
  useMemo,
  useState,
  forwardRef,
} from "react";
import { useLocation, useNavigate, useSearchParams } from "react-router-dom";
import { DayPicker } from "react-day-picker";
import "react-day-picker/style.css";
import {
  createReservation,
  getReservationsByRoomId,
} from "../services/reservationService";
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
  const [reservedRanges, setReservedRanges] = useState([]);
  const [reservedRangesError, setReservedRangesError] = useState("");
  const navigate = useNavigate();
  const location = useLocation();
  const [searchParams] = useSearchParams();

  const initialRoomId = useMemo(() => {
    const stateRoomId = location.state?.roomId;
    const queryRoomId = searchParams.get("roomId");
    return stateRoomId || (queryRoomId ? Number(queryRoomId) : null);
  }, [location.state, searchParams]);

  const [selectedRoomId, setSelectedRoomId] = useState(initialRoomId);

  useEffect(() => {
    setSelectedRoomId(initialRoomId);
  }, [initialRoomId]);

  const parseIsoDate = (value) => {
    if (!value) {
      return undefined;
    }
    const [year, month, day] = value.split("-").map(Number);
    if (!year || !month || !day) {
      return undefined;
    }
    return new Date(year, month - 1, day);
  };

  const today = useMemo(() => {
    const now = new Date();
    now.setHours(0, 0, 0, 0);
    return now;
  }, []);

  const formatIsoDate = (date) => {
    if (!date) {
      return "";
    }
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  const ReservationDayButton = useMemo(
    () =>
      forwardRef((props, ref) => {
        const { day, date, ...buttonProps } = props;
        const resolvedDate =
          (day && day.date) || day || date || buttonProps?.value;
        const testId =
          resolvedDate instanceof Date
            ? `day-${formatIsoDate(resolvedDate)}`
            : undefined;
        return (
          <button
            ref={ref}
            {...buttonProps}
            data-testid={testId}
          />
        );
      }),
    []
  );

  const isBeforeToday = useCallback(
    (date) => {
      if (!date) {
        return false;
      }
      const normalized = new Date(date);
      normalized.setHours(0, 0, 0, 0);
      return normalized < today;
    },
    [today]
  );

  const selectedRange = useMemo(
    () => ({
      from: parseIsoDate(formState.checkInDate),
      to: parseIsoDate(formState.checkOutDate),
    }),
    [formState.checkInDate, formState.checkOutDate]
  );

  const disabledDays = useMemo(() => {
    const blockedRanges = reservedRanges
      .map((range) => {
        const from = parseIsoDate(range.checkInDate);
        const checkOut = parseIsoDate(range.checkOutDate);
        if (!from || !checkOut) {
          return null;
        }
        const to = new Date(
          checkOut.getFullYear(),
          checkOut.getMonth(),
          checkOut.getDate() - 1
        );
        if (to < from) {
          return null;
        }
        return { from, to };
      })
      .filter(Boolean);

    return [{ before: today }, ...blockedRanges];
  }, [reservedRanges, today]);

  const isCheckInBlocked = useCallback(
    (dateValue) =>
      reservedRanges.some(
        (range) =>
          dateValue >= range.checkInDate && dateValue < range.checkOutDate
      ),
    [reservedRanges]
  );

  const isRangeOverlapping = useCallback(
    (checkIn, checkOut) =>
      reservedRanges.some(
        (range) => range.checkOutDate > checkIn && range.checkInDate < checkOut
      ),
    [reservedRanges]
  );

  useEffect(() => {
    if (!selectedRoomId) {
      setReservedRanges([]);
      return;
    }

    let active = true;
    setReservedRangesError("");
    setReservedRanges([]);

    getReservationsByRoomId(selectedRoomId)
      .then((response) => {
        if (!active) {
          return;
        }
        setReservedRanges(response.data || []);
      })
      .catch(() => {
        if (active) {
          setReservedRangesError("Dolu tarihler yüklenemedi.");
        }
      });

    return () => {
      active = false;
    };
  }, [selectedRoomId]);

  useEffect(() => {
    if (reservedRanges.length === 0) {
      return;
    }

    if (formState.checkInDate && isCheckInBlocked(formState.checkInDate)) {
      setFormState((prev) => ({
        ...prev,
        checkInDate: "",
        checkOutDate: "",
      }));
      setErrorMessage(
        "Seçili oda için giriş tarihi dolu. Lütfen başka tarih seçin."
      );
      return;
    }

    if (
      formState.checkInDate &&
      formState.checkOutDate &&
      isRangeOverlapping(formState.checkInDate, formState.checkOutDate)
    ) {
      setFormState((prev) => ({ ...prev, checkOutDate: "" }));
      setErrorMessage(
        "Seçili oda için tarihler dolu aralıkla çakışıyor. Çıkış tarihini tekrar seçin."
      );
    }
  }, [
    formState.checkInDate,
    formState.checkOutDate,
    isCheckInBlocked,
    isRangeOverlapping,
    reservedRanges.length,
  ]);

  const handleChange = (event) => {
    const { name, value } = event.target;
    if (name === "checkInDate" || name === "checkOutDate") {
      setAvailabilityMessage("");
      setAvailabilityStatus("info");
      setErrorMessage("");
    }

    if (name === "checkInDate" && value) {
      if (isCheckInBlocked(value)) {
        setErrorMessage("Seçilen giriş tarihi dolu. Lütfen başka tarih seçin.");
        return;
      }

      if (
        formState.checkOutDate &&
        isRangeOverlapping(value, formState.checkOutDate)
      ) {
        setFormState((prev) => ({
          ...prev,
          checkInDate: value,
          checkOutDate: "",
        }));
        setErrorMessage(
          "Seçilen giriş tarihi dolu aralıkla çakışıyor. Çıkış tarihini tekrar seçin."
        );
        return;
      }
    }

    if (name === "checkOutDate" && value && formState.checkInDate) {
      if (isRangeOverlapping(formState.checkInDate, value)) {
        setErrorMessage("Seçilen çıkış tarihi dolu aralıkla çakışıyor.");
        return;
      }
    }

    setFormState((prev) => ({ ...prev, [name]: value }));
  };

  const handleRangeSelect = useCallback(
    (range) => {
      if (!range) {
        setFormState((prev) => ({
          ...prev,
          checkInDate: "",
          checkOutDate: "",
        }));
        return;
      }

      const checkInValue = range.from ? formatIsoDate(range.from) : "";
      const checkOutValue = range.to ? formatIsoDate(range.to) : "";

      if (range.from && isBeforeToday(range.from)) {
        setErrorMessage("Geçmiş tarih seçilemez.");
        setFormState((prev) => ({
          ...prev,
          checkInDate: "",
          checkOutDate: "",
        }));
        return;
      }

      if (range.to && isBeforeToday(range.to)) {
        setErrorMessage("Geçmiş tarih seçilemez.");
        setFormState((prev) => ({
          ...prev,
          checkInDate: "",
          checkOutDate: "",
        }));
        return;
      }

      if (checkInValue && isCheckInBlocked(checkInValue)) {
        setErrorMessage("Seçilen giriş tarihi dolu. Lütfen başka tarih seçin.");
        setFormState((prev) => ({
          ...prev,
          checkInDate: "",
          checkOutDate: "",
        }));
        return;
      }

      if (checkInValue && checkOutValue) {
        if (isRangeOverlapping(checkInValue, checkOutValue)) {
          setErrorMessage("Seçilen tarihler dolu aralıkla çakışıyor.");
          setFormState((prev) => ({
            ...prev,
            checkInDate: checkInValue,
            checkOutDate: "",
          }));
          return;
        }
      }

      setErrorMessage("");
      setAvailabilityMessage("");
      setAvailabilityStatus("info");
      setFormState((prev) => ({
        ...prev,
        checkInDate: checkInValue,
        checkOutDate: checkOutValue,
      }));
    },
    [isCheckInBlocked, isRangeOverlapping, isBeforeToday]
  );

  const handleSubmit = async (event) => {
    event.preventDefault();
    setErrorMessage("");
    setAvailabilityMessage("");
    setAvailabilityStatus("info");

    if (!formState.checkInDate || !formState.checkOutDate) {
      setErrorMessage("Lütfen giriş ve çıkış tarihlerini seçin.");
      return;
    }

    if (!selectedRoomId) {
      setErrorMessage(
        "Lütfen tarih seçip uygun odalar arasından oda belirleyin."
      );
      return;
    }

    const checkInDateValue = parseIsoDate(formState.checkInDate);
    const checkOutDateValue = parseIsoDate(formState.checkOutDate);
    if (isBeforeToday(checkInDateValue) || isBeforeToday(checkOutDateValue)) {
      setErrorMessage("Bugünden önceki tarihler seçilemez.");
      return;
    }

    if (formState.checkOutDate <= formState.checkInDate) {
      setErrorMessage("Çıkış tarihi giriş tarihinden sonra olmalı.");
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

      const payload = {
        firstName: formState.firstName,
        lastName: formState.lastName,
        phone: formState.phone,
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
      setAvailabilityMessage("Tarih seçimi yapınız.");
      setAvailabilityStatus("info");
      return;
    }

    if (checkOutDate <= checkInDate) {
      setAvailableRooms([]);
      setAvailabilityMessage("Çıkış tarihi giriş tarihinden sonra olmalı.");
      setAvailabilityStatus("warning");
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
          setAvailabilityMessage("Müsait odalar yüklenemedi.");
          setAvailabilityStatus("warning");
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

      {reservedRangesError && (
        <div className="rounded-lg border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-700">
          {reservedRangesError}
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
              type="text"
              name="checkInDate"
              value={formState.checkInDate}
              readOnly
              placeholder="Tarih seçin"
              className="rounded-lg border border-slate-200 px-3 py-2"
              data-testid="reservation-checkin"
            />
          </label>
          <label className="grid gap-2 text-sm text-slate-700">
            <span className="font-semibold text-slate-900">Çıkış Tarihi</span>
            <input
              type="text"
              name="checkOutDate"
              value={formState.checkOutDate}
              readOnly
              placeholder="Tarih seçin"
              className="rounded-lg border border-slate-200 px-3 py-2"
              data-testid="reservation-checkout"
            />
          </label>
        </div>

        <div className="rounded-xl border border-slate-200 bg-slate-50 p-4">
          <DayPicker
            mode="range"
            selected={selectedRange}
            onSelect={handleRangeSelect}
            disabled={disabledDays}
            numberOfMonths={1}
            components={{ DayButton: ReservationDayButton }}
          />
          <p className="mt-2 text-xs text-slate-500">
            Dolu günler seçilemez. Çıkış günü dolu olsa bile seçilebilir.
          </p>
        </div>

        <div className="grid gap-3" data-testid="reservation-room-options">
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
