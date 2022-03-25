package task5.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class Wallet {
    private Map<Currency, BigDecimal> balance = new HashMap<>();
}
