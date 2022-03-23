package task5.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import task5.Constants;
import task5.model.Currency;
import task5.model.ExchangeRates;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

@Service
public class ExchangeRateService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateService.class);
    private List<ExchangeRates> listOfExchangeRates = new LinkedList<>();

    public ExchangeRateService() {
        initFromFile(Constants.TEMPLATE_EXCHANGE_RATE_FILENAME);
    }

    public void changeExchangeRate(Currency currencyFrom, Currency currencyTo, BigDecimal newRate) {
        findWithCurrency(currencyFrom, currencyTo).setRate(newRate);
        findWithCurrency(currencyTo, currencyFrom).setRate(createInverseAndRoundBigDecimal(newRate));
    }

    public BigDecimal getExchangeRate(Currency currencyFrom, Currency currencyTo) {
        return findWithCurrency(currencyFrom, currencyTo).getRate();
    }

    public void printExchangeRate() {
        System.out.println("---------------------");
        for (ExchangeRates listOfExchangeRate : listOfExchangeRates) {
            System.out.println(listOfExchangeRate.getFrom() + "-" + listOfExchangeRate.getTo() + " " +
                    listOfExchangeRate.getRate());
        }
        System.out.println("---------------------");
    }

    private ExchangeRates findWithCurrency(Currency currencyFrom, Currency currencyTo) {
        return listOfExchangeRates.stream()
                .filter(t -> t.getFrom().equals(currencyFrom) && t.getTo().equals(currencyTo))
                .findFirst()
                .orElseThrow(NullPointerException::new);
    }

    private BigDecimal createInverseAndRoundBigDecimal(BigDecimal a) {
        return BigDecimal.valueOf(1 / a.doubleValue()).setScale(2, RoundingMode.HALF_UP);
    }

    private void initFromFile(String filename) {
        try {
            for (String s : FileService.readFromFile(filename).split("\n")) {
                String[] line = s.split(" ");
                Currency firstCurrency = Currency.valueOf(line[0]);
                Currency secondCurrency = Currency.valueOf(line[1]);
                BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(line[2]));
                BigDecimal inverseRate = createInverseAndRoundBigDecimal(rate);
                listOfExchangeRates.add(new ExchangeRates(firstCurrency, secondCurrency, rate));
                listOfExchangeRates.add(new ExchangeRates(secondCurrency, firstCurrency, inverseRate));
            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        }
    }
}
