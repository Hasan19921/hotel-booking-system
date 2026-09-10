# Hotel Booking System

## Tech Stack

* Java 17
* Spring Boot
* Maven
* In-memory persistence
* JUnit 5

## How to Build

```
mvn clean test
```

## How to Run

```
mvn spring-boot:run
```

The application starts on `http://localhost:8080`.

## Core Features

* Property onboarding
* Property discovery/search
* Room availability
* Booking
* Payment
* Cancellation/refund

## Design Decisions

**Repository abstraction for persistence**
`OwnerRepository`, `PropertyRepository` and `BookingRepository` are plain interfaces. The only
implementations today are `ConcurrentHashMap`-backed classes that generate IDs with `AtomicLong`.
Services depend on the interfaces, so swapping in a JPA or JDBC implementation later requires no
change to domain logic.

**Strategy pattern for payment processors**
`PaymentProcessor` declares `supportedMethod()` and `process(booking)`. `CardPaymentProcessor`,
`UpiPaymentProcessor` and `WalletPaymentProcessor` implement it. `PaymentService` receives every
processor as a `List` and indexes them by `PaymentMethod`, so adding a new method means adding one
class — `PaymentService` is not modified.

**Filter abstraction for property search**
`PropertyFilter` has a single `matches(property, criteria)` method. `LocationFilter`, `PriceFilter`,
`AmenityFilter` and `StarRatingFilter` implement it and are injected into `SearchService` as a list.
Each filter returns `true` when its criterion is absent, so optional filters are naturally skipped.
A new filter is a new `@Component` and nothing else.

**Strategy abstraction for cancellation policy**
`CancellationService` depends on the `CancellationPolicy` interface, not on the concrete
`StandardCancellationPolicy`. A different refund rule can be introduced without touching the
cancellation flow.

**Booking state lifecycle**

```
CREATED ──pay──> PAYMENT_PENDING ──success──> CONFIRMED ──cancel──> CANCELLED
                        │
                        └──failure──> PAYMENT_FAILED ──retry pay──> PAYMENT_PENDING
```

Payment is only accepted from `CREATED` or `PAYMENT_FAILED`; cancellation is only accepted from
`CONFIRMED`. Invalid transitions raise `InvalidBookingStateException`.

**Date overlap logic for inventory**
`AvailabilityService` is the single place that decides availability. It counts bookings for the same
property and room type whose status occupies inventory and whose dates overlap the request:

```
existingCheckIn.isBefore(requestedCheckOut) && requestedCheckIn.isBefore(existingCheckOut)
```

A room type is available while `overlapping active bookings < roomType.totalRooms`. Both search and
booking creation use this same service, so discovery and booking can never disagree.

## Assumptions

* Check-out date is exclusive; nights = `ChronoUnit.DAYS.between(checkIn, checkOut)`.
* Each booking reserves exactly one room of the selected `RoomType`.
* `CREATED`, `PAYMENT_PENDING` and `CONFIRMED` occupy inventory.
* `CANCELLED` and `PAYMENT_FAILED` do not occupy inventory, so cancelling a booking releases the
  room automatically — no separate inventory table is written back.
* Payment processors are mocked; no third-party gateway is contacted.
* No authentication is required; owner and booking identity come from the request.
* Persistence is intentionally in-memory, so all data is lost on restart.
* Refund is calculated and returned in the response only; no refund transaction is stored.

## API list

| Method | Path | Purpose |
| --- | --- | --- |
| POST | `/owners` | Create an owner |
| POST | `/owners/{ownerId}/properties` | Onboard a property under an owner |
| GET | `/properties/search` | Search available properties |
| POST | `/bookings` | Create a booking |
| POST | `/bookings/{bookingId}/payments` | Pay for a booking |
| POST | `/bookings/{bookingId}/cancel` | Cancel a booking and get the refund amount |

### POST /owners

```json
{ "name": "Rupeek Stays" }
```

### POST /owners/{ownerId}/properties

```json
{
  "name": "Rupeek Grand",
  "city": "Bangalore",
  "locality": "Indiranagar",
  "starRating": 4,
  "amenities": ["WIFI", "POOL"],
  "roomTypes": [
    { "name": "Deluxe", "capacity": 2, "pricePerNight": 2500, "totalRooms": 5 }
  ]
}
```

### GET /properties/search

Required: `city`, `checkIn`, `checkOut`, `guests`.
Optional: `minPrice`, `maxPrice`, `amenities`, `starRating`.

```
GET /properties/search?city=Bangalore&checkIn=2026-10-01&checkOut=2026-10-03&guests=2
    &minPrice=1000&maxPrice=5000&amenities=WIFI,POOL&starRating=3
```

### POST /bookings

```json
{
  "propertyId": 1,
  "roomTypeId": 1,
  "checkIn": "2026-10-01",
  "checkOut": "2026-10-03",
  "guests": 2
}
```

### POST /bookings/{bookingId}/payments

```json
{ "paymentMethod": "UPI" }
```

Accepted values: `CARD`, `UPI`, `WALLET`.

### Error responses

| Status | Cause |
| --- | --- |
| 400 | Invalid booking input, unsupported payment method |
| 404 | Owner, property, room type or booking not found |
| 409 | No rooms available, or illegal booking state transition |

Body: `{ "error": "<message>" }`

## What I Would Add With More Time

These are **not implemented**:

* Concurrency handling for simultaneous bookings on the last available room
* Pluggable dynamic pricing
* Payment idempotency
* OpenAPI/Swagger documentation
