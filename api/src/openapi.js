
// declares const `errorSchema`,  and opens a multi-line initialiser
const errorSchema = {
  // expression: `type: 'object',`
  type: 'object',
  // continues the statement started above: `properties: { error: { type: 'string', example: 'Trip not f…`
  properties: { error: { type: 'string', example: 'Trip not found' } },
// closes the initialiser of `errorSchema`
};

// declares const `deletedSchema`,  and opens a multi-line initialiser
const deletedSchema = {
  // expression: `type: 'object',`
  type: 'object',
  // continues the statement started above: `properties: { deleted: { type: 'string', example: 'trip_abc…`
  properties: { deleted: { type: 'string', example: 'trip_abc123' } },
// closes the initialiser of `deletedSchema`
};

// declares const `err` as an arrow function taking 1 parameter (`description`) and opens a parenthesised expression
const err = (description) => ({
  // expression: `description,`
  description,
  // continues the statement started above: `content: { 'application/json': { schema: { $ref: '#/compone…`
  content: { 'application/json': { schema: { $ref: '#/components/schemas/Error' } } },
// closes the arrow function `err`
});

// declares const `authErrors`,  and opens a multi-line initialiser
const authErrors = {
  // expression: `401: err('Missing, invalid or expired Firebase ID token'),`
  401: err('Missing, invalid or expired Firebase ID token'),
  // continues the statement started above: `403: err('Authenticated user does not own this resource'),`
  403: err('Authenticated user does not own this resource'),
  // continues the statement started above: `500: err('Unexpected server error'),`
  500: err('Unexpected server error'),
// closes the initialiser of `authErrors`
};

// declares const `jsonBody` as an arrow function taking 2 parameters (`ref`, `required`) and opens a parenthesised expression
const jsonBody = (ref, required = true) => ({
  // expression: `required,`
  required,
  // continues the statement started above: `content: { 'application/json': { schema: { $ref: `#/compone…`
  content: { 'application/json': { schema: { $ref: `#/components/schemas/${ref}` } } },
// closes the arrow function `jsonBody`
});

// declares const `jsonRes` as an arrow function taking 2 parameters (`description`, `ref`) and opens a parenthesised expression
const jsonRes = (description, ref) => ({
  // expression: `description,`
  description,
  // continues the statement started above: `content: { 'application/json': { schema: { $ref: `#/compone…`
  content: { 'application/json': { schema: { $ref: `#/components/schemas/${ref}` } } },
// closes the arrow function `jsonRes`
});

// declares const `jsonListRes` as an arrow function taking 2 parameters (`description`, `ref`) and opens a parenthesised expression
const jsonListRes = (description, ref) => ({
  // expression: `description,`
  description,
  // continues the statement started above: `content: {`
  content: {
    // opens a block after `'application/json':`
    'application/json': {
      // expression: `schema: { type: 'array', items: { $ref: `#/components/schemas/${…`
      schema: { type: 'array', items: { $ref: `#/components/schemas/${ref}` } },
    // closes the block
    },
  // closes the block
  },
// closes the arrow function `jsonListRes`
});

// declares const `pathParam` as an arrow function taking 2 parameters (`name`, `description`) and opens a parenthesised expression
const pathParam = (name, description) => ({
  // expression: `name,`
  name,
  // continues the statement started above: `in: 'path',`
  in: 'path',
  // continues the statement started above: `required: true,`
  required: true,
  // continues the statement started above: `schema: { type: 'string' },`
  schema: { type: 'string' },
  // continues the statement started above: `description,`
  description,
// closes the arrow function `pathParam`
});

// declares function `listPath` taking 1 parameter (a destructured object `{…}`) and opens its body
function listPath({ tag, summary, param, paramDesc, schema }) {
  // returns `{` from the current function
  return {
    // opens a block after `get:`
    get: {
      // expression: `tags: [tag],`
      tags: [tag],
      // continues the statement started above: `summary,`
      summary,
      // continues the statement started above: `parameters: [pathParam(param, paramDesc)],`
      parameters: [pathParam(param, paramDesc)],
      // continues the statement started above: `responses: {`
      responses: {
        // expression: `200: jsonListRes('The matching records, oldest first', schema),`
        200: jsonListRes('The matching records, oldest first', schema),
        // continues the statement started above: `404: err('Parent trip or day not found'),`
        404: err('Parent trip or day not found'),
        // continues the statement started above: `...authErrors,`
        ...authErrors,
      // closes the block
      },
    // closes the block
    },
  // closes the block
  };
// closes the function `listPath`
}

// declares function `createPath` taking 1 parameter (a destructured object `{…}`) and opens its body
function createPath({ tag, summary, bodySchema, schema }) {
  // returns `{` from the current function
  return {
    // opens a block after `post:`
    post: {
      // expression: `tags: [tag],`
      tags: [tag],
      // continues the statement started above: `summary,`
      summary,
      // continues the statement started above: `requestBody: jsonBody(bodySchema),`
      requestBody: jsonBody(bodySchema),
      // continues the statement started above: `responses: {`
      responses: {
        // expression: `201: jsonRes('Created', schema),`
        201: jsonRes('Created', schema),
        200: jsonRes(
          'The caller already owns a record at this id - treated as a successful retry of a create whose response was lost in transit',
          schema
        ),
        // continues the statement started above: `400: err('Required fields missing from the request body'),`
        400: err('Required fields missing from the request body, or a field failed validation (id/parent id not a UUID, invalid date, name too long, dates out of order, lat/lng out of range)'),
        // continues the statement started above: `404: err('Parent trip or day not found'),`
        404: err('Parent trip or day not found'),
        409: err('This id is already in use by a record owned by a different trip'),
        // continues the statement started above: `...authErrors,`
        ...authErrors,
      // closes the block
      },
    // closes the block
    },
  // closes the block
  };
// closes the function `createPath`
}

// declares function `deleteOp` taking 1 parameter (a destructured object `{…}`) and opens its body
function deleteOp({ tag, summary, notFound }) {
  // returns `{` from the current function
  return {
    // expression: `tags: [tag],`
    tags: [tag],
    // continues the statement started above: `summary,`
    summary,
    // continues the statement started above: `parameters: [pathParam('id', 'Record identifier')],`
    parameters: [pathParam('id', 'Record identifier')],
    // continues the statement started above: `responses: {`
    responses: {
      // expression: `200: jsonRes('Deleted', 'Deleted'),`
      200: jsonRes('Deleted', 'Deleted'),
      // continues the statement started above: `404: err(notFound),`
      404: err(notFound),
      // continues the statement started above: `...authErrors,`
      ...authErrors,
    // closes the block
    },
  // closes the block
  };
// closes the function `deleteOp`
}

