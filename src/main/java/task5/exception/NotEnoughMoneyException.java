package task5.exception;

public class NotEnoughMoneyException extends Exception {

    public NotEnoughMoneyException(String currency) {
        super("Not enough money on your " + currency + " wallet balance");
    }
}
