package services;

// ----------------------
// AI GENERATED PLACEHOLDER
// ----------------------
public class TokenizationService {
    public String tokenize(String cardNumber) {
        if (cardNumber.equals("4111222233334444")) {
            // A fixed token for the default card
            return "TOKEN-1A2B-3C4D-5E6F-DEFAULT";
        }
        return "TOKENIZED(" + cardNumber + ")";

    }

    public String detokenize(String token) {
        return token.replace("TOKENIZED(", "").replace(")", "");
    }
}
// ----------------------
// AI GENERATED PLACEHOLDER
// ----------------------