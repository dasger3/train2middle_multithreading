package task5;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import task5.model.Currency;
import task5.service.AccountService;
import task5.service.ExchangerService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class ExchangeTest {

    @SpyBean
    private AccountService accountService;
    @SpyBean
    private ExchangerService exchangerService;

    @Test
    public void contextLoad() {

    }

    @Test
    public void exchangeSomeTest() throws InterruptedException {

        ExecutorService service = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 1000; i++) {

            service.execute(() -> {
                List<Currency> twoDifferentRandomCurrency = generateRandomCurrenciesForExchange();
                exchangerService.exchange(generateRandomAccountId(),
                        twoDifferentRandomCurrency.get(0),
                        twoDifferentRandomCurrency.get(1),
                        generateRandomValue());
            });
        }

        //service.shutdown();
        Thread.sleep(1000);
        accountService.printAccounts();
    }

    private Long generateRandomAccountId() {
        return (long) (1 + Math.random() * (accountService.getAllAccounts().size() - 1));
    }

    private List<Currency> generateRandomCurrenciesForExchange() {
        Currency[] currencies = Currency.class.getEnumConstants();
        int firstIndex = 0;
        int secondIndex = 0;
        while (firstIndex == secondIndex) {
            firstIndex = (int) (Math.random() * currencies.length);
            secondIndex = (int) (Math.random() * currencies.length);
        }
        return Arrays.asList(currencies[firstIndex], currencies[secondIndex]);
    }

    private BigDecimal generateRandomValue() {
        return BigDecimal.valueOf(Math.random() * 20);
    }
}