// declares const `openapiSpec`,  and opens a multi-line initialiser
const openapiSpec = {
  // expression: `openapi: '3.0.3',`
  openapi: '3.0.3',
  // continues the statement started above: `info: {`
  info: {
    // expression: `title: 'Waypoint API',`
    title: 'Waypoint API',
    // continues the statement started above: `version: '1.0.0',`
    version: '1.0.0',
    // continues the statement started above: `description:`
    description:
      // expression: `'REST API middleware between the Waypoint Android app and Firest…`
      'REST API middleware between the Waypoint Android app and Firestore.\n\n' +
      // continues the statement started above: `'**Authentication** — every endpoint except `GET /health` a…`
      '**Authentication** — every endpoint except `GET /health` and `GET /counter/{key}` ' +
      // continues the statement started above: `'requires a Firebase ID token sent as `Authorization: Beare…`
      'requires a Firebase ID token sent as `Authorization: Bearer <token>`. ' +
      // continues the statement started above: `'Use the **Authorize** button above and paste the raw token…`
      'Use the **Authorize** button above and paste the raw token (no `Bearer ` prefix).\n\n' +
      // continues the statement started above: `'**Ownership** — trips are scoped to the authenticated user…`
      '**Ownership** — trips are scoped to the authenticated user via `accountId`. ' +
      // continues the statement started above: `'Itinerary records inherit that check through their parent …`
      'Itinerary records inherit that check through their parent trip, so requesting ' +
      // continues the statement started above: `"another user's data returns `403 Forbidden`.\n\n" +`
      "another user's data returns `403 Forbidden`.\n\n" +
      // continues the statement started above: `'**Identifiers** — the client generates document ids and se…`
      '**Identifiers** — the client generates document ids and sends them in the request ' +
      // continues the statement started above: `'body, which keeps offline-created records stable once they…`
      'body, which keeps offline-created records stable once they sync.',
  // closes the block
  },
  // continues the statement started above: `servers: [`
  servers: [
    // continues the statement started above: `{ url: '/', description: 'This server' },`
    { url: '/', description: 'This server' },
    // continues the statement started above: `{ url: 'http://localhost:8080', description: 'Local develop…`
    { url: 'http://localhost:8080', description: 'Local development' },
  // closes the multi-line list / array started above
  ],
  // continues the statement started above: `tags: [`
  tags: [
    // continues the statement started above: `{ name: 'Health', description: 'Service liveness probe' },`
    { name: 'Health', description: 'Service liveness probe' },
    // continues the statement started above: `{ name: 'Trips', description: 'Trips owned by the authentic…`
    { name: 'Trips', description: 'Trips owned by the authenticated user' },
    // continues the statement started above: `{ name: 'Itinerary — Days', description: 'Calendar days bel…`
    { name: 'Itinerary — Days', description: 'Calendar days belonging to a trip' },
    // continues the statement started above: `{ name: 'Itinerary — Flights', description: 'Flights attach…`
    { name: 'Itinerary — Flights', description: 'Flights attached to a day' },
    // continues the statement started above: `{ name: 'Itinerary — Lodging', description: 'Accommodation …`
    { name: 'Itinerary — Lodging', description: 'Accommodation bookings for a trip' },
    // continues the statement started above: `{ name: 'Itinerary — Cars', description: 'Car rentals for a…`
    { name: 'Itinerary — Cars', description: 'Car rentals for a trip' },
    // continues the statement started above: `{ name: 'Itinerary — Places', description: 'Places of inter…`
    { name: 'Itinerary — Places', description: 'Places of interest planned for a day' },
    // continues the statement started above: `{ name: 'Counter', description: 'Atomic named counters' },`
    { name: 'Counter', description: 'Atomic named counters' },
    { name: 'Config', description: 'Server-held API keys and the Firebase web client id' },
    { name: 'Cache', description: 'Shared weather/currency/places lookup cache' },
    { name: 'Devices', description: 'FCM push tokens registered per user' },
    { name: 'Notify', description: 'Push notification test endpoint' },
  // closes the multi-line list / array started above
  ],
  // continues the statement started above: `components: {`
  components: {
    // opens a block after `securitySchemes:`
    securitySchemes: {
      // opens a block after `firebaseAuth:`
      firebaseAuth: {
        // expression: `type: 'http',`
        type: 'http',
        // continues the statement started above: `scheme: 'bearer',`
        scheme: 'bearer',
        // continues the statement started above: `bearerFormat: 'JWT',`
        bearerFormat: 'JWT',
        // continues the statement started above: `description: 'Firebase ID token obtained by the Android cli…`
        description: 'Firebase ID token obtained by the Android client after sign-in.',
      // closes the block
      },
    // closes the block
    },
    // continues the statement started above: `schemas: {`
    schemas: {
      // expression: `Error: errorSchema,`
      Error: errorSchema,
      // continues the statement started above: `Deleted: deletedSchema,`
      Deleted: deletedSchema,

      // continues the statement started above: `Health: {`
      Health: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `properties: {`
        properties: {
          // expression: `status: { type: 'string', example: 'ok' },`
          status: { type: 'string', example: 'ok' },
          // continues the statement started above: `service: { type: 'string', example: 'waypoint-api' },`
          service: { type: 'string', example: 'waypoint-api' },
        // closes the block
        },
      // closes the block
      },

      // continues the statement started above: `Trip: {`
      Trip: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `properties: {`
        properties: {
          // expression: `id: { type: 'string', example: 'trip_abc123' },`
          id: { type: 'string', example: 'trip_abc123' },
          // continues the statement started above: `accountId: {`
          accountId: {
            // expression: `type: 'string',`
            type: 'string',
            // continues the statement started above: `description:`
            description:
              // expression: `'Firebase uid of the owner. Set by the server; never accepted fr…`
              'Firebase uid of the owner. Set by the server; never accepted from the client.',
            // continues the statement started above: `readOnly: true,`
            readOnly: true,
          // closes the block
          },
          // continues the statement started above: `name: { type: 'string', example: 'Tokyo, Spring 2026' },`
          name: { type: 'string', example: 'Tokyo, Spring 2026' },
          // continues the statement started above: `startDate: { type: 'string', nullable: true, example: '2026…`
          startDate: { type: 'string', nullable: true, example: '2026-04-02' },
          // continues the statement started above: `endDate: { type: 'string', nullable: true, example: '2026-0…`
          endDate: { type: 'string', nullable: true, example: '2026-04-16' },
          // continues the statement started above: `destination: { type: 'string', nullable: true, example: 'To…`
          destination: { type: 'string', nullable: true, example: 'Tokyo, Japan' },
          // continues the statement started above: `destLat: { type: 'number', format: 'double', nullable: true…`
          destLat: { type: 'number', format: 'double', nullable: true, example: 35.6762 },
          // continues the statement started above: `destLng: { type: 'number', format: 'double', nullable: true…`
          destLng: { type: 'number', format: 'double', nullable: true, example: 139.6503 },
          // continues the statement started above: `destPhotoUrl: { type: 'string', nullable: true },`
          destPhotoUrl: { type: 'string', nullable: true },
          // continues the statement started above: `createdAtMs: { type: 'integer', format: 'int64', example: 1…`
          createdAtMs: { type: 'integer', format: 'int64', example: 1774000000000 },
          // continues the statement started above: `updatedAtMs: { type: 'integer', format: 'int64', example: 1…`
          updatedAtMs: { type: 'integer', format: 'int64', example: 1774000000000 },
        // closes the block
        },
      // closes the block
      },
      // continues the statement started above: `TripCreate: {`
      TripCreate: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `required: ['id', 'name'],`
        required: ['id', 'name'],
        // continues the statement started above: `properties: {`
        properties: {
          // opens a block after `id:`
          id: {
            // expression: `type: 'string',`
            type: 'string',
            // continues the statement started above: `description: 'Client-generated document id',`
            description: 'Client-generated document id',
            // continues the statement started above: `example: 'trip_abc123',`
            example: 'trip_abc123',
          // closes the block
          },
          // continues the statement started above: `name: { type: 'string', example: 'Tokyo, Spring 2026' },`
          name: { type: 'string', example: 'Tokyo, Spring 2026' },
          // continues the statement started above: `startDate: { type: 'string', nullable: true, example: '2026…`
          startDate: { type: 'string', nullable: true, example: '2026-04-02' },
          // continues the statement started above: `endDate: { type: 'string', nullable: true, example: '2026-0…`
          endDate: { type: 'string', nullable: true, example: '2026-04-16' },
          // continues the statement started above: `destination: { type: 'string', nullable: true, example: 'To…`
          destination: { type: 'string', nullable: true, example: 'Tokyo, Japan' },
          // continues the statement started above: `destLat: { type: 'number', format: 'double', nullable: true…`
          destLat: { type: 'number', format: 'double', nullable: true, example: 35.6762 },
          // continues the statement started above: `destLng: { type: 'number', format: 'double', nullable: true…`
          destLng: { type: 'number', format: 'double', nullable: true, example: 139.6503 },
          // continues the statement started above: `destPhotoUrl: { type: 'string', nullable: true },`
          destPhotoUrl: { type: 'string', nullable: true },
          // continues the statement started above: `createdAtMs: { type: 'integer', format: 'int64', descriptio…`
          createdAtMs: { type: 'integer', format: 'int64', description: 'Defaults to now' },
          // continues the statement started above: `updatedAtMs: { type: 'integer', format: 'int64', descriptio…`
          updatedAtMs: { type: 'integer', format: 'int64', description: 'Defaults to now' },
        // closes the block
        },
      // closes the block
      },
      // continues the statement started above: `TripUpdate: {`
      TripUpdate: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `description:`
        description:
          // expression: `'Any subset of the mutable trip fields. `updatedAtMs` is set by …`
          'Any subset of the mutable trip fields. `updatedAtMs` is set by the server and ' +
          // continues the statement started above: `'`accountId` is stripped so ownership cannot be reassigned.…`
          '`accountId` is stripped so ownership cannot be reassigned.',
        // continues the statement started above: `properties: {`
        properties: {
          // expression: `name: { type: 'string', example: 'Tokyo & Kyoto, Spring 2026' },`
          name: { type: 'string', example: 'Tokyo & Kyoto, Spring 2026' },
          // continues the statement started above: `startDate: { type: 'string', nullable: true },`
          startDate: { type: 'string', nullable: true },
          // continues the statement started above: `endDate: { type: 'string', nullable: true },`
          endDate: { type: 'string', nullable: true },
          // continues the statement started above: `destination: { type: 'string', nullable: true },`
          destination: { type: 'string', nullable: true },
          // continues the statement started above: `destLat: { type: 'number', format: 'double', nullable: true…`
          destLat: { type: 'number', format: 'double', nullable: true },
          // continues the statement started above: `destLng: { type: 'number', format: 'double', nullable: true…`
          destLng: { type: 'number', format: 'double', nullable: true },
          // continues the statement started above: `destPhotoUrl: { type: 'string', nullable: true },`
          destPhotoUrl: { type: 'string', nullable: true },
        // closes the block
        },
      // closes the block
      },

      // continues the statement started above: `Day: {`
      Day: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `properties: {`
        properties: {
          // expression: `id: { type: 'string', example: 'day_20260402' },`
          id: { type: 'string', example: 'day_20260402' },
          // continues the statement started above: `tripId: { type: 'string', example: 'trip_abc123' },`
          tripId: { type: 'string', example: 'trip_abc123' },
          // continues the statement started above: `date: {`
          date: {
            // expression: `type: 'string',`
            type: 'string',
            // continues the statement started above: `description: 'ISO date, sorted ascending',`
            description: 'ISO date, sorted ascending',
            // continues the statement started above: `example: '2026-04-02',`
            example: '2026-04-02',
          // closes the block
          },
        // closes the block
        },
      // closes the block
      },
      // continues the statement started above: `DayCreate: {`
      DayCreate: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `required: ['id', 'tripId', 'date'],`
        required: ['id', 'tripId', 'date'],
        // continues the statement started above: `properties: {`
        properties: {
          // expression: `id: { type: 'string', example: 'day_20260402' },`
          id: { type: 'string', example: 'day_20260402' },
          // continues the statement started above: `tripId: { type: 'string', example: 'trip_abc123' },`
          tripId: { type: 'string', example: 'trip_abc123' },
          // continues the statement started above: `date: { type: 'string', example: '2026-04-02' },`
          date: { type: 'string', example: '2026-04-02' },
        // closes the block
        },
      // closes the block
      },
      DayUpdate: {
        type: 'object',
        description: 'Only `date` is mutable; `tripId` is the parent link.',
        properties: {
          date: { type: 'string', example: '2026-04-02' },
        },
      },

      // continues the statement started above: `Flight: {`
      Flight: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `properties: {`
        properties: {
          // expression: `id: { type: 'string', example: 'flight_nh204' },`
          id: { type: 'string', example: 'flight_nh204' },
          // continues the statement started above: `dayId: { type: 'string', example: 'day_20260402' },`
          dayId: { type: 'string', example: 'day_20260402' },
          // continues the statement started above: `flightNumber: { type: 'string', nullable: true, example: 'N…`
          flightNumber: { type: 'string', nullable: true, example: 'NH204' },
          // continues the statement started above: `departureTime: { type: 'string', nullable: true, example: '…`
          departureTime: { type: 'string', nullable: true, example: '2026-04-02T09:45' },
          // continues the statement started above: `pdfUri: {`
          pdfUri: {
            // expression: `type: 'string',`
            type: 'string',
            // continues the statement started above: `nullable: true,`
            nullable: true,
            // continues the statement started above: `description: 'Storage URI of the boarding pass',`
            description: 'Storage URI of the boarding pass',
          // closes the block
          },
          // continues the statement started above: `docName: { type: 'string', nullable: true, example: 'boardi…`
          docName: { type: 'string', nullable: true, example: 'boarding-pass.pdf' },
          // continues the statement started above: `createdAtMs: { type: 'integer', format: 'int64' },`
          createdAtMs: { type: 'integer', format: 'int64' },
        // closes the block
        },
      // closes the block
      },
      // continues the statement started above: `FlightCreate: {`
      FlightCreate: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `required: ['id', 'dayId'],`
        required: ['id', 'dayId'],
        // continues the statement started above: `properties: {`
        properties: {
          // expression: `id: { type: 'string', example: 'flight_nh204' },`
          id: { type: 'string', example: 'flight_nh204' },
          // continues the statement started above: `dayId: { type: 'string', example: 'day_20260402' },`
          dayId: { type: 'string', example: 'day_20260402' },
          // continues the statement started above: `flightNumber: { type: 'string', nullable: true, example: 'N…`
          flightNumber: { type: 'string', nullable: true, example: 'NH204' },
          // continues the statement started above: `departureTime: { type: 'string', nullable: true, example: '…`
          departureTime: { type: 'string', nullable: true, example: '2026-04-02T09:45' },
          // continues the statement started above: `pdfUri: { type: 'string', nullable: true },`
          pdfUri: { type: 'string', nullable: true },
          // continues the statement started above: `docName: { type: 'string', nullable: true },`
          docName: { type: 'string', nullable: true },
        // closes the block
        },
      // closes the block
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

      // continues the statement started above: `Lodging: {`
      Lodging: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `properties: {`
        properties: {
          // expression: `id: { type: 'string', example: 'lodging_shinjuku' },`
          id: { type: 'string', example: 'lodging_shinjuku' },
          // continues the statement started above: `tripId: { type: 'string', example: 'trip_abc123' },`
          tripId: { type: 'string', example: 'trip_abc123' },
          // continues the statement started above: `fromDate: { type: 'string', nullable: true, example: '2026-…`
          fromDate: { type: 'string', nullable: true, example: '2026-04-02' },
          // continues the statement started above: `toDate: { type: 'string', nullable: true, example: '2026-04…`
          toDate: { type: 'string', nullable: true, example: '2026-04-09' },
          // continues the statement started above: `pdfUri: { type: 'string', nullable: true },`
          pdfUri: { type: 'string', nullable: true },
          // continues the statement started above: `docName: { type: 'string', nullable: true, example: 'hotel-…`
          docName: { type: 'string', nullable: true, example: 'hotel-confirmation.pdf' },
          // continues the statement started above: `createdAtMs: { type: 'integer', format: 'int64' },`
          createdAtMs: { type: 'integer', format: 'int64' },
        // closes the block
        },
      // closes the block
      },
      // continues the statement started above: `LodgingCreate: {`
      LodgingCreate: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `required: ['id', 'tripId'],`
        required: ['id', 'tripId'],
        // continues the statement started above: `properties: {`
        properties: {
          // expression: `id: { type: 'string', example: 'lodging_shinjuku' },`
          id: { type: 'string', example: 'lodging_shinjuku' },
          // continues the statement started above: `tripId: { type: 'string', example: 'trip_abc123' },`
          tripId: { type: 'string', example: 'trip_abc123' },
          // continues the statement started above: `fromDate: { type: 'string', nullable: true, example: '2026-…`
          fromDate: { type: 'string', nullable: true, example: '2026-04-02' },
          // continues the statement started above: `toDate: { type: 'string', nullable: true, example: '2026-04…`
          toDate: { type: 'string', nullable: true, example: '2026-04-09' },
          // continues the statement started above: `pdfUri: { type: 'string', nullable: true },`
          pdfUri: { type: 'string', nullable: true },
          // continues the statement started above: `docName: { type: 'string', nullable: true },`
          docName: { type: 'string', nullable: true },
        // closes the block
        },
      // closes the block
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

      // continues the statement started above: `Car: {`
      Car: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `properties: {`
        properties: {
          // expression: `id: { type: 'string', example: 'car_toyota' },`
          id: { type: 'string', example: 'car_toyota' },
          // continues the statement started above: `tripId: { type: 'string', example: 'trip_abc123' },`
          tripId: { type: 'string', example: 'trip_abc123' },
          // continues the statement started above: `fromDate: { type: 'string', nullable: true, example: '2026-…`
          fromDate: { type: 'string', nullable: true, example: '2026-04-09' },
          // continues the statement started above: `toDate: { type: 'string', nullable: true, example: '2026-04…`
          toDate: { type: 'string', nullable: true, example: '2026-04-12' },
          // continues the statement started above: `pdfUri: { type: 'string', nullable: true },`
          pdfUri: { type: 'string', nullable: true },
          // continues the statement started above: `docName: { type: 'string', nullable: true, example: 'rental…`
          docName: { type: 'string', nullable: true, example: 'rental-agreement.pdf' },
          // continues the statement started above: `createdAtMs: { type: 'integer', format: 'int64' },`
          createdAtMs: { type: 'integer', format: 'int64' },
        // closes the block
        },
      // closes the block
      },
      // continues the statement started above: `CarCreate: {`
      CarCreate: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `required: ['id', 'tripId'],`
        required: ['id', 'tripId'],
        // continues the statement started above: `properties: {`
        properties: {
          // expression: `id: { type: 'string', example: 'car_toyota' },`
          id: { type: 'string', example: 'car_toyota' },
          // continues the statement started above: `tripId: { type: 'string', example: 'trip_abc123' },`
          tripId: { type: 'string', example: 'trip_abc123' },
          // continues the statement started above: `fromDate: { type: 'string', nullable: true, example: '2026-…`
          fromDate: { type: 'string', nullable: true, example: '2026-04-09' },
          // continues the statement started above: `toDate: { type: 'string', nullable: true, example: '2026-04…`
          toDate: { type: 'string', nullable: true, example: '2026-04-12' },
          // continues the statement started above: `pdfUri: { type: 'string', nullable: true },`
          pdfUri: { type: 'string', nullable: true },
          // continues the statement started above: `docName: { type: 'string', nullable: true },`
          docName: { type: 'string', nullable: true },
        // closes the block
        },
      // closes the block
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

      // continues the statement started above: `Place: {`
      Place: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `properties: {`
        properties: {
          // expression: `id: { type: 'string', example: 'place_sensoji' },`
          id: { type: 'string', example: 'place_sensoji' },
          // continues the statement started above: `dayId: { type: 'string', example: 'day_20260402' },`
          dayId: { type: 'string', example: 'day_20260402' },
          // continues the statement started above: `name: { type: 'string', example: 'Senso-ji Temple' },`
          name: { type: 'string', example: 'Senso-ji Temple' },
          // continues the statement started above: `category: { type: 'string', nullable: true, example: 'Landm…`
          category: { type: 'string', nullable: true, example: 'Landmark' },
          // continues the statement started above: `lat: { type: 'number', format: 'double', nullable: true, ex…`
          lat: { type: 'number', format: 'double', nullable: true, example: 35.7148 },
          // continues the statement started above: `lng: { type: 'number', format: 'double', nullable: true, ex…`
          lng: { type: 'number', format: 'double', nullable: true, example: 139.7967 },
          // continues the statement started above: `note: { type: 'string', nullable: true, example: 'Go early …`
          note: { type: 'string', nullable: true, example: 'Go early to beat the crowds' },
          // continues the statement started above: `photoUrl: { type: 'string', nullable: true },`
          photoUrl: { type: 'string', nullable: true },
          // continues the statement started above: `createdAtMs: { type: 'integer', format: 'int64' },`
          createdAtMs: { type: 'integer', format: 'int64' },
        // closes the block
        },
      // closes the block
      },
      // continues the statement started above: `PlaceCreate: {`
      PlaceCreate: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `required: ['id', 'dayId', 'name'],`
        required: ['id', 'dayId', 'name'],
        // continues the statement started above: `properties: {`
        properties: {
          // expression: `id: { type: 'string', example: 'place_sensoji' },`
          id: { type: 'string', example: 'place_sensoji' },
          // continues the statement started above: `dayId: { type: 'string', example: 'day_20260402' },`
          dayId: { type: 'string', example: 'day_20260402' },
          // continues the statement started above: `name: { type: 'string', example: 'Senso-ji Temple' },`
          name: { type: 'string', example: 'Senso-ji Temple' },
          // continues the statement started above: `category: { type: 'string', nullable: true, example: 'Landm…`
          category: { type: 'string', nullable: true, example: 'Landmark' },
          // continues the statement started above: `lat: { type: 'number', format: 'double', nullable: true, ex…`
          lat: { type: 'number', format: 'double', nullable: true, example: 35.7148 },
          // continues the statement started above: `lng: { type: 'number', format: 'double', nullable: true, ex…`
          lng: { type: 'number', format: 'double', nullable: true, example: 139.7967 },
          // continues the statement started above: `note: { type: 'string', nullable: true },`
          note: { type: 'string', nullable: true },
          // continues the statement started above: `photoUrl: { type: 'string', nullable: true },`
          photoUrl: { type: 'string', nullable: true },
        // closes the block
        },
      // closes the block
      },
      // continues the statement started above: `PlaceUpdate: {`
      PlaceUpdate: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `description: 'Any subset of the place fields; merged into t…`
        description: 'Any subset of the place fields; merged into the existing record.',
        // continues the statement started above: `properties: {`
        properties: {
          // expression: `name: { type: 'string' },`
          name: { type: 'string' },
          // continues the statement started above: `category: { type: 'string', nullable: true },`
          category: { type: 'string', nullable: true },
          // continues the statement started above: `lat: { type: 'number', format: 'double', nullable: true },`
          lat: { type: 'number', format: 'double', nullable: true },
          // continues the statement started above: `lng: { type: 'number', format: 'double', nullable: true },`
          lng: { type: 'number', format: 'double', nullable: true },
          // continues the statement started above: `note: { type: 'string', nullable: true },`
          note: { type: 'string', nullable: true },
          // continues the statement started above: `photoUrl: { type: 'string', nullable: true },`
          photoUrl: { type: 'string', nullable: true },
        // closes the block
        },
      // closes the block
      },

      // continues the statement started above: `Counter: {`
      Counter: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `properties: {`
        properties: {
          // expression: `key: { type: 'string', example: 'trips_created' },`
          key: { type: 'string', example: 'trips_created' },
          // continues the statement started above: `count: { type: 'integer', example: 42 },`
          count: { type: 'integer', example: 42 },
        // closes the block
        },
      // closes the block
      },

      // continues the statement started above: `Keys: {`
      Keys: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `description:`
        description:
          'Always includes every key, falling back to an empty string if that env var is unset server-side.',
        // continues the statement started above: `properties: {`
        properties: {
          LOCATIONIQ_API_KEY: { type: 'string' },
          EXCHANGERATE_API_KEY: { type: 'string' },
          COUNTERAPI_API_KEY: { type: 'string' },
          COUNTERAPI_WORKSPACE: { type: 'string' },
          COUNTERAPI_PLACES_SLUG: { type: 'string' },
          OPENWEATHER_API_KEY: { type: 'string' },
          AIRLABS_API_KEY: { type: 'string' },
        // closes the block
        },
      // closes the block
      },

      // continues the statement started above: `WebClientId: {`
      WebClientId: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `properties: {`
        properties: {
          FIREBASE_WEB_CLIENT_ID: { type: 'string' },
        // closes the block
        },
      // closes the block
      },

      // continues the statement started above: `CacheEntry: {`
      CacheEntry: {
        // expression: `type: 'object',`
        type: 'object',
        // continues the statement started above: `description: 'Caller-defined shape - whatever was last PUT …`
        description: 'Caller-defined shape - whatever was last PUT to this path, stored and returned verbatim.',
        // continues the statement started above: `additionalProperties: true,`
        additionalProperties: true,
      // closes the block
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
    // closes the block
    },
  // closes the block
  },

  // continues the statement started above: `security: [{ firebaseAuth: [] }],`
  security: [{ firebaseAuth: [] }],

  // continues the statement started above: `paths: {`
  paths: {
    // opens a block after `'/health':`
    '/health': {
      // opens a block after `get:`
      get: {
        // expression: `tags: ['Health'],`
        tags: ['Health'],
        // continues the statement started above: `summary: 'Liveness probe',`
        summary: 'Liveness probe',
        // continues the statement started above: `security: [],`
        security: [],
        // continues the statement started above: `responses: { 200: jsonRes('Service is up', 'Health') },`
        responses: { 200: jsonRes('Service is up', 'Health') },
      // closes the block
      },
    // closes the block
    },

    // continues the statement started above: `'/trips': {`
    '/trips': {
      // opens a block after `get:`
      get: {
        // expression: `tags: ['Trips'],`
        tags: ['Trips'],
        // continues the statement started above: `summary: 'List the trips of the authenticated user',`
        summary: 'List the trips of the authenticated user',
        // continues the statement started above: `description: 'Returns trips owned by the caller, newest fir…`
        description: 'Returns trips owned by the caller, newest first.',
        // continues the statement started above: `responses: { 200: jsonListRes('Trips, newest first', 'Trip'…`
        responses: { 200: jsonListRes('Trips, newest first', 'Trip'), ...authErrors },
      // closes the block
      },
      // continues the statement started above: `post: {`
      post: {
        // expression: `tags: ['Trips'],`
        tags: ['Trips'],
        // continues the statement started above: `summary: 'Create a trip',`
        summary: 'Create a trip',
        // continues the statement started above: `description:`
        description:
          // expression: `'The owner is taken from the verified token, so `accountId` in t…`
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
        // continues the statement started above: `requestBody: jsonBody('TripCreate'),`
        requestBody: jsonBody('TripCreate'),
        // continues the statement started above: `responses: {`
        responses: {
          // expression: `201: jsonRes('Created', 'Trip'),`
          201: jsonRes('Created', 'Trip'),
          200: jsonRes(
            'The caller already owns a trip at this id - treated as a successful retry of a create whose response was lost in transit',
            'Trip'
          ),
          // continues the statement started above: `400: err('`id` and `name` are required'),`
          400: err('`id` and `name` are required, or a field failed validation (id not a UUID, name too long, invalid date, or startDate after endDate)'),
          409: err('This id is already in use by a trip owned by a different account'),
          // continues the statement started above: `...authErrors,`
          ...authErrors,
        // closes the block
        },
      // closes the block
      },
    // closes the block
    },
    // continues the statement started above: `'/trips/{id}': {`
    '/trips/{id}': {
      // expression: `parameters: [pathParam('id', 'Trip identifier')],`
      parameters: [pathParam('id', 'Trip identifier')],
      // continues the statement started above: `get: {`
      get: {
        // expression: `tags: ['Trips'],`
        tags: ['Trips'],
        // continues the statement started above: `summary: 'Fetch a single trip',`
        summary: 'Fetch a single trip',
        // continues the statement started above: `responses: {`
        responses: {
          // expression: `200: jsonRes('The trip', 'Trip'),`
          200: jsonRes('The trip', 'Trip'),
          // continues the statement started above: `404: err('Trip not found'),`
          404: err('Trip not found'),
          // continues the statement started above: `...authErrors,`
          ...authErrors,
        // closes the block
        },
      // closes the block
      },
      // continues the statement started above: `put: {`
      put: {
        // expression: `tags: ['Trips'],`
        tags: ['Trips'],
        // continues the statement started above: `summary: 'Update a trip',`
        summary: 'Update a trip',
        // continues the statement started above: `requestBody: jsonBody('TripUpdate'),`
        requestBody: jsonBody('TripUpdate'),
        // continues the statement started above: `responses: {`
        responses: {
          // expression: `200: jsonRes('The applied updates', 'Trip'),`
          200: jsonRes('The applied updates', 'Trip'),
          400: err('A field failed validation (name too long, invalid date, or the resulting startDate/endDate out of order)'),
          // continues the statement started above: `404: err('Trip not found'),`
          404: err('Trip not found'),
          // continues the statement started above: `...authErrors,`
          ...authErrors,
        // closes the block
        },
      // closes the block
      },
      // continues the statement started above: `delete: {`
      delete: {
        // expression: `tags: ['Trips'],`
        tags: ['Trips'],
        // continues the statement started above: `summary: 'Delete a trip',`
        summary: 'Delete a trip',
        // continues the statement started above: `description: 'Deletes the trip document only; itinerary rec…`
        description: 'Cascades: every itinerary day, flight, lodging booking, car rental and place that references this trip is deleted first, then the trip document itself.',
        // continues the statement started above: `responses: {`
        responses: {
          // expression: `200: jsonRes('Deleted', 'Deleted'),`
          200: jsonRes('Deleted', 'Deleted'),
          // continues the statement started above: `404: err('Trip not found'),`
          404: err('Trip not found'),
          // continues the statement started above: `...authErrors,`
          ...authErrors,
        // closes the block
        },
      // closes the block
      },
    // closes the block
    },

    // continues the statement started above: `'/itinerary/days/{tripId}': listPath({`
    '/itinerary/days/{tripId}': listPath({
      // expression: `tag: 'Itinerary — Days',`
      tag: 'Itinerary — Days',
      // continues the statement started above: `summary: 'List the days of a trip',`
      summary: 'List the days of a trip',
      // continues the statement started above: `param: 'tripId',`
      param: 'tripId',
      // continues the statement started above: `paramDesc: 'Trip identifier',`
      paramDesc: 'Trip identifier',
      // continues the statement started above: `schema: 'Day',`
      schema: 'Day',
    // expression: `}),`
    }),
    // continues the statement started above: `'/itinerary/days': createPath({`
    '/itinerary/days': createPath({
      // expression: `tag: 'Itinerary — Days',`
      tag: 'Itinerary — Days',
      // continues the statement started above: `summary: 'Add a day to a trip',`
      summary: 'Add a day to a trip',
      // continues the statement started above: `bodySchema: 'DayCreate',`
      bodySchema: 'DayCreate',
      // continues the statement started above: `schema: 'Day',`
      schema: 'Day',
    // expression: `}),`
    }),
    // continues the statement started above: `'/itinerary/days/{id}': {`
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
      // opens a block after `delete: deleteOp(`
      delete: deleteOp({
        // expression: `tag: 'Itinerary — Days',`
        tag: 'Itinerary — Days',
        // continues the statement started above: `summary: 'Delete a day',`
        summary: 'Delete a day',
        // continues the statement started above: `notFound: 'Day not found',`
        notFound: 'Day not found',
      // expression: `}),`
      }),
    // closes the block
    },

    // continues the statement started above: `'/itinerary/flights/{dayId}': listPath({`
    '/itinerary/flights/{dayId}': listPath({
      // expression: `tag: 'Itinerary — Flights',`
      tag: 'Itinerary — Flights',
      // continues the statement started above: `summary: 'List the flights on a day',`
      summary: 'List the flights on a day',
      // continues the statement started above: `param: 'dayId',`
      param: 'dayId',
      // continues the statement started above: `paramDesc: 'Day identifier',`
      paramDesc: 'Day identifier',
      // continues the statement started above: `schema: 'Flight',`
      schema: 'Flight',
    // expression: `}),`
    }),
    // continues the statement started above: `'/itinerary/flights': createPath({`
    '/itinerary/flights': createPath({
      // expression: `tag: 'Itinerary — Flights',`
      tag: 'Itinerary — Flights',
      // continues the statement started above: `summary: 'Add a flight to a day',`
      summary: 'Add a flight to a day',
      // continues the statement started above: `bodySchema: 'FlightCreate',`
      bodySchema: 'FlightCreate',
      // continues the statement started above: `schema: 'Flight',`
      schema: 'Flight',
    // expression: `}),`
    }),
    // continues the statement started above: `'/itinerary/flights/{id}': {`
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
      // opens a block after `delete: deleteOp(`
      delete: deleteOp({
        // expression: `tag: 'Itinerary — Flights',`
        tag: 'Itinerary — Flights',
        // continues the statement started above: `summary: 'Delete a flight',`
        summary: 'Delete a flight',
        // continues the statement started above: `notFound: 'Flight not found',`
        notFound: 'Flight not found',
      // expression: `}),`
      }),
    // closes the block
    },

    // continues the statement started above: `'/itinerary/lodging/{tripId}': listPath({`
    '/itinerary/lodging/{tripId}': listPath({
      // expression: `tag: 'Itinerary — Lodging',`
      tag: 'Itinerary — Lodging',
      // continues the statement started above: `summary: 'List the lodging bookings for a trip',`
      summary: 'List the lodging bookings for a trip',
      // continues the statement started above: `param: 'tripId',`
      param: 'tripId',
      // continues the statement started above: `paramDesc: 'Trip identifier',`
      paramDesc: 'Trip identifier',
      // continues the statement started above: `schema: 'Lodging',`
      schema: 'Lodging',
    // expression: `}),`
    }),
    // continues the statement started above: `'/itinerary/lodging': createPath({`
    '/itinerary/lodging': createPath({
      // expression: `tag: 'Itinerary — Lodging',`
      tag: 'Itinerary — Lodging',
      // continues the statement started above: `summary: 'Add a lodging booking to a trip',`
      summary: 'Add a lodging booking to a trip',
      // continues the statement started above: `bodySchema: 'LodgingCreate',`
      bodySchema: 'LodgingCreate',
      // continues the statement started above: `schema: 'Lodging',`
      schema: 'Lodging',
    // expression: `}),`
    }),
    // continues the statement started above: `'/itinerary/lodging/{id}': {`
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
      // opens a block after `delete: deleteOp(`
      delete: deleteOp({
        // expression: `tag: 'Itinerary — Lodging',`
        tag: 'Itinerary — Lodging',
        // continues the statement started above: `summary: 'Delete a lodging booking',`
        summary: 'Delete a lodging booking',
        // continues the statement started above: `notFound: 'Lodging not found',`
        notFound: 'Lodging not found',
      // expression: `}),`
      }),
    // closes the block
    },

    // continues the statement started above: `'/itinerary/cars/{tripId}': listPath({`
    '/itinerary/cars/{tripId}': listPath({
      // expression: `tag: 'Itinerary — Cars',`
      tag: 'Itinerary — Cars',
      // continues the statement started above: `summary: 'List the car rentals for a trip',`
      summary: 'List the car rentals for a trip',
      // continues the statement started above: `param: 'tripId',`
      param: 'tripId',
      // continues the statement started above: `paramDesc: 'Trip identifier',`
      paramDesc: 'Trip identifier',
      // continues the statement started above: `schema: 'Car',`
      schema: 'Car',
    // expression: `}),`
    }),
    // continues the statement started above: `'/itinerary/cars': createPath({`
    '/itinerary/cars': createPath({
      // expression: `tag: 'Itinerary — Cars',`
      tag: 'Itinerary — Cars',
      // continues the statement started above: `summary: 'Add a car rental to a trip',`
      summary: 'Add a car rental to a trip',
      // continues the statement started above: `bodySchema: 'CarCreate',`
      bodySchema: 'CarCreate',
      // continues the statement started above: `schema: 'Car',`
      schema: 'Car',
    // expression: `}),`
    }),
    // continues the statement started above: `'/itinerary/cars/{id}': {`
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
      // opens a block after `delete: deleteOp(`
      delete: deleteOp({
        // expression: `tag: 'Itinerary — Cars',`
        tag: 'Itinerary — Cars',
        // continues the statement started above: `summary: 'Delete a car rental',`
        summary: 'Delete a car rental',
        // continues the statement started above: `notFound: 'Car rental not found',`
        notFound: 'Car rental not found',
      // expression: `}),`
      }),
    // closes the block
    },

    // continues the statement started above: `'/itinerary/places/{dayId}': listPath({`
    '/itinerary/places/{dayId}': listPath({
      // expression: `tag: 'Itinerary — Places',`
      tag: 'Itinerary — Places',
      // continues the statement started above: `summary: 'List the places planned for a day',`
      summary: 'List the places planned for a day',
      // continues the statement started above: `param: 'dayId',`
      param: 'dayId',
      // continues the statement started above: `paramDesc: 'Day identifier',`
      paramDesc: 'Day identifier',
      // continues the statement started above: `schema: 'Place',`
      schema: 'Place',
    // expression: `}),`
    }),
    // continues the statement started above: `'/itinerary/places': createPath({`
    '/itinerary/places': createPath({
      // expression: `tag: 'Itinerary — Places',`
      tag: 'Itinerary — Places',
      // continues the statement started above: `summary: 'Add a place to a day',`
      summary: 'Add a place to a day',
      // continues the statement started above: `bodySchema: 'PlaceCreate',`
      bodySchema: 'PlaceCreate',
      // continues the statement started above: `schema: 'Place',`
      schema: 'Place',
    // expression: `}),`
    }),
    // continues the statement started above: `'/itinerary/places/{id}': {`
    '/itinerary/places/{id}': {
      // opens a block after `put:`
      put: {
        // expression: `tags: ['Itinerary — Places'],`
        tags: ['Itinerary — Places'],
        // continues the statement started above: `summary: 'Update a place',`
        summary: 'Update a place',
        // continues the statement started above: `parameters: [pathParam('id', 'Place identifier')],`
        parameters: [pathParam('id', 'Place identifier')],
        // continues the statement started above: `requestBody: jsonBody('PlaceUpdate'),`
        requestBody: jsonBody('PlaceUpdate'),
        // continues the statement started above: `responses: {`
        responses: {
          // expression: `200: jsonRes('The applied updates', 'Place'),`
          200: jsonRes('The applied updates', 'Place'),
          400: err('name, lat or lng failed validation'),
          // continues the statement started above: `404: err('Place not found'),`
          404: err('Place not found'),
          // continues the statement started above: `...authErrors,`
          ...authErrors,
        // closes the block
        },
      // closes the block
      },
      // continues the statement started above: `delete: deleteOp({`
      delete: deleteOp({
        // expression: `tag: 'Itinerary — Places',`
        tag: 'Itinerary — Places',
        // continues the statement started above: `summary: 'Delete a place',`
        summary: 'Delete a place',
        // continues the statement started above: `notFound: 'Place not found',`
        notFound: 'Place not found',
      // expression: `}),`
      }),
    // closes the block
    },

    // continues the statement started above: `'/counter/{key}': {`
    '/counter/{key}': {
      // opens a block after `get:`
      get: {
        // expression: `tags: ['Counter'],`
        tags: ['Counter'],
        // continues the statement started above: `summary: 'Read a counter',`
        summary: 'Read a counter',
        // continues the statement started above: `description: 'Public read. An unknown key reports a count o…`
        description:
          'Public read. An unknown key reports a count of `0` rather than 404. ' +
          'Key must match `^[a-z][a-z0-9_-]{0,63}$`.',
        // continues the statement started above: `security: [],`
        security: [],
        // continues the statement started above: `parameters: [pathParam('key', 'Counter name, e.g. `trips_cr…`
        parameters: [pathParam('key', 'Counter name, e.g. `trips_created`')],
        // continues the statement started above: `responses: {`
        responses: {
          // expression: `200: jsonRes('Current value', 'Counter'),`
          200: jsonRes('Current value', 'Counter'),
          400: err('Counter key does not match the allowed pattern'),
          // continues the statement started above: `500: err('Unexpected server error'),`
          500: err('Unexpected server error'),
        // closes the block
        },
      // closes the block
      },
    // closes the block
    },
    // continues the statement started above: `'/counter/{key}/increment': {`
    '/counter/{key}/increment': {
      // opens a block after `post:`
      post: {
        // expression: `tags: ['Counter'],`
        tags: ['Counter'],
        // continues the statement started above: `summary: 'Increment a counter',`
        summary: 'Increment a counter',
        // continues the statement started above: `description:`
        description:
          // expression: `'Runs inside a Firestore transaction, so concurrent callers cann…`
          'Runs inside a Firestore transaction, so concurrent callers cannot double-count. ' +
          // continues the statement started above: `'The counter is created at `1` if it does not exist yet.',`
          'The counter is created at `1` if it does not exist yet.',
        // continues the statement started above: `parameters: [pathParam('key', 'Counter name')],`
        parameters: [pathParam('key', 'Counter name')],
        // continues the statement started above: `responses: {`
        responses: {
          // expression: `200: jsonRes('Value after incrementing', 'Counter'),`
          200: jsonRes('Value after incrementing', 'Counter'),
          400: err('Counter key does not match the allowed pattern'),
          // continues the statement started above: `401: err('Missing, invalid or expired Firebase ID token'),`
          401: err('Missing, invalid or expired Firebase ID token'),
          // continues the statement started above: `500: err('Unexpected server error'),`
          500: err('Unexpected server error'),
        // closes the block
        },
      // closes the block
      },
    // closes the block
    },

    // continues the statement started above: `'/config/keys': {`
    '/config/keys': {
      // opens a block after `get:`
      get: {
        // expression: `tags: ['Config'],`
        tags: ['Config'],
        // continues the statement started above: `summary: 'Read the third-party API keys',`
        summary: 'Read the third-party API keys',
        // continues the statement started above: `description:`
        description:
          'These live as Cloud Run environment variables / Secret Manager, never in the ' +
          "app's own BuildConfig. The app caches the response locally so it still works " +
          'offline after the first successful fetch.',
        // continues the statement started above: `responses: {`
        responses: {
          // expression: `200: jsonRes('The current keys', 'Keys'),`
          200: jsonRes('The current keys', 'Keys'),
          // continues the statement started above: `401: err('Missing, invalid or expired Firebase ID token'),`
          401: err('Missing, invalid or expired Firebase ID token'),
          // continues the statement started above: `500: err('Unexpected server error'),`
          500: err('Unexpected server error'),
        // closes the block
        },
      // closes the block
      },
    // closes the block
    },

    // continues the statement started above: `'/config/web-client-id': {`
    '/config/web-client-id': {
      // opens a block after `get:`
      get: {
        // expression: `tags: ['Config'],`
        tags: ['Config'],
        // continues the statement started above: `summary: 'Read the Firebase OAuth web client id',`
        summary: 'Read the Firebase OAuth web client id',
        // continues the statement started above: `description:`
        description:
          "Deliberately unauthenticated and separate from `GET /config/keys` - the app " +
          'needs this value before a user has signed in, to start the Google Sign-In ' +
          "flow in the first place, and it isn't actually a secret per Google's own " +
          'OAuth documentation.',
        // continues the statement started above: `security: [],`
        security: [],
        // continues the statement started above: `responses: {`
        responses: {
          // expression: `200: jsonRes('The current web client id', 'WebClientId'),`
          200: jsonRes('The current web client id', 'WebClientId'),
          // continues the statement started above: `500: err('Unexpected server error'),`
          500: err('Unexpected server error'),
        // closes the block
        },
      // closes the block
      },
    // closes the block
    },

    // continues the statement started above: `'/cache/{path}': {`
    '/cache/{path}': {
      // opens a block after `get:`
      get: {
        // expression: `tags: ['Cache'],`
        tags: ['Cache'],
        // continues the statement started above: `summary: 'Read a cached lookup',`
        summary: 'Read a cached lookup',
        // continues the statement started above: `description:`
        description:
          'Shared across every user - any authenticated caller can read any path. ' +
          '`path` must match `^(weather|currency|places)_[a-zA-Z0-9_]{1,64}$`.',
        // continues the statement started above: `parameters: [pathParam('path', 'Cache key, e.g. `weather_ca…`
        parameters: [pathParam('path', 'Cache key, e.g. `weather_cape_town` or `currency_USD_ZAR`')],
        // continues the statement started above: `responses: {`
        responses: {
          // expression: `200: jsonRes('The cached entry', 'CacheEntry'),`
          200: jsonRes('The cached entry', 'CacheEntry'),
          // continues the statement started above: `400: err('path does not match the allowed pattern'),`
          400: err('path does not match the allowed pattern'),
          404: err('Not cached yet'),
          // continues the statement started above: `...authErrors,`
          ...authErrors,
        // closes the block
        },
      // closes the block
      },
      // opens a block after `put:`
      put: {
        // expression: `tags: ['Cache'],`
        tags: ['Cache'],
        // continues the statement started above: `summary: 'Write a cached lookup',`
        summary: 'Write a cached lookup',
        // continues the statement started above: `description:`
        description:
          'Shared across every user - any authenticated caller can overwrite any path. ' +
          'Stores the request body verbatim, with no field whitelist.',
        // continues the statement started above: `parameters: [pathParam('path', 'Cache key, e.g. `weather_ca…`
        parameters: [pathParam('path', 'Cache key, e.g. `weather_cape_town` or `currency_USD_ZAR`')],
        // continues the statement started above: `requestBody: jsonBody('CacheEntry'),`
        requestBody: jsonBody('CacheEntry'),
        // continues the statement started above: `responses: {`
        responses: {
          // expression: `200: jsonRes('The stored entry', 'CacheEntry'),`
          200: jsonRes('The stored entry', 'CacheEntry'),
          // continues the statement started above: `400: err('path does not match the allowed pattern'),`
          400: err('path does not match the allowed pattern'),
          // continues the statement started above: `...authErrors,`
          ...authErrors,
        // closes the block
        },
      // closes the block
      },
    // closes the block
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
  // closes the block
  },
// closes the initialiser of `openapiSpec`
};

// assigns `module.exports` the value `{ openapiSpec }`
module.exports = { openapiSpec };
