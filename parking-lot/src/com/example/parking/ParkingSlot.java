package com.example.parking;

import java.util.Map;

public class ParkingSlot {

    private final String slotId;
    private final SlotType slotType;
    private final int floorNumber;
    private final Map<Integer, Integer> distanceFromGate;
    private boolean occupied;

    public ParkingSlot(String slotId, SlotType slotType, int floorNumber,
                       Map<Integer, Integer> distanceFromGate) {
        this.slotId           = slotId;
        this.slotType         = slotType;
        this.floorNumber      = floorNumber;
        this.distanceFromGate = distanceFromGate;
        this.occupied         = false;
    }

    public void occupy() { this.occupied = true; }
    public void vacate() { this.occupied = false; }

    public boolean isOccupied()    { return occupied; }
    public String  getSlotId()     { return slotId; }
    public SlotType getSlotType()  { return slotType; }
    public int     getFloorNumber(){ return floorNumber; }

    public int getDistanceFromGate(int gateId) {
        return distanceFromGate.getOrDefault(gateId, Integer.MAX_VALUE);
    }

    @Override
    public String toString() {
        return slotId + " (" + slotType + ", Floor " + floorNumber + ")";
    }
}
