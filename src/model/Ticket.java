package model;

import utils.Enums.ZoneType;

public class Ticket extends Product {

    private final int trips; // number of trips for this ticket (1, 2, 10)

    // Constructor
    public Ticket(ZoneType zone, int trips, boolean printed) {
        super(zone, printed);
        this.trips = trips;
    }

    public int getTrips() {
        return trips;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "zone=" + zone +
                ", valid=" + valid +
                ", trips=" + trips +
                ", isPrinted=" + isPrinted +
                '}';
    }
}