package task5.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExchangeRates {
    Currency from;
    Currency to;
    BigDecimal rate;
}
