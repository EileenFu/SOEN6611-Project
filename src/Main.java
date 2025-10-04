import model.*;
import services.*;
import services.FareService;

public class Main {
    public static void main(String[] args) {
        FareService fareService = new FareService();
        TransactionLogger txnLogger = new TransactionLogger();

        TVM tvm = new TVM(fareService, txnLogger);
    }
}