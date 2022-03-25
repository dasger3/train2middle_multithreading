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
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class ExchangerService {
    private final AccountService accountService;
    private final ExchangeRateService exchangeRateService;
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    private final ConcurrentLinkedQueue<Exchange> queue = new ConcurrentLinkedQueue<>();

    public ExchangerService(AccountService accountService, ExchangeRateService exchangeRateService) {
        this.accountService = accountService;
        this.exchangeRateService = exchangeRateService;
    }

    public void exchange(Long accountId, Currency from, Currency to, BigDecimal value) {
        String uniqueKey = "" + accountId + from + to + "|" + value  + "|" + new Random().nextInt(10000);
        Exchange exchange = new Exchange(uniqueKey, accountId, from, to, value);
        queue.add(exchange);
        run();
    }

    public void run() {
        if (queue.isEmpty()) {
            return;
        }
        Exchange exchange = queue.poll();
        String uniqueKey = exchange.getId();

        try {
            Long accountId = exchange.getAccountId();
            Currency from = exchange.getFrom();
            Currency to = exchange.getTo();
            BigDecimal value = exchange.getValue();

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
        }
    }
}
