Multilevel Parking Lot — LLD Implementation
--------------------------------------------

## Narrative

A multilevel parking lot system that supports:
- Three slot types: **Small** (2-wheelers), **Medium** (cars), **Large** (buses)
- Different hourly rates per slot type
- Multiple entry gates with **nearest-slot assignment**
- Smaller vehicles can park in larger slots (bike in medium/large, car in large)
- Billing based on **allocated slot type**, not vehicle type
- Ticket generation on entry, bill generation on exit

## Class Diagram (UML — text)

```
+------------------+       +------------------+
| <<enum>>         |       | <<enum>>         |
| VehicleType      |       | SlotType         |
+------------------+       +------------------+
| TWO_WHEELER      |       | SMALL            |
| CAR              |       | MEDIUM           |
| BUS              |       | LARGE            |
+------------------+       +------------------+

+---------------------------+
|         Vehicle           |
+---------------------------+
| - licensePlate: String    |
| - type: VehicleType       |
+---------------------------+
| + getLicensePlate(): String|
| + getType(): VehicleType  |
+---------------------------+

+-------------------------------+
|        ParkingSlot            |
+-------------------------------+
| - slotId: String              |
| - slotType: SlotType          |
| - floorNumber: int            |
| - distanceFromGate: Map<Int,Int>|
| - occupied: boolean           |
+-------------------------------+
| + occupy(): void              |
| + vacate(): void              |
| + isOccupied(): boolean       |
| + getDistanceFromGate(int):int|
+-------------------------------+

+-------------------------------+
|       ParkingTicket           |
+-------------------------------+
| - ticketId: String            |
| - vehicle: Vehicle            |
| - slot: ParkingSlot           |
| - entryTime: LocalDateTime    |
+-------------------------------+
| + getTicketId(): String       |
| + getVehicle(): Vehicle       |
| + getSlot(): ParkingSlot      |
| + getEntryTime(): LocalDateTime|
+-------------------------------+

+-------------------------------+
|           Bill                |
+-------------------------------+
| - ticket: ParkingTicket       |
| - exitTime: LocalDateTime     |
| - durationHours: long         |
| - amount: double              |
+-------------------------------+
| + getAmount(): double         |
| + print(): void               |
+-------------------------------+

+-------------------------------+
|       ParkingFloor            |
+-------------------------------+
| - floorNumber: int            |
| - slots: List<ParkingSlot>    |
+-------------------------------+
| + getAvailableSlots(SlotType) |
| + countAvailable(SlotType):int|
+-------------------------------+

+----------------------------------------+
|            ParkingLot                  |
+----------------------------------------+
| - floors: List<ParkingFloor>           |
| - activeTickets: Map<String, Ticket>   |
| - rates: Map<SlotType, Double>         |
| - entryGateCount: int                  |
+----------------------------------------+
| + park(Vehicle, LocalDateTime,         |
|        SlotType, int): ParkingTicket   |
| + exit(ParkingTicket,                  |
|        LocalDateTime): Bill            |
| + status(): void                       |
| - findNearestSlot(SlotType, int)       |
|        : ParkingSlot                   |
| - getCompatibleSlotTypes(VehicleType)  |
|        : List<SlotType>                |
+----------------------------------------+

+------------------+
|      App         |
+------------------+
| + main(): void   |
+------------------+

Relationships:
  ParkingLot  ───>  ParkingFloor   (has-many)
  ParkingFloor───>  ParkingSlot    (has-many)
  ParkingLot  ───>  ParkingTicket  (tracks active)
  ParkingTicket───> Vehicle        (has-a)
  ParkingTicket───> ParkingSlot    (has-a)
  Bill        ───>  ParkingTicket  (has-a)
```

## Design Decisions

1. **Nearest-slot assignment**: Each slot stores distance-from-gate for every gate.
   `park()` picks the closest available compatible slot to the given entry gate.

2. **Slot compatibility**: A 2-wheeler fits SMALL/MEDIUM/LARGE, a car fits MEDIUM/LARGE,
   a bus fits only LARGE. The system tries the requested slot type first, then
   upgrades to larger slots if needed.

3. **Billing by slot type**: Rates are per slot type. If a bike parks in a MEDIUM
   slot, it pays the MEDIUM rate — not the SMALL rate.

4. **Ceiling-based billing**: Any partial hour is billed as a full hour.

## Build & Run

```bash
cd parking-lot/src
javac com/example/parking/*.java
java com.example.parking.App
```
