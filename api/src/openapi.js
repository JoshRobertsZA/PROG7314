/**
 * OpenAPI 3 description of every route, served as /openapi.json and rendered by Swagger UI at /docs.
 * Keep it in step with routes/ when an endpoint changes.
 */
const errorSchema = {
  type: 'object',
  properties: { error: { type: 'string', example: 'Trip not found' } },
};

const deletedSchema = {
  type: 'object',
  properties: { deleted: { type: 'string', example: 'trip_abc123' } },
};

const err = (description) => ({
  description,
  content: { 'application/json': { schema: { $ref: '#/components/schemas/Error' } } },
});

const authErrors = {
  401: err('Missing, invalid or expired Firebase ID token'),
  403: err('Authenticated user does not own this resource'),
  500: err('Unexpected server error'),
};

const jsonBody = (ref, required = true) => ({
  required,
  content: { 'application/json': { schema: { $ref: `#/components/schemas/${ref}` } } },
});

const jsonRes = (description, ref) => ({
  description,
  content: { 'application/json': { schema: { $ref: `#/components/schemas/${ref}` } } },
});

const jsonListRes = (description, ref) => ({
  description,
  content: {
    'application/json': {
      schema: { type: 'array', items: { $ref: `#/components/schemas/${ref}` } },
    },
  },
});

const pathParam = (name, description) => ({
  name,
  in: 'path',
  required: true,
  schema: { type: 'string' },
  description,
});

function listPath({ tag, summary, param, paramDesc, schema }) {
  return {
    get: {
      tags: [tag],
      summary,
      parameters: [pathParam(param, paramDesc)],
      responses: {
        200: jsonListRes('The matching records, oldest first', schema),
        404: err('Parent trip or day not found'),
        ...authErrors,
      },
    },
  };
}

function createPath({ tag, summary, bodySchema, schema }) {
  return {
    post: {
      tags: [tag],
      summary,
      requestBody: jsonBody(bodySchema),
      responses: {
        201: jsonRes('Created', schema),
        200: jsonRes(
          'The caller already owns a record at this id - treated as a successful retry of a create whose response was lost in transit',
          schema
        ),
        400: err('Required fields missing from the request body, or a field failed validation (id/parent id not a UUID, invalid date, name too long, dates out of order, lat/lng out of range)'),
        404: err('Parent trip or day not found'),
        409: err('This id is already in use by a record owned by a different trip'),
        ...authErrors,
      },
    },
  };
}

function deleteOp({ tag, summary, notFound }) {
  return {
    tags: [tag],
    summary,
    parameters: [pathParam('id', 'Record identifier')],
    responses: {
      200: jsonRes('Deleted', 'Deleted'),
      404: err(notFound),
      ...authErrors,
    },
  };
}

