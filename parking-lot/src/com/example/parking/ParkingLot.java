package com.example.parking;

import java.time.LocalDateTime;
import java.util.*;

public class ParkingLot {

    private final String name;
    private final List<ParkingFloor> floors;
    private final Map<String, ParkingTicket> activeTickets;
    private final Map<SlotType, Double> rates;
    private final int entryGateCount;

    public ParkingLot(String name, List<ParkingFloor> floors,
                      Map<SlotType, Double> rates, int entryGateCount) {
        this.name           = name;
        this.floors         = floors;
        this.rates          = rates;
        this.entryGateCount = entryGateCount;
        this.activeTickets  = new HashMap<>();
    }

    // ---- API 1: park ----
    public ParkingTicket park(Vehicle vehicle, LocalDateTime entryTime,
                              SlotType requestedSlotType, int entryGateId) {

        List<SlotType> compatible = getCompatibleSlotTypes(vehicle.getType());

        // Ensure requested type is compatible; if not, reject
        if (!compatible.contains(requestedSlotType)) {
            System.out.println("[ParkingLot] " + vehicle.getType()
                    + " cannot fit in " + requestedSlotType + " slot.");
            return null;
        }

        // Try requested type first, then upgrade to larger slots
        int startIdx = compatible.indexOf(requestedSlotType);
        for (int i = startIdx; i < compatible.size(); i++) {
            SlotType tryType = compatible.get(i);
            ParkingSlot nearest = findNearestSlot(tryType, entryGateId);
            if (nearest != null) {
                nearest.occupy();
                ParkingTicket ticket = new ParkingTicket(vehicle, nearest, entryTime);
                activeTickets.put(ticket.getTicketId(), ticket);

                System.out.println("[ParkingLot] Vehicle parked successfully.");
                ticket.print();
                return ticket;
            }
        }

        System.out.println("[ParkingLot] No compatible slot available for "
                + vehicle + " from gate " + entryGateId + ".");
        return null;
    }

    // ---- API 2: status ----
    public void status() {
        System.out.println("\n+===== Parking Lot Status: " + name + " =====+");
        System.out.printf("| %-8s | %-10s | %-10s | %-10s |%n",
                "Floor", "Small", "Medium", "Large");
        System.out.println("|----------|------------|------------|------------|");

        int totalSmall = 0, totalMedium = 0, totalLarge = 0;
        int availSmall = 0, availMedium = 0, availLarge = 0;

        for (ParkingFloor floor : floors) {
            int sA = floor.countAvailable(SlotType.SMALL);
            int sT = floor.countTotal(SlotType.SMALL);
            int mA = floor.countAvailable(SlotType.MEDIUM);
            int mT = floor.countTotal(SlotType.MEDIUM);
            int lA = floor.countAvailable(SlotType.LARGE);
            int lT = floor.countTotal(SlotType.LARGE);

            System.out.printf("| Floor %-2d | %3d / %-3d  | %3d / %-3d  | %3d / %-3d  |%n",
                    floor.getFloorNumber(), sA, sT, mA, mT, lA, lT);

            totalSmall  += sT; availSmall  += sA;
            totalMedium += mT; availMedium += mA;
            totalLarge  += lT; availLarge  += lA;
        }

        System.out.println("|----------|------------|------------|------------|");
        System.out.printf("| %-8s | %3d / %-3d  | %3d / %-3d  | %3d / %-3d  |%n",
                "TOTAL", availSmall, totalSmall, availMedium, totalMedium,
                availLarge, totalLarge);
        System.out.println("+===============================================+");
        System.out.println("  (available / total)\n");
    }

    // ---- API 3: exit ----
    public Bill exit(ParkingTicket ticket, LocalDateTime exitTime) {
        if (!activeTickets.containsKey(ticket.getTicketId())) {
            System.out.println("[ParkingLot] Ticket " + ticket.getTicketId()
                    + " not found or already exited.");
            return null;
        }

        ticket.getSlot().vacate();
        activeTickets.remove(ticket.getTicketId());

        double hourlyRate = rates.get(ticket.getSlot().getSlotType());
        Bill bill = new Bill(ticket, exitTime, hourlyRate);

        System.out.println("[ParkingLot] Vehicle exited.");
        bill.print();
        return bill;
    }

    // ---- helpers ----

    private ParkingSlot findNearestSlot(SlotType slotType, int entryGateId) {
        ParkingSlot nearest = null;
        int minDist = Integer.MAX_VALUE;

        for (ParkingFloor floor : floors) {
            for (ParkingSlot slot : floor.getAvailableSlots(slotType)) {
                int dist = slot.getDistanceFromGate(entryGateId);
                if (dist < minDist) {
                    minDist = dist;
                    nearest = slot;
                }
            }
        }
        return nearest;
    }

    private List<SlotType> getCompatibleSlotTypes(VehicleType vehicleType) {
        List<SlotType> types = new ArrayList<>();
        switch (vehicleType) {
            case TWO_WHEELER:
                types.add(SlotType.SMALL);
                types.add(SlotType.MEDIUM);
                types.add(SlotType.LARGE);
                break;
            case CAR:
                types.add(SlotType.MEDIUM);
                types.add(SlotType.LARGE);
                break;
            case BUS:
                types.add(SlotType.LARGE);
                break;
        }
        return types;
    }
}
