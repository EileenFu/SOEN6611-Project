package model;

import services.FareService;
import services.TransactionLogger;
import utils.Enums.*;

public class TVM {

    private final FareService fareService;
    private final TransactionLogger txnLogger;
    private OPUSCard currentCard; // Simulate NFC card being tapped

    public TVM(FareService fareService, TransactionLogger txnLogger) {
        this.fareService = fareService;
        this.txnLogger = txnLogger;
        this.currentCard = null;
    }

    // ----------------------
    // AI GENERATED PLACEHOLDER
    // ----------------------
    // Simulate tapping an OPUS card on the NFC reader
    public void tapCard(OPUSCard card) {
        this.currentCard = card;
        System.out.println("OPUS card detected: " + card.getCardType());
    }

    // Remove OPUS card from NFC
    public void removeCard() {
        this.currentCard = null;
        System.out.println("No OPUS card detected. TVM will print tickets/passes.");
    }
    // ----------------------
    // AI GENERATED PLACEHOLDER
    // ----------------------

    // Buy ticket
    public void buyTicket(ZoneType zone, int trips, PaymentMethod paymentMethod) {
        // Group tickets must always be printed
        boolean isGroupTicket = (zone == ZoneType.A_GROUP);

        if (currentCard != null && !isGroupTicket) {
            boolean reducedFare = currentCard.getCardType() == OPUSCard.CardType.REDUCED_FARE;
            double price = fareService.getTicketFare(zone, trips, reducedFare);
            Ticket ticket = new Ticket(zone, trips, false);
            currentCard.loadTicket(ticket);
            Transaction txn = new Transaction(price, paymentMethod, ticket);
            txnLogger.logTransaction(txn);

            // PLACEHOLDER
            // Output should be to UI instead
            System.out.println("Loaded " + trips + "-trip ticket onto OPUS card for $" + price);

        } else {
            // Printed ticket, regular fare only
            double price = fareService.getTicketFare(zone, trips, false);
            Ticket ticket = new Ticket(zone, trips, true);
            Transaction txn = new Transaction(price, paymentMethod, ticket);
            txnLogger.logTransaction(txn);

            // PLACEHOLDER
            // Output should be to UI instead
            System.out.println("Printed " + trips + "-trip ticket for $" + price);
        }
    }

    // Buy pass
    public void buyPass(ZoneType zone, PassDuration duration, PaymentMethod paymentMethod) {
        if (currentCard != null) {
            boolean reducedFare = currentCard.getCardType() == OPUSCard.CardType.REDUCED_FARE;
            double price = fareService.getPassFare(zone, duration, reducedFare);
            Pass pass = new Pass(zone, duration, false);
            currentCard.loadPass(pass);
            Transaction txn = new Transaction(price, paymentMethod, pass);
            txnLogger.logTransaction(txn);

            // PLACEHOLDER
            // Output should be to UI instead
            System.out.println("Loaded " + duration + " pass onto OPUS card for $" + price);

        } else {
            double price = fareService.getPassFare(zone, duration, false);
            Pass pass = new Pass(zone, duration, true);
            Transaction txn = new Transaction(price, paymentMethod, pass);
            txnLogger.logTransaction(txn);

            // PLACEHOLDER
            // Output should be to UI instead
            System.out.println("Printed " + duration + " pass for $" + price);
        }
    }
}