import React from "react";
import { Link, Route, Routes } from "react-router-dom";
import HomePage from "./pages/HomePage";
import RoomDetailPage from "./pages/RoomDetailPage";
import RoomsPage from "./pages/RoomsPage";
import ReservationListPage from "./pages/ReservationListPage";
import ReservationPage from "./pages/ReservationPage";
import ReservationResultPage from "./pages/ReservationResultPage";

function App() {
  return (
    <div
      className="min-h-screen bg-slate-50 text-slate-900"
      data-testid="app-shell"
    >
      <header
        className="border-b border-slate-200 bg-white"
        data-testid="app-header"
      >
        <div className="mx-auto flex max-w-6xl items-center justify-between px-6 py-4">
          <Link
            to="/"
            className="text-lg font-semibold tracking-wide text-indigo-700"
            data-testid="app-brand"
          >
            Otel Rezervasyon Sistemi
          </Link>
          <nav className="flex items-center gap-4" data-testid="app-nav">
            <Link
              to="/rooms"
              className="text-sm font-medium text-slate-700 hover:text-indigo-700"
              data-testid="nav-rooms"
            >
              Odalar
            </Link>
            <Link
              to="/reservations"
              className="text-sm font-medium text-slate-700 hover:text-indigo-700"
              data-testid="nav-reservations"
            >
              Rezervasyon Kayıtları
            </Link>
            <Link
              to="/reserve"
              className="text-sm font-medium text-slate-700 hover:text-indigo-700"
              data-testid="nav-reserve"
            >
              Rezervasyon
            </Link>
          </nav>
        </div>
      </header>

      <main className="mx-auto w-full max-w-6xl px-6 py-8">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/rooms" element={<RoomsPage />} />
          <Route path="/rooms/:id" element={<RoomDetailPage />} />
          <Route path="/reserve" element={<ReservationPage />} />
          <Route path="/reserve/result" element={<ReservationResultPage />} />
          <Route path="/reservations" element={<ReservationListPage />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
