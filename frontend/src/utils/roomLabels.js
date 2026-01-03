export const roomStatusLabel = (status) => {
  switch (status) {
    case "AVAILABLE":
      return "Müsait";
    case "OCCUPIED":
      return "Dolu";
    case "CLEANING":
      return "Temizlikte";
    case "MAINTENANCE":
      return "Bakımda";
    default:
      return status || "Bilinmiyor";
  }
};

export const roomTypeLabel = (typeName) => {
  if (!typeName) {
    return "Bilinmiyor";
  }

  const normalized = typeName.toLowerCase();
  if (normalized.includes("single")) {
    return "Tek Kişilik";
  }
  if (normalized.includes("double")) {
    return "Çift Kişilik";
  }
  if (normalized.includes("suite")) {
    return "Suit";
  }
  if (normalized.includes("standard")) {
    return "Standart";
  }

  return typeName;
};

export const roomImageByType = (typeName) => {
  if (!typeName) {
    return "/rooms/standard.svg";
  }

  const normalized = typeName.toLowerCase();
  if (normalized.includes("single")) {
    return "/rooms/single.svg";
  }
  if (normalized.includes("double")) {
    return "/rooms/double.svg";
  }
  if (normalized.includes("suite")) {
    return "/rooms/suite.svg";
  }

  return "/rooms/standard.svg";
};

export const reservationStatusLabel = (status) => {
  switch (status) {
    case "CREATED":
      return "Oluşturuldu";
    case "CONFIRMED":
      return "Onaylandı";
    case "CHECKED_IN":
      return "Giriş Yapıldı";
    case "CANCELLED":
      return "İptal Edildi";
    case "COMPLETED":
      return "Tamamlandı";
    default:
      return status || "Bilinmiyor";
  }
};
