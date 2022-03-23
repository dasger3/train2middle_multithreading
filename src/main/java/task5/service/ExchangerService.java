package task5.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import task5.exception.NotEnoughMoneyException;
import task5.exception.ObjectNotFoundException;
import task5.model.Currency;
import task5.model.Wallet;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExchangerService {
    private final AccountService accountService;
    private final ExchangeRateService exchangeRateService;
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    public synchronized void exchange(Long accountId, Currency from, Currency to, BigDecimal value) {
        UUID uniqueKey = UUID.randomUUID();
        try {
            System.out.println(
                    "Exchange with number: " + uniqueKey + "with params: " + accountId + "|" + from + "|" + to +
                            "|" + value);
            Wallet wallet = accountService.findAccountById(accountId).getWallet();

            BigDecimal rate = exchangeRateService.getExchangeRate(from, to);

            BigDecimal newBalance = wallet.getBalance().get(from).subtract(value);

            if (newBalance.doubleValue() < 0) {
                throw new NotEnoughMoneyException(from.getCurrency());
            }

            wallet.getBalance().put(from, newBalance);
            wallet.getBalance().put(to, wallet.getBalance().get(to).add(value.multiply(rate)));

            accountService.updateWalletById(wallet, accountId);
            System.out.println("Transaction with number: " + uniqueKey + " successfully completed");
        } catch (ObjectNotFoundException | NotEnoughMoneyException e) {
            log.error(e.getMessage());
            System.out.println("Transaction with number: " + uniqueKey + " completed unsuccessfully");
        }

    }
}
