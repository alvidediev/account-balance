package by.dediev.account_balance.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super("Account not found " + message);
    }
}
