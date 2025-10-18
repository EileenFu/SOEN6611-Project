package services;

import utils.Enums.*;

import java.util.HashMap;
import java.util.Map;

public class FareService {

    // Inner class for fares
    public static class Fare {
        private final double regular;
        private final double reduced;

        public Fare(double regular, double reduced) {
            this.regular = regular;
            this.reduced = reduced;
        }

        public double getFare(boolean reducedFare) {
            return reducedFare ? reduced : regular;
        }
    }

    // Ticket fares keyed by "ZoneType + trips"
    private final Map<String, Fare> ticketFares = new HashMap<>();
    // Pass fares keyed by "ZoneType + PassDuration"
    private final Map<String, Fare> passFares = new HashMap<>();

    // Constructor
    // Constructor
    public FareService() {
        // Ticket fares
        ticketFares.put("A_1", new Fare(3.75, 2.75));
        ticketFares.put("A_2", new Fare(7.00, 5.00));
        ticketFares.put("A_10", new Fare(34.25, 23.00));

        ticketFares.put("AB_1", new Fare(5.00, 3.50));
        ticketFares.put("AB_2", new Fare(9.75, 6.50));
        ticketFares.put("AB_10", new Fare(47.50, 32.25));

        ticketFares.put("ABC_1", new Fare(7.00, 4.75));
        ticketFares.put("ABC_2", new Fare(13.50, 9.00));
        ticketFares.put("ABC_10", new Fare(60.25, 40.25));

        ticketFares.put("ABCD_1", new Fare(9.50, 6.75));
        ticketFares.put("ABCD_2", new Fare(18.50, 13.00));
        ticketFares.put("ABCD_10", new Fare(82.75, 55.50));

        // Special case: 6-13 Group Ticket
        ticketFares.put("A_GROUP", new Fare(21.25, 21.25));

        // Pass fares - Zone A (all durations)
        passFares.put("A_DAY", new Fare(11.25, 11.25));
        passFares.put("A_THREEDAY", new Fare(21.75, 21.75));
        passFares.put("A_WEEK", new Fare(32.00, 19.25));
        passFares.put("A_MONTH", new Fare(104.50, 62.75));
        passFares.put("A_NIGHT", new Fare(6.50, 6.50));
        passFares.put("A_WEEKEND", new Fare(16.75, 16.75));
        passFares.put("A_FOURMONTH", new Fare(240.00, 240.00));

        // Pass fares - Zone AB
        passFares.put("AB_DAY", new Fare(13.75, 13.75));
        passFares.put("AB_THREEDAY", new Fare(29.25, 29.25));
        passFares.put("AB_WEEK", new Fare(38.00, 22.75));
        passFares.put("AB_MONTH", new Fare(164.50, 98.75));
        passFares.put("AB_NIGHT", new Fare(6.50, 6.50));
        passFares.put("AB_WEEKEND", new Fare(19.75, 19.75));
        passFares.put("AB_FOURMONTH", new Fare(300.00, 300.00));

        // Pass fares - Zone ABC
        passFares.put("ABC_DAY", new Fare(17.75, 17.75));
        passFares.put("ABC_THREEDAY", new Fare(41.25, 41.25));
        passFares.put("ABC_WEEK", new Fare(46.00, 27.50));
        passFares.put("ABC_MONTH", new Fare(200.50, 120.25));
        passFares.put("ABC_NIGHT", new Fare(6.50, 6.50));
        passFares.put("ABC_WEEKEND", new Fare(23.75, 23.75));
        passFares.put("ABC_FOURMONTH", new Fare(375.00, 375.00));

        // Pass fares - Zone ABCD
        passFares.put("ABCD_DAY", new Fare(22.75, 22.75));
        passFares.put("ABCD_THREEDAY", new Fare(56.25, 56.25));
        passFares.put("ABCD_WEEK", new Fare(56.00, 33.50));
        passFares.put("ABCD_MONTH", new Fare(275.50, 165.25));
        passFares.put("ABCD_NIGHT", new Fare(6.50, 6.50));
        passFares.put("ABCD_WEEKEND", new Fare(29.75, 29.75));
        passFares.put("ABCD_FOURMONTH", new Fare(480.00, 480.00));
    }

    // Get ticket fare for zone + trips
    public double getTicketFare(ZoneType zone, int trips, boolean reducedFare) {
        String key = zone.name() + "_" + trips;
        Fare fare = ticketFares.get(key);
        if (fare == null) return 0.0;
        return fare.getFare(reducedFare);
    }

    // Get pass fare for zone + duration
    public double getPassFare(ZoneType zone, PassDuration duration, boolean reducedFare) {
        String key = zone.name() + "_" + duration.name();
        Fare fare = passFares.get(key);
        if (fare == null) return 0.0;
        return fare.getFare(reducedFare);
    }
}