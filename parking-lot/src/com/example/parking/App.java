package com.example.parking;

import java.time.LocalDateTime;
import java.util.*;

public class App {

    public static void main(String[] args) {

        System.out.println("=== Multilevel Parking Lot Demo ===\n");

        // ---- 1. Setup rates ----
        Map<SlotType, Double> rates = new EnumMap<>(SlotType.class);
        rates.put(SlotType.SMALL,  10.0);   // Rs.10/hr for 2-wheelers
        rates.put(SlotType.MEDIUM, 20.0);   // Rs.20/hr for cars
        rates.put(SlotType.LARGE,  40.0);   // Rs.40/hr for buses

        // ---- 2. Build floors with slots ----
        //  2 floors, 3 entry gates
        //  Floor 1: 3 small, 3 medium, 2 large
        //  Floor 2: 2 small, 2 medium, 2 large

        int entryGates = 3;

        ParkingFloor floor1 = new ParkingFloor(1);
        floor1.addSlot(makeSlot("F1-S1", SlotType.SMALL,  1, new int[]{1, 5, 8}));
        floor1.addSlot(makeSlot("F1-S2", SlotType.SMALL,  1, new int[]{3, 2, 6}));
        floor1.addSlot(makeSlot("F1-S3", SlotType.SMALL,  1, new int[]{6, 4, 3}));
        floor1.addSlot(makeSlot("F1-M1", SlotType.MEDIUM, 1, new int[]{2, 3, 7}));
        floor1.addSlot(makeSlot("F1-M2", SlotType.MEDIUM, 1, new int[]{4, 1, 5}));
        floor1.addSlot(makeSlot("F1-M3", SlotType.MEDIUM, 1, new int[]{7, 6, 2}));
        floor1.addSlot(makeSlot("F1-L1", SlotType.LARGE,  1, new int[]{5, 7, 4}));
        floor1.addSlot(makeSlot("F1-L2", SlotType.LARGE,  1, new int[]{8, 8, 1}));

        ParkingFloor floor2 = new ParkingFloor(2);
        floor2.addSlot(makeSlot("F2-S1", SlotType.SMALL,  2, new int[]{4, 6, 9}));
        floor2.addSlot(makeSlot("F2-S2", SlotType.SMALL,  2, new int[]{7, 3, 5}));
        floor2.addSlot(makeSlot("F2-M1", SlotType.MEDIUM, 2, new int[]{5, 4, 8}));
        floor2.addSlot(makeSlot("F2-M2", SlotType.MEDIUM, 2, new int[]{6, 2, 3}));
        floor2.addSlot(makeSlot("F2-L1", SlotType.LARGE,  2, new int[]{3, 5, 7}));
        floor2.addSlot(makeSlot("F2-L2", SlotType.LARGE,  2, new int[]{9, 9, 2}));

        List<ParkingFloor> floors = Arrays.asList(floor1, floor2);

        ParkingLot lot = new ParkingLot("CampusParking", floors, rates, entryGates);

        // ---- 3. Show initial status ----
        lot.status();

        // ---- 4. Park vehicles ----
        LocalDateTime now = LocalDateTime.of(2026, 3, 23, 9, 0);

        System.out.println("--- Parking a bike from Gate 1 (requesting SMALL slot) ---");
        Vehicle bike1 = new Vehicle("KA-01-1234", VehicleType.TWO_WHEELER);
        ParkingTicket t1 = lot.park(bike1, now, SlotType.SMALL, 1);

        System.out.println("\n--- Parking a car from Gate 2 (requesting MEDIUM slot) ---");
        Vehicle car1 = new Vehicle("MH-02-5678", VehicleType.CAR);
        ParkingTicket t2 = lot.park(car1, now.plusMinutes(10), SlotType.MEDIUM, 2);

        System.out.println("\n--- Parking a bus from Gate 3 (requesting LARGE slot) ---");
        Vehicle bus1 = new Vehicle("DL-03-9999", VehicleType.BUS);
        ParkingTicket t3 = lot.park(bus1, now.plusMinutes(20), SlotType.LARGE, 3);

        System.out.println("\n--- Parking a bike from Gate 2 (requesting SMALL slot) ---");
        Vehicle bike2 = new Vehicle("KA-04-4321", VehicleType.TWO_WHEELER);
        ParkingTicket t4 = lot.park(bike2, now.plusMinutes(30), SlotType.SMALL, 2);

        System.out.println("\n--- Parking a car from Gate 3 (requesting MEDIUM slot) ---");
        Vehicle car2 = new Vehicle("TN-05-8888", VehicleType.CAR);
        ParkingTicket t5 = lot.park(car2, now.plusMinutes(40), SlotType.MEDIUM, 3);

        // ---- 5. Status after parking ----
        lot.status();

        // ---- 6. Exit vehicles ----
        System.out.println("--- Bike exits after 3 hours ---");
        lot.exit(t1, now.plusHours(3));

        System.out.println("\n--- Car exits after 1.5 hours (billed as 2 hours) ---");
        lot.exit(t2, now.plusMinutes(100));

        System.out.println("\n--- Bus exits after 5 hours ---");
        lot.exit(t3, now.plusHours(5));

        // ---- 7. Status after some exits ----
        lot.status();

        // ---- 8. Demonstrate slot upgrade: bike parks in MEDIUM when SMALL full ----
        System.out.println("--- Fill all SMALL slots, then park a bike (should upgrade) ---");
        // Park bikes in remaining small slots
        lot.park(new Vehicle("KA-06-1111", VehicleType.TWO_WHEELER),
                 now.plusHours(6), SlotType.SMALL, 1);
        lot.park(new Vehicle("KA-07-2222", VehicleType.TWO_WHEELER),
                 now.plusHours(6), SlotType.SMALL, 1);
        lot.park(new Vehicle("KA-08-3333", VehicleType.TWO_WHEELER),
                 now.plusHours(6), SlotType.SMALL, 1);
        lot.park(new Vehicle("KA-09-4444", VehicleType.TWO_WHEELER),
                 now.plusHours(6), SlotType.SMALL, 1);

        System.out.println("\n--- All SMALL slots full. Parking another bike (should get MEDIUM) ---");
        ParkingTicket t9 = lot.park(new Vehicle("KA-10-5555", VehicleType.TWO_WHEELER),
                now.plusHours(6), SlotType.SMALL, 1);

        if (t9 != null) {
            System.out.println("  -> Bike got slot type: " + t9.getSlot().getSlotType()
                    + " (billed at " + t9.getSlot().getSlotType() + " rate)");
        }

        // ---- 9. Invalid: bus in SMALL slot ----
        System.out.println("\n--- Invalid: try parking a bus in SMALL slot ---");
        lot.park(new Vehicle("DL-11-0000", VehicleType.BUS),
                 now.plusHours(7), SlotType.SMALL, 1);

        // ---- 10. Final status ----
        lot.status();
    }

    private static ParkingSlot makeSlot(String id, SlotType type, int floor,
                                         int[] distances) {
        Map<Integer, Integer> distMap = new HashMap<>();
        for (int g = 0; g < distances.length; g++) {
            distMap.put(g + 1, distances[g]);
        }
        return new ParkingSlot(id, type, floor, distMap);
    }
}