const openapiSpec = {
  openapi: '3.0.3',
  info: {
    title: 'Waypoint API',
    version: '1.0.0',
    description:
      'REST API middleware between the Waypoint Android app and Firestore.\n\n' +
      '**Authentication** — every endpoint except `GET /health` and `GET /config/web-client-id` ' +
      'requires a Firebase ID token sent as `Authorization: Bearer <token>`. ' +
      'Use the **Authorize** button above and paste the raw token (no `Bearer ` prefix).\n\n' +
      '**Ownership** — trips are scoped to the authenticated user via `accountId`. ' +
      'Itinerary records inherit that check through their parent trip, so requesting ' +
      "another user's data returns `403 Forbidden`.\n\n" +
      '**Identifiers** — the client generates document ids and sends them in the request ' +
      'body, which keeps offline-created records stable once they sync.',
  },
  servers: [
    { url: '/', description: 'This server' },
    { url: 'http://localhost:8080', description: 'Local development' },
  ],
  tags: [
    { name: 'Health', description: 'Service liveness probe' },
    { name: 'Trips', description: 'Trips owned by the authenticated user' },
    { name: 'Itinerary — Days', description: 'Calendar days belonging to a trip' },
    { name: 'Itinerary — Flights', description: 'Flights attached to a day' },
    { name: 'Itinerary — Lodging', description: 'Accommodation bookings for a trip' },
    { name: 'Itinerary — Cars', description: 'Car rentals for a trip' },
    { name: 'Itinerary — Places', description: 'Places of interest planned for a day' },
    { name: 'Config', description: 'Server-held API keys and the Firebase web client id' },
    { name: 'Cache', description: 'Shared weather/currency/places lookup cache' },
    { name: 'Devices', description: 'FCM push tokens registered per user' },
    { name: 'Notify', description: 'Push notification test endpoint' },
  ],
  components: {
    securitySchemes: {
      firebaseAuth: {
        type: 'http',
        scheme: 'bearer',
        bearerFormat: 'JWT',
        description: 'Firebase ID token obtained by the Android client after sign-in.',
      },
    },
    schemas: {
      Error: errorSchema,
      Deleted: deletedSchema,
      Settings: {
        type: 'object',
        required: ['darkMode', 'notifyTripUpdates', 'notifyReminders', 'defaultCurrency', 'language', 'updatedAtMs'],
        properties: {
          darkMode: { type: 'boolean', nullable: true, description: 'null = follow the phone setting', example: true },
          notifyTripUpdates: { type: 'boolean', example: true },
          notifyReminders: { type: 'boolean', example: true },
          defaultCurrency: { type: 'string', pattern: '^[A-Z]{3}$', example: 'ZAR' },
          language: { type: 'string', enum: ['en', 'zu', 'xh'], example: 'zu' },
          updatedAtMs: { type: 'integer', description: 'When the settings last changed on the phone; the newer copy wins', example: 1762300000000 },
        },
      },

      Health: {
        type: 'object',
        properties: {
          status: { type: 'string', example: 'ok' },
          service: { type: 'string', example: 'waypoint-api' },
        },
      },

      Trip: {
        type: 'object',
        properties: {
          id: { type: 'string', example: 'trip_abc123' },
          accountId: {
            type: 'string',
            description:
              'Firebase uid of the owner. Set by the server; never accepted from the client.',
            readOnly: true,
          },
          name: { type: 'string', example: 'Tokyo, Spring 2026' },
          startDate: { type: 'string', nullable: true, example: '2026-04-02' },
          endDate: { type: 'string', nullable: true, example: '2026-04-16' },
          destination: { type: 'string', nullable: true, example: 'Tokyo, Japan' },
          destLat: { type: 'number', format: 'double', nullable: true, example: 35.6762 },
          destLng: { type: 'number', format: 'double', nullable: true, example: 139.6503 },
          destPhotoUrl: { type: 'string', nullable: true },
          createdAtMs: { type: 'integer', format: 'int64', example: 1774000000000 },
          updatedAtMs: { type: 'integer', format: 'int64', example: 1774000000000 },
        },
      },
      TripCreate: {
        type: 'object',
        required: ['id', 'name'],
        properties: {
          id: {
            type: 'string',
            description: 'Client-generated document id',
            example: 'trip_abc123',
          },
          name: { type: 'string', example: 'Tokyo, Spring 2026' },
          startDate: { type: 'string', nullable: true, example: '2026-04-02' },
          endDate: { type: 'string', nullable: true, example: '2026-04-16' },
          destination: { type: 'string', nullable: true, example: 'Tokyo, Japan' },
          destLat: { type: 'number', format: 'double', nullable: true, example: 35.6762 },
          destLng: { type: 'number', format: 'double', nullable: true, example: 139.6503 },
          destPhotoUrl: { type: 'string', nullable: true },
          createdAtMs: { type: 'integer', format: 'int64', description: 'Defaults to now' },
          updatedAtMs: { type: 'integer', format: 'int64', description: 'Defaults to now' },
        },
      },
      TripUpdate: {
        type: 'object',
        description:
          'Any subset of the mutable trip fields. `updatedAtMs` is set by the server and ' +
          '`accountId` is stripped so ownership cannot be reassigned.',
        properties: {
          name: { type: 'string', example: 'Tokyo & Kyoto, Spring 2026' },
          startDate: { type: 'string', nullable: true },
          endDate: { type: 'string', nullable: true },
          destination: { type: 'string', nullable: true },
          destLat: { type: 'number', format: 'double', nullable: true },
          destLng: { type: 'number', format: 'double', nullable: true },
          destPhotoUrl: { type: 'string', nullable: true },
        },
      },

      Day: {
        type: 'object',
        properties: {
          id: { type: 'string', example: 'day_20260402' },
          tripId: { type: 'string', example: 'trip_abc123' },
          date: {
            type: 'string',
            description: 'ISO date, sorted ascending',
            example: '2026-04-02',
          },
        },
      },
      DayCreate: {
        type: 'object',
        required: ['id', 'tripId', 'date'],
        properties: {
          id: { type: 'string', example: 'day_20260402' },
          tripId: { type: 'string', example: 'trip_abc123' },
          date: { type: 'string', example: '2026-04-02' },
        },
      },
      DayUpdate: {
        type: 'object',
        description: 'Only `date` is mutable; `tripId` is the parent link.',
        properties: {
          date: { type: 'string', example: '2026-04-02' },
        },
      },

      Flight: {
        type: 'object',
        properties: {
          id: { type: 'string', example: 'flight_nh204' },
          dayId: { type: 'string', example: 'day_20260402' },
          flightNumber: { type: 'string', nullable: true, example: 'NH204' },
          departureTime: { type: 'string', nullable: true, example: '2026-04-02T09:45' },
          pdfUri: {
            type: 'string',
            nullable: true,
            description: 'Storage URI of the boarding pass',
          },
          docName: { type: 'string', nullable: true, example: 'boarding-pass.pdf' },
          createdAtMs: { type: 'integer', format: 'int64' },
        },
      },
      FlightCreate: {
        type: 'object',
        required: ['id', 'dayId'],
        properties: {
          id: { type: 'string', example: 'flight_nh204' },
          dayId: { type: 'string', example: 'day_20260402' },
          flightNumber: { type: 'string', nullable: true, example: 'NH204' },
          departureTime: { type: 'string', nullable: true, example: '2026-04-02T09:45' },
          pdfUri: { type: 'string', nullable: true },
          docName: { type: 'string', nullable: true },
        },
      },
      FlightUpdate: {
        type: 'object',
        description: 'Any subset of the mutable flight fields; `dayId` is the parent link and `createdAtMs` is immutable.',
        properties: {
          flightNumber: { type: 'string', nullable: true, example: 'NH204' },
          departureTime: { type: 'string', nullable: true, example: '2026-04-02T09:45' },
          pdfUri: { type: 'string', nullable: true },
          docName: { type: 'string', nullable: true },
        },
      },

      Lodging: {
        type: 'object',
        properties: {
          id: { type: 'string', example: 'lodging_shinjuku' },
          tripId: { type: 'string', example: 'trip_abc123' },
          fromDate: { type: 'string', nullable: true, example: '2026-04-02' },
          toDate: { type: 'string', nullable: true, example: '2026-04-09' },
          pdfUri: { type: 'string', nullable: true },
          docName: { type: 'string', nullable: true, example: 'hotel-confirmation.pdf' },
          createdAtMs: { type: 'integer', format: 'int64' },
        },
      },
      LodgingCreate: {
        type: 'object',
        required: ['id', 'tripId'],
        properties: {
          id: { type: 'string', example: 'lodging_shinjuku' },
          tripId: { type: 'string', example: 'trip_abc123' },
          fromDate: { type: 'string', nullable: true, example: '2026-04-02' },
          toDate: { type: 'string', nullable: true, example: '2026-04-09' },
          pdfUri: { type: 'string', nullable: true },
          docName: { type: 'string', nullable: true },
        },
      },
      LodgingUpdate: {
        type: 'object',
        description: 'Any subset of the mutable lodging fields; `tripId` is the parent link and `createdAtMs` is immutable.',
        properties: {
          fromDate: { type: 'string', nullable: true, example: '2026-04-02' },
          toDate: { type: 'string', nullable: true, example: '2026-04-09' },
          pdfUri: { type: 'string', nullable: true },
          docName: { type: 'string', nullable: true },
        },
      },

      Car: {
        type: 'object',
        properties: {
          id: { type: 'string', example: 'car_toyota' },
          tripId: { type: 'string', example: 'trip_abc123' },
          fromDate: { type: 'string', nullable: true, example: '2026-04-09' },
          toDate: { type: 'string', nullable: true, example: '2026-04-12' },
          pdfUri: { type: 'string', nullable: true },
          docName: { type: 'string', nullable: true, example: 'rental-agreement.pdf' },
          createdAtMs: { type: 'integer', format: 'int64' },
        },
      },
      CarCreate: {
        type: 'object',
        required: ['id', 'tripId'],
        properties: {
          id: { type: 'string', example: 'car_toyota' },
          tripId: { type: 'string', example: 'trip_abc123' },
          fromDate: { type: 'string', nullable: true, example: '2026-04-09' },
          toDate: { type: 'string', nullable: true, example: '2026-04-12' },
          pdfUri: { type: 'string', nullable: true },
          docName: { type: 'string', nullable: true },
        },
      },
      CarUpdate: {
        type: 'object',
        description: 'Any subset of the mutable car-rental fields; `tripId` is the parent link and `createdAtMs` is immutable.',
        properties: {
          fromDate: { type: 'string', nullable: true, example: '2026-04-09' },
          toDate: { type: 'string', nullable: true, example: '2026-04-12' },
          pdfUri: { type: 'string', nullable: true },
          docName: { type: 'string', nullable: true },
        },
      },

      Place: {
        type: 'object',
        properties: {
          id: { type: 'string', example: 'place_sensoji' },
          dayId: { type: 'string', example: 'day_20260402' },
          name: { type: 'string', example: 'Senso-ji Temple' },
          category: { type: 'string', nullable: true, example: 'Landmark' },
          lat: { type: 'number', format: 'double', nullable: true, example: 35.7148 },
          lng: { type: 'number', format: 'double', nullable: true, example: 139.7967 },
          note: { type: 'string', nullable: true, example: 'Go early to beat the crowds' },
          photoUrl: { type: 'string', nullable: true },
          createdAtMs: { type: 'integer', format: 'int64' },
        },
      },
      PlaceCreate: {
        type: 'object',
        required: ['id', 'dayId', 'name'],
        properties: {
          id: { type: 'string', example: 'place_sensoji' },
          dayId: { type: 'string', example: 'day_20260402' },
          name: { type: 'string', example: 'Senso-ji Temple' },
          category: { type: 'string', nullable: true, example: 'Landmark' },
          lat: { type: 'number', format: 'double', nullable: true, example: 35.7148 },
          lng: { type: 'number', format: 'double', nullable: true, example: 139.7967 },
          note: { type: 'string', nullable: true },
          photoUrl: { type: 'string', nullable: true },
        },
      },
      PlaceUpdate: {
        type: 'object',
        description: 'Any subset of the place fields; merged into the existing record.',
        properties: {
          name: { type: 'string' },
          category: { type: 'string', nullable: true },
          lat: { type: 'number', format: 'double', nullable: true },
          lng: { type: 'number', format: 'double', nullable: true },
          note: { type: 'string', nullable: true },
          photoUrl: { type: 'string', nullable: true },
        },
      },

      Keys: {
        type: 'object',
        description:
          'Always includes every key, falling back to an empty string if that env var is unset server-side.',
        properties: {
          LOCATIONIQ_API_KEY: { type: 'string' },
          EXCHANGERATE_API_KEY: { type: 'string' },
          COUNTERAPI_API_KEY: { type: 'string' },
          COUNTERAPI_WORKSPACE: { type: 'string' },
          COUNTERAPI_PLACES_SLUG: { type: 'string' },
          OPENWEATHER_API_KEY: { type: 'string' },
          AIRLABS_API_KEY: { type: 'string' },
        },
      },

      WebClientId: {
        type: 'object',
        properties: {
          FIREBASE_WEB_CLIENT_ID: { type: 'string' },
        },
      },

      CacheEntry: {
        type: 'object',
        description: 'Caller-defined shape - whatever was last PUT to this path, stored and returned verbatim.',
        additionalProperties: true,
      },

      Device: {
        type: 'object',
        required: ['token'],
        properties: {
          token: { type: 'string', description: 'FCM registration token; also the document id' },
          locale: { type: 'string', example: 'en', description: 'App language tag (en/zu/xh), sent so a future server-built message can be localised' },
          accountId: { type: 'string', readOnly: true },
          updatedAtMs: { type: 'integer', readOnly: true },
        },
      },

      NotifyResult: {
        type: 'object',
        description: 'Outcome of a send to every device registered under a user.',
        properties: {
          successCount: { type: 'integer' },
          failureCount: { type: 'integer' },
          pruned: { type: 'integer', description: 'Devices removed because FCM reported their token unregistered' },
        },
      },
    },
  },

  security: [{ firebaseAuth: [] }],

  paths: {
    '/health': {
      get: {
        tags: ['Health'],
        summary: 'Liveness probe',
        security: [],
        responses: { 200: jsonRes('Service is up', 'Health') },
      },
    },

    '/trips': {
      get: {
        tags: ['Trips'],
        summary: 'List the trips of the authenticated user',
        description: 'Returns trips owned by the caller, newest first.',
        responses: { 200: jsonListRes('Trips, newest first', 'Trip'), ...authErrors },
      },
      post: {
        tags: ['Trips'],
        summary: 'Create a trip',
        description:
          'The owner is taken from the verified token, so `accountId` in the body is ignored. ' +
          'On success, sends a push (see Notify) to the user\'s other registered devices - ' +
          'pass the optional `X-Device-Token` header with this device\'s own FCM token to ' +
          "exclude it from that push.",
        parameters: [
          {
            name: 'X-Device-Token',
            in: 'header',
            required: false,
            schema: { type: 'string' },
            description: "This device's own FCM token, if registered, so the trip_created push skips it.",
          },
        ],
        requestBody: jsonBody('TripCreate'),
        responses: {
          201: jsonRes('Created', 'Trip'),
          200: jsonRes(
            'The caller already owns a trip at this id - treated as a successful retry of a create whose response was lost in transit',
            'Trip'
          ),
          400: err('`id` and `name` are required, or a field failed validation (id not a UUID, name too long, invalid date, or startDate after endDate)'),
          409: err('This id is already in use by a trip owned by a different account'),
          ...authErrors,
        },
      },
    },
    '/trips/{id}': {
      parameters: [pathParam('id', 'Trip identifier')],
      get: {
        tags: ['Trips'],
        summary: 'Fetch a single trip',
        responses: {
          200: jsonRes('The trip', 'Trip'),
          404: err('Trip not found'),
          ...authErrors,
        },
      },
      put: {
        tags: ['Trips'],
        summary: 'Update a trip',
        requestBody: jsonBody('TripUpdate'),
        responses: {
          200: jsonRes('The applied updates', 'Trip'),
          400: err('A field failed validation (name too long, invalid date, or the resulting startDate/endDate out of order)'),
          404: err('Trip not found'),
          ...authErrors,
        },
      },
      delete: {
        tags: ['Trips'],
        summary: 'Delete a trip',
        description: 'Cascades: every itinerary day, flight, lodging booking, car rental and place that references this trip is deleted first, then the trip document itself.',
        responses: {
          200: jsonRes('Deleted', 'Deleted'),
          404: err('Trip not found'),
          ...authErrors,
        },
      },
    },

    '/itinerary/days/{tripId}': listPath({
      tag: 'Itinerary — Days',
      summary: 'List the days of a trip',
      param: 'tripId',
      paramDesc: 'Trip identifier',
      schema: 'Day',
    }),
    '/itinerary/days': createPath({
      tag: 'Itinerary — Days',
      summary: 'Add a day to a trip',
      bodySchema: 'DayCreate',
      schema: 'Day',
    }),
    '/itinerary/days/{id}': {
      put: {
        tags: ['Itinerary — Days'],
        summary: 'Update a day',
        parameters: [pathParam('id', 'Day identifier')],
        requestBody: jsonBody('DayUpdate'),
        responses: {
          200: jsonRes('The applied updates', 'Day'),
          400: err('date failed validation'),
          404: err('Day not found'),
          ...authErrors,
        },
      },
      delete: deleteOp({
        tag: 'Itinerary — Days',
        summary: 'Delete a day',
        notFound: 'Day not found',
      }),
    },

    '/itinerary/flights/{dayId}': listPath({
      tag: 'Itinerary — Flights',
      summary: 'List the flights on a day',
      param: 'dayId',
      paramDesc: 'Day identifier',
      schema: 'Flight',
    }),
    '/itinerary/flights': createPath({
      tag: 'Itinerary — Flights',
      summary: 'Add a flight to a day',
      bodySchema: 'FlightCreate',
      schema: 'Flight',
    }),
    '/itinerary/flights/{id}': {
      put: {
        tags: ['Itinerary — Flights'],
        summary: 'Update a flight',
        parameters: [pathParam('id', 'Flight identifier')],
        requestBody: jsonBody('FlightUpdate'),
        responses: {
          200: jsonRes('The applied updates', 'Flight'),
          404: err('Flight not found'),
          ...authErrors,
        },
      },
      delete: deleteOp({
        tag: 'Itinerary — Flights',
        summary: 'Delete a flight',
        notFound: 'Flight not found',
      }),
    },

    '/itinerary/lodging/{tripId}': listPath({
      tag: 'Itinerary — Lodging',
      summary: 'List the lodging bookings for a trip',
      param: 'tripId',
      paramDesc: 'Trip identifier',
      schema: 'Lodging',
    }),
    '/itinerary/lodging': createPath({
      tag: 'Itinerary — Lodging',
      summary: 'Add a lodging booking to a trip',
      bodySchema: 'LodgingCreate',
      schema: 'Lodging',
    }),
    '/itinerary/lodging/{id}': {
      put: {
        tags: ['Itinerary — Lodging'],
        summary: 'Update a lodging booking',
        parameters: [pathParam('id', 'Lodging identifier')],
        requestBody: jsonBody('LodgingUpdate'),
        responses: {
          200: jsonRes('The applied updates', 'Lodging'),
          400: err('fromDate/toDate failed validation or are out of order'),
          404: err('Lodging not found'),
          ...authErrors,
        },
      },
      delete: deleteOp({
        tag: 'Itinerary — Lodging',
        summary: 'Delete a lodging booking',
        notFound: 'Lodging not found',
      }),
    },

    '/itinerary/cars/{tripId}': listPath({
      tag: 'Itinerary — Cars',
      summary: 'List the car rentals for a trip',
      param: 'tripId',
      paramDesc: 'Trip identifier',
      schema: 'Car',
    }),
    '/itinerary/cars': createPath({
      tag: 'Itinerary — Cars',
      summary: 'Add a car rental to a trip',
      bodySchema: 'CarCreate',
      schema: 'Car',
    }),
    '/itinerary/cars/{id}': {
      put: {
        tags: ['Itinerary — Cars'],
        summary: 'Update a car rental',
        parameters: [pathParam('id', 'Car rental identifier')],
        requestBody: jsonBody('CarUpdate'),
        responses: {
          200: jsonRes('The applied updates', 'Car'),
          400: err('fromDate/toDate failed validation or are out of order'),
          404: err('Car rental not found'),
          ...authErrors,
        },
      },
      delete: deleteOp({
        tag: 'Itinerary — Cars',
        summary: 'Delete a car rental',
        notFound: 'Car rental not found',
      }),
    },

    '/itinerary/places/{dayId}': listPath({
      tag: 'Itinerary — Places',
      summary: 'List the places planned for a day',
      param: 'dayId',
      paramDesc: 'Day identifier',
      schema: 'Place',
    }),
    '/itinerary/places': createPath({
      tag: 'Itinerary — Places',
      summary: 'Add a place to a day',
      bodySchema: 'PlaceCreate',
      schema: 'Place',
    }),
    '/itinerary/places/{id}': {
      put: {
        tags: ['Itinerary — Places'],
        summary: 'Update a place',
        parameters: [pathParam('id', 'Place identifier')],
        requestBody: jsonBody('PlaceUpdate'),
        responses: {
          200: jsonRes('The applied updates', 'Place'),
          400: err('name, lat or lng failed validation'),
          404: err('Place not found'),
          ...authErrors,
        },
      },
      delete: deleteOp({
        tag: 'Itinerary — Places',
        summary: 'Delete a place',
        notFound: 'Place not found',
      }),
    },

    '/config/keys': {
      get: {
        tags: ['Config'],
        summary: 'Read the third-party API keys',
        description:
          'These live as Cloud Run environment variables / Secret Manager, never in the ' +
          "app's own BuildConfig. The app caches the response locally so it still works " +
          'offline after the first successful fetch.',
        responses: {
          200: jsonRes('The current keys', 'Keys'),
          401: err('Missing, invalid or expired Firebase ID token'),
          500: err('Unexpected server error'),
        },
      },
    },

    '/config/web-client-id': {
      get: {
        tags: ['Config'],
        summary: 'Read the Firebase OAuth web client id',
        description:
          "Deliberately unauthenticated and separate from `GET /config/keys` - the app " +
          'needs this value before a user has signed in, to start the Google Sign-In ' +
          "flow in the first place, and it isn't actually a secret per Google's own " +
          'OAuth documentation.',
        security: [],
        responses: {
          200: jsonRes('The current web client id', 'WebClientId'),
          500: err('Unexpected server error'),
        },
      },
    },

    '/cache/{path}': {
      get: {
        tags: ['Cache'],
        summary: 'Read a cached lookup',
        description:
          'Shared across every user - any authenticated caller can read any path. ' +
          '`path` must match `^(weather|currency|places)_[a-zA-Z0-9_]{1,64}$`.',
        parameters: [pathParam('path', 'Cache key, e.g. `weather_cape_town` or `currency_USD_ZAR`')],
        responses: {
          200: jsonRes('The cached entry', 'CacheEntry'),
          400: err('path does not match the allowed pattern'),
          404: err('Not cached yet'),
          ...authErrors,
        },
      },
      put: {
        tags: ['Cache'],
        summary: 'Write a cached lookup',
        description:
          'Shared across every user - any authenticated caller can overwrite any path. ' +
          'Stores the request body verbatim, with no field whitelist.',
        parameters: [pathParam('path', 'Cache key, e.g. `weather_cape_town` or `currency_USD_ZAR`')],
        requestBody: jsonBody('CacheEntry'),
        responses: {
          200: jsonRes('The stored entry', 'CacheEntry'),
          400: err('path does not match the allowed pattern'),
          ...authErrors,
        },
      },
    },

    '/devices': {
      post: {
        tags: ['Devices'],
        summary: 'Register this device for push',
        description:
          'Upserts by token (the document id), so re-registering the same token - a ' +
          'refresh, or a re-login on the same device - overwrites rather than duplicating.',
        requestBody: jsonBody('Device'),
        responses: {
          200: jsonRes('Registered', 'Device'),
          400: err('token is required, or locale failed validation'),
          ...authErrors,
        },
      },
    },

    '/devices/{token}': {
      delete: {
        tags: ['Devices'],
        summary: 'Unregister a device',
        description: 'A no-op (200) if the token is already gone. 403 if it belongs to a different user.',
        parameters: [pathParam('token', 'The FCM token to remove')],
        responses: {
          200: jsonRes('Deleted (or already gone)', 'Deleted'),
          ...authErrors,
        },
      },
    },

    '/account': {
      delete: {
        tags: ['Account'],
        summary: "Delete the caller's account data",
        description:
          "Cascades through every trip the caller owns (and that trip's itinerary children) and " +
          'every device token registered to the caller. Does not delete the Firebase Auth user ' +
          'itself - the client deletes that separately, after this succeeds.',
        responses: {
          200: jsonRes('Deleted', 'Deleted'),
          ...authErrors,
        },
      },
    },

    '/account/settings': {
      get: {
        tags: ['Account'],
        summary: "Get the caller's synced settings",
        description: 'Returns the defaults with updatedAtMs 0 if the caller has never saved settings.',
        responses: {
          200: jsonRes('Settings', 'Settings'),
          ...authErrors,
        },
      },
      put: {
        tags: ['Account'],
        summary: "Save the caller's settings",
        description:
          'Last write wins by updatedAtMs: if the stored copy is newer it is kept and returned with ' +
          'applied: false. Trip-update pushes are skipped while notifyTripUpdates is false.',
        requestBody: jsonBody('Settings'),
        responses: {
          200: jsonRes('Stored settings', 'Settings'),
          400: err('A field is missing or invalid (unknown language, currency not 3 capital letters, etc.)'),
          ...authErrors,
        },
      },
    },

    '/notify/test': {
      post: {
        tags: ['Notify'],
        summary: 'Send a test push to every device the caller has registered',
        responses: {
          200: jsonRes('Send outcome', 'NotifyResult'),
          ...authErrors,
        },
      },
    },
  },
};

module.exports = { openapiSpec };
