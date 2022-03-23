package task5;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import task5.model.Currency;
import task5.repository.AccountRepository;
import task5.service.AccountService;
import task5.service.ExchangeRateService;
import task5.service.ExchangerService;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        try {
            AccountRepository accountRepository = new AccountRepository();
            AccountService accountService = new AccountService(accountRepository);
            ExchangeRateService exchangeRateService = new ExchangeRateService();

            ExchangerService exchangerService = new ExchangerService(accountService, exchangeRateService);

            accountService.printAccounts();

            ExecutorService service = Executors.newFixedThreadPool(3);

            service.execute(()-> exchangerService.exchange(1L, Currency.USD, Currency.UAN, BigDecimal.valueOf(10)));
            service.execute(()-> exchangerService.exchange(1L, Currency.USD, Currency.UAN, BigDecimal.valueOf(2)));

            service.shutdown();
            Thread.sleep(1000);
            accountService.printAccounts();


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
