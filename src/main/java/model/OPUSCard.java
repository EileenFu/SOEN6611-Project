package model;

import services.FareService;
import utils.Enums.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OPUSCard {

    public enum CardType { REGULAR, REDUCED_FARE }

    private final String cardId;
    private final CardType cardType;

    private final List<Ticket> tickets;
    private final List<Pass> passes;

    private final FareService fareService;

    // Constructor
    public OPUSCard(CardType cardType, FareService fareService) {
        this.cardId = UUID.randomUUID().toString();
        this.cardType = cardType;
        this.tickets = new ArrayList<>();
        this.passes = new ArrayList<>();
        this.fareService = fareService;
    }

    // Getters
    public String getCardId() { return cardId; }
    public CardType getCardType() { return cardType; }
    public List<Ticket> getTickets() { return new ArrayList<>(tickets); }
    public List<Pass> getPasses() { return new ArrayList<>(passes); }


    // Ticket loading
    public void loadTicket(Ticket ticket) {
        boolean reducedFare = (cardType == CardType.REDUCED_FARE);
        double price = fareService.getTicketFare(ticket.getZone(), ticket.getTrips(), reducedFare);
        tickets.add(ticket);
    }

    // Pass loading
    public void loadPass(Pass pass) {
        boolean reducedFare = (cardType == CardType.REDUCED_FARE);
        double price = fareService.getPassFare(pass.getZone(), pass.getDuration(), reducedFare);
        passes.add(pass);
    }

    @Override
    public String toString() {
        return "OPUSCard{" +
                "cardId='" + cardId + '\'' +
                ", cardType=" + cardType +
                ", tickets=" + tickets.size() +
                ", passes=" + passes.size() +
                '}';
    }
}