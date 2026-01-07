import React, { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { getRoomById } from "../services/roomService";
import {
  roomImageByType,
  roomStatusLabel,
  roomTypeLabel,
} from "../utils/roomLabels";

function RoomDetailPage() {
  const { id } = useParams();
  const [room, setRoom] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  const galleryImages = [
    roomImageByType(room?.roomTypeName),
    "/rooms/detail-1.svg",
    "/rooms/detail-2.svg",
  ];

  useEffect(() => {
    if (!id) {
      setErrorMessage("Oda bulunamadı.");
      setIsLoading(false);
      return;
    }

    getRoomById(id)
      .then((response) => setRoom(response.data))
      .catch(() => setErrorMessage("Oda detayı yüklenemedi."))
      .finally(() => setIsLoading(false));
  }, [id]);

  const handleReserve = () => {
    if (!room?.id) {
      setErrorMessage("Oda seçilemedi.");
      return;
    }
    navigate(`/reserve?roomId=${room.id}`, { state: { roomId: room.id } });
  };

  return (
    <div className="space-y-6" data-testid="room-detail-page">
      <div className="rounded-2xl border border-slate-200 bg-white p-8 shadow-sm">
        <h1
          className="text-3xl font-semibold text-slate-900"
          data-testid="room-detail-title"
        >
          Oda Detayı
        </h1>
        <p className="mt-2 text-base text-slate-600">
          Odanın detaylarını inceleyip rezervasyon yapabilirsiniz.
        </p>
      </div>

      {isLoading && (
        <div
          className="rounded-lg border border-slate-200 bg-white px-4 py-3 text-sm text-slate-700 shadow-sm"
          data-testid="room-detail-loading"
        >
          Yükleniyor...
        </div>
      )}

      {!isLoading && errorMessage && (
        <div
          className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700"
          data-testid="room-detail-error"
        >
          {errorMessage}
        </div>
      )}

      {!isLoading && room && (
        <div
          className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm"
          data-testid="room-detail-card"
        >
          <div className="grid gap-4 md:grid-cols-3">
            {galleryImages.map((image, index) => (
              <img
                key={`${room.id}-${index}`}
                src={image}
                alt={`Oda ${room.roomNumber} gorsel ${index + 1}`}
                className="h-48 w-full rounded-xl object-cover"
                loading="lazy"
                data-testid={`room-detail-image-${index + 1}`}
              />
            ))}
          </div>
          <div className="mt-6 flex flex-col gap-6 lg:flex-row">
            <div className="flex-1 space-y-4">
              <div className="rounded-2xl border border-slate-200 bg-slate-50 p-5">
                <div className="flex items-start justify-between">
                  <div>
                    <p className="text-xs uppercase tracking-[0.3em] text-slate-400">
                      Oda Bilgisi
                    </p>
                    <h2 className="mt-2 text-2xl font-semibold text-slate-900">
                      Oda {room.roomNumber} • {roomTypeLabel(room.roomTypeName)}
                    </h2>
                  </div>
                </div>
                <div className="mt-4 grid gap-4 text-sm text-slate-700 sm:grid-cols-2">
                  <div className="rounded-xl border border-slate-200 bg-white p-4">
                    <p className="text-xs uppercase tracking-wide text-slate-400">
                      Oda No
                    </p>
                    <p
                      className="mt-2 text-lg font-semibold text-slate-900"
                      data-testid="room-detail-number"
                    >
                      {room.roomNumber}
                    </p>
                  </div>
                  <div className="rounded-xl border border-slate-200 bg-white p-4">
                    <p className="text-xs uppercase tracking-wide text-slate-400">
                      Kapasite
                    </p>
                    <p
                      className="mt-2 text-lg font-semibold text-slate-900"
                      data-testid="room-detail-capacity"
                    >
                      {room.capacity ?? "-"} kişi
                    </p>
                  </div>
                  <div className="rounded-xl border border-slate-200 bg-white p-4">
                    <p className="text-xs uppercase tracking-wide text-slate-400">
                      Oda Tipi
                    </p>
                    <p
                      className="mt-2 text-lg font-semibold text-slate-900"
                      data-testid="room-detail-type"
                    >
                      {roomTypeLabel(room.roomTypeName)}
                    </p>
                  </div>
                  <div className="rounded-xl border border-slate-200 bg-white p-4">
                    <p className="text-xs uppercase tracking-wide text-slate-400">
                      Gecelik Ücret
                    </p>
                    <p
                      className="mt-2 text-lg font-semibold text-slate-900"
                      data-testid="room-detail-price"
                    >
                      {room.basePrice ?? "-"} TL
                    </p>
                  </div>
                </div>
              </div>

              <div className="rounded-2xl border border-slate-200 bg-white p-5">
                <h3 className="text-lg font-semibold text-slate-900">
                  Konaklama Avantajları
                </h3>
                <ul className="mt-3 grid gap-2 text-sm text-slate-600 sm:grid-cols-2">
                  {[
                    "Ücretsiz Wi-Fi",
                    "Kahvaltı Dahil",
                    "Giriş 14:00 - Çıkış 12:00",
                    "Günlük Oda Servisi",
                  ].map((item) => (
                    <li
                      key={item}
                      className="rounded-lg border border-slate-200 bg-slate-50 px-3 py-2"
                    >
                      {item}
                    </li>
                  ))}
                </ul>
              </div>
            </div>

            <div className="w-full max-w-sm space-y-4">
              <div className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
                <h3 className="text-lg font-semibold text-slate-900">
                  Rezervasyon Hazır
                </h3>
                <p className="mt-2 text-sm text-slate-600">
                  Tarihlerinizi seçerek rezervasyon talebinizi oluşturun.
                </p>
                <button
                  type="button"
                  className="mt-4 inline-flex w-full items-center justify-center rounded-lg bg-indigo-600 px-5 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-indigo-500"
                  data-testid="room-detail-reserve"
                  onClick={handleReserve}
                >
                  Rezervasyon Yap
                </button>
              </div>
              <Link
                to="/rooms"
                className="inline-flex w-full items-center justify-center rounded-lg border border-slate-300 px-5 py-2 text-sm font-semibold text-slate-700 transition hover:bg-slate-100"
                data-testid="room-detail-back"
              >
                Odalara Dön
              </Link>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default RoomDetailPage;
