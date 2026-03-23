package com.example.parking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

public class Bill {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ParkingTicket ticket;
    private final LocalDateTime exitTime;
    private final long durationHours;
    private final double amount;

    public Bill(ParkingTicket ticket, LocalDateTime exitTime, double hourlyRate) {
        this.ticket   = ticket;
        this.exitTime = exitTime;

        long minutes = Duration.between(ticket.getEntryTime(), exitTime).toMinutes();
        this.durationHours = (long) Math.ceil(minutes / 60.0);
        this.amount = durationHours * hourlyRate;
    }

    public double getAmount()       { return amount; }
    public long   getDurationHours(){ return durationHours; }

    public void print() {
        System.out.println("+--------- Bill ------------+");
        System.out.println("| Ticket   : " + ticket.getTicketId());
        System.out.println("| Vehicle  : " + ticket.getVehicle());
        System.out.println("| Slot     : " + ticket.getSlot());
        System.out.println("| Entry    : " + ticket.getEntryTime().format(FMT));
        System.out.println("| Exit     : " + exitTime.format(FMT));
        System.out.println("| Duration : " + durationHours + " hour(s)");
        System.out.println("| Rate     : " + ticket.getSlot().getSlotType() + " slot rate");
        System.out.println("| TOTAL    : Rs. " + String.format("%.2f", amount));
        System.out.println("+----------------------------+");
    }
}
