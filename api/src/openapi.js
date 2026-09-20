/**
 * OpenAPI 3.0 specification for the Waypoint API.
 * Served as interactive docs at /docs and as raw JSON at /openapi.json
 */

// Reusable response shapes
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

//  Generic itinerary sub-resource path builders
// Most itinerary resources follow the same list / create / delete shape, so the
// path objects are generated rather than repeated five times.
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
        400: err('Required fields missing from the request body'),
        404: err('Parent trip or day not found'),
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

// Spec
const openapiSpec = {
  openapi: '3.0.3',
  info: {
    title: 'Waypoint API',
    version: '1.0.0',
    description:
      'REST API middleware between the Waypoint Android app and Firestore.\n\n' +
      '**Authentication** — every endpoint except `GET /health` and `GET /counter/{key}` ' +
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
    { name: 'Counter', description: 'Atomic named counters' },
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

      Counter: {
        type: 'object',
        properties: {
          key: { type: 'string', example: 'trips_created' },
          count: { type: 'integer', example: 42 },
        },
      },
    },
  },

  // Applied to every operation; individually cleared on the public ones below.
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

    // Trips
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
          'The owner is taken from the verified token, so `accountId` in the body is ignored.',
        requestBody: jsonBody('TripCreate'),
        responses: {
          201: jsonRes('Created', 'Trip'),
          400: err('`id` and `name` are required'),
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
          404: err('Trip not found'),
          ...authErrors,
        },
      },
      delete: {
        tags: ['Trips'],
        summary: 'Delete a trip',
        description: 'Deletes the trip document only; itinerary records are not cascaded.',
        responses: {
          200: jsonRes('Deleted', 'Deleted'),
          404: err('Trip not found'),
          ...authErrors,
        },
      },
    },

    // Itinerary: days
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
      delete: deleteOp({
        tag: 'Itinerary — Days',
        summary: 'Delete a day',
        notFound: 'Day not found',
      }),
    },

    // Itinerary: flights
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
      delete: deleteOp({
        tag: 'Itinerary — Flights',
        summary: 'Delete a flight',
        notFound: 'Flight not found',
      }),
    },

    // Itinerary: lodging
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
      delete: deleteOp({
        tag: 'Itinerary — Lodging',
        summary: 'Delete a lodging booking',
        notFound: 'Lodging not found',
      }),
    },

    // Itinerary: cars
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
      delete: deleteOp({
        tag: 'Itinerary — Cars',
        summary: 'Delete a car rental',
        notFound: 'Car rental not found',
      }),
    },

    // Itinerary: places
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

    // Counter
    '/counter/{key}': {
      get: {
        tags: ['Counter'],
        summary: 'Read a counter',
        description: 'Public read. An unknown key reports a count of `0` rather than 404.',
        security: [],
        parameters: [pathParam('key', 'Counter name, e.g. `trips_created`')],
        responses: {
          200: jsonRes('Current value', 'Counter'),
          500: err('Unexpected server error'),
        },
      },
    },
    '/counter/{key}/increment': {
      post: {
        tags: ['Counter'],
        summary: 'Increment a counter',
        description:
          'Runs inside a Firestore transaction, so concurrent callers cannot double-count. ' +
          'The counter is created at `1` if it does not exist yet.',
        parameters: [pathParam('key', 'Counter name')],
        responses: {
          200: jsonRes('Value after incrementing', 'Counter'),
          401: err('Missing, invalid or expired Firebase ID token'),
          500: err('Unexpected server error'),
        },
      },
    },
    '/counter/{key}/reset': {
      post: {
        tags: ['Counter'],
        summary: 'Reset a counter to zero',
        parameters: [pathParam('key', 'Counter name')],
        responses: {
          200: jsonRes('Value after resetting', 'Counter'),
          401: err('Missing, invalid or expired Firebase ID token'),
          500: err('Unexpected server error'),
        },
      },
    },
  },
};

module.exports = { openapiSpec };
