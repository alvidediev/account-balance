package by.dediev.account_balance.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super("Insufficient funds for withdrawal from account: " + message);
    }
}
