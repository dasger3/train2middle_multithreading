package task5;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import task5.model.Currency;
import task5.service.AccountService;
import task5.service.ExchangerService;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class Main implements CommandLineRunner {

    private final AccountService accountService;
    private final ExchangerService exchangerService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        try {
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
