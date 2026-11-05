// Client ids are always UUIDv4 strings from UUID.randomUUID().toString()
// (see TripRepository.kt and ItineraryRepository.kt in the Android app).
// Rejecting anything else blocks an id containing '/' or '..' from
// escaping the intended Firestore document path.
const UUID_PATTERN = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;

function isValidId(id) {
  return typeof id === 'string' && UUID_PATTERN.test(id);
}

const DATE_PATTERN = /^\d{4}-\d{2}-\d{2}$/;

// The app sends java.time.LocalDate.toString(), always ISO-8601
// (YYYY-MM-DD). Matching the pattern alone would accept a calendar date
// that doesn't exist (e.g. 2026-02-30), so round-trip it through
// Date.UTC and compare the parts that come back out.
function isValidDateString(value) {
  if (typeof value !== 'string' || !DATE_PATTERN.test(value)) return false;
  const [y, m, d] = value.split('-').map(Number);
  const date = new Date(Date.UTC(y, m - 1, d));
  return date.getUTCFullYear() === y && date.getUTCMonth() === m - 1 && date.getUTCDate() === d;
}

function isValidName(name, maxLength = 200) {
  return typeof name === 'string' && name.trim().length > 0 && name.length <= maxLength;
}

// Pass when either side is absent - not every date pair is required, only
// their order when both are present.
function isValidDateOrder(start, end) {
  if (start == null || end == null) return true;
  return start <= end;
}

function isValidLat(lat) {
  return typeof lat === 'number' && lat >= -90 && lat <= 90;
}

function isValidLng(lng) {
  return typeof lng === 'number' && lng >= -180 && lng <= 180;
}

// Matches AppLanguage in the Android app.
const SUPPORTED_LANGUAGES = ['en', 'zu', 'xh'];
const CURRENCY_PATTERN = /^[A-Z]{3}$/;

const SETTINGS_DEFAULTS = Object.freeze({
  darkMode: null,
  notifyTripUpdates: true,
  notifyReminders: true,
  defaultCurrency: 'ZAR',
  language: 'en',
  updatedAtMs: 0,
});

// Checks a PUT /account/settings body field by field and returns either
// { settings } containing only the known fields, or { error } with a
// message for a 400 response.
function validateSettings(body) {
  if (body == null || typeof body !== 'object' || Array.isArray(body)) {
    return { error: 'Request body must be a JSON object' };
  }
  const { darkMode, notifyTripUpdates, notifyReminders, defaultCurrency, language, updatedAtMs } = body;

  if (darkMode !== null && typeof darkMode !== 'boolean') {
    return { error: 'darkMode must be true, false or null' };
  }
  if (typeof notifyTripUpdates !== 'boolean') return { error: 'notifyTripUpdates must be a boolean' };
  if (typeof notifyReminders !== 'boolean') return { error: 'notifyReminders must be a boolean' };
  if (typeof defaultCurrency !== 'string' || !CURRENCY_PATTERN.test(defaultCurrency)) {
    return { error: 'defaultCurrency must be a 3-letter ISO 4217 code, e.g. ZAR' };
  }
  if (!SUPPORTED_LANGUAGES.includes(language)) {
    return { error: `language must be one of: ${SUPPORTED_LANGUAGES.join(', ')}` };
  }
  if (!Number.isInteger(updatedAtMs) || updatedAtMs < 0) {
    return { error: 'updatedAtMs must be a non-negative integer (milliseconds since epoch)' };
  }

  return {
    settings: { darkMode, notifyTripUpdates, notifyReminders, defaultCurrency, language, updatedAtMs },
  };
}

module.exports = {
  UUID_PATTERN,
  isValidId,
  isValidDateString,
  isValidName,
  isValidDateOrder,
  isValidLat,
  isValidLng,
  SETTINGS_DEFAULTS,
  validateSettings,
};
