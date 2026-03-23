package com.example.parking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParkingTicket {

    private static int counter = 0;
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSlot slot;
    private final LocalDateTime entryTime;

    public ParkingTicket(Vehicle vehicle, ParkingSlot slot, LocalDateTime entryTime) {
        this.ticketId  = "TKT-" + (++counter);
        this.vehicle   = vehicle;
        this.slot      = slot;
        this.entryTime = entryTime;
    }

    public String        getTicketId()  { return ticketId; }
    public Vehicle       getVehicle()   { return vehicle; }
    public ParkingSlot   getSlot()      { return slot; }
    public LocalDateTime getEntryTime() { return entryTime; }

    public void print() {
        System.out.println("+------ Parking Ticket ------+");
        System.out.println("| Ticket  : " + ticketId);
        System.out.println("| Vehicle : " + vehicle);
        System.out.println("| Slot    : " + slot);
        System.out.println("| Entry   : " + entryTime.format(FMT));
        System.out.println("+----------------------------+");
    }
}
