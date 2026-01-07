import React from "react";
import { Link } from "react-router-dom";

function HomePage() {
  return (
    <div className="space-y-10" data-testid="home-page">
      <section className="overflow-hidden rounded-3xl border border-slate-200 bg-gradient-to-br from-indigo-600 via-indigo-500 to-slate-900 p-10 text-white shadow-lg">
        <div className="max-w-3xl space-y-6">
          <p
            className="text-sm uppercase tracking-[0.3em]"
            data-testid="home-kicker"
          >
            Profesyonel Otel Rezervasyonu Deneyimi
          </p>
          <h1
            className="text-4xl font-semibold leading-tight"
            data-testid="home-title"
          >
            Otel Rezervasyon Sistemi
          </h1>
          <p
            className="text-base text-indigo-100"
            data-testid="home-description"
          >
            Oda seçimi, tarih kontrolü ve rezervasyon akışı
          </p>
          <div className="flex flex-wrap gap-4">
            <Link
              className="inline-flex items-center justify-center rounded-lg bg-emerald-400 px-6 py-2 text-sm font-semibold text-slate-900 shadow-sm transition hover:bg-emerald-300"
              to="/rooms"
              data-testid="home-rooms-button"
            >
              Odalarımızı İncele
            </Link>
            <Link
              className="inline-flex items-center justify-center rounded-lg border border-white/40 px-6 py-2 text-sm font-semibold text-white transition hover:bg-white/10"
              to="/reservations"
              data-testid="home-reservations-button"
            >
              Rezervasyon Sorgula
            </Link>
          </div>
        </div>
      </section>

      <section
        className="grid gap-6 md:grid-cols-3"
        data-testid="home-features"
      >
        {[
          {
            title: "Oda Seçimi",
            text: "Müsaitlik, kapasite ve fiyat bilgileri tek listede.",
          },
          {
            title: "Tarih Seçimi",
            text: "Seçilen tarihlere uygun odalar otomatik getirilir.",
          },
          {
            title: "Rezervasyon",
            text: "Rezervasyonlarınızı kolayca sorgulayın.",
          },
        ].map((item) => (
          <div
            key={item.title}
            className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm transition hover:-translate-y-0.5 hover:shadow-md"
            data-testid="home-feature-card"
          >
            <h3 className="text-lg font-semibold text-slate-900">
              {item.title}
            </h3>
            <p className="mt-2 text-sm text-slate-600">{item.text}</p>
          </div>
        ))}
      </section>

      <section
        className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm"
        data-testid="home-highlight"
      >
        <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
          <div>
            <h2 className="text-2xl font-semibold text-slate-900">
              Hemen Rezervasyonunuzu Oluşturun
            </h2>
            <p className="mt-2 text-sm text-slate-600">
              Rezervasyon için tarihinizi ve odanızı seçin
            </p>
          </div>
          <Link
            className="inline-flex items-center justify-center rounded-lg bg-indigo-600 px-5 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-indigo-500"
            to="/reserve"
            data-testid="home-reserve-cta"
          >
            Rezervasyon Oluştur
          </Link>
        </div>
      </section>
    </div>
  );
}

export default HomePage;
