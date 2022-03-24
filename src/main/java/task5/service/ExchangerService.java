package task5.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import task5.exception.NotEnoughMoneyException;
import task5.exception.ObjectNotFoundException;
import task5.model.Currency;
import task5.model.Exchange;
import task5.model.Wallet;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExchangerService {
    private final AccountService accountService;
    private final ExchangeRateService exchangeRateService;
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    private final Map<UUID, Exchange> queue = new ConcurrentHashMap<>();

    public ExchangerService(AccountService accountService, ExchangeRateService exchangeRateService) {
        this.accountService = accountService;
        this.exchangeRateService = exchangeRateService;
    }

    public void exchange(Long accountId, Currency from, Currency to, BigDecimal value) {

        Exchange exchange = new Exchange(accountId, from, to, value);
        UUID uniqueKey = UUID.randomUUID();
        queue.put(uniqueKey, exchange);
        run();
    }

    public synchronized void run() {
            for (Map.Entry<UUID, Exchange> uuidExchangeEntry : queue.entrySet()) {

                UUID uniqueKey = uuidExchangeEntry.getKey();
                Long accountId = uuidExchangeEntry.getValue().getAccountId();
                Currency from = uuidExchangeEntry.getValue().getFrom();
                Currency to = uuidExchangeEntry.getValue().getTo();
                BigDecimal value = uuidExchangeEntry.getValue().getValue();

                try {

                    System.out.println(
                            "Exchange with number: " + uniqueKey + " with params: " + accountId + "|" + from + "|" +
                                    to +
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
                    System.out.println("Exchange with number: " + uniqueKey + " successfully completed");
                } catch (ObjectNotFoundException | NotEnoughMoneyException e) {
                    log.error(e.getMessage());
                    System.out.println("Exchange with number: " + uniqueKey + " completed unsuccessfully");
                } finally {
                    queue.remove(uniqueKey);
                }
            }
    }
}
