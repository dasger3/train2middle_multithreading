package task5.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Exchange {
    private String id;
    private Long accountId;
    private Currency from;
    private Currency to;
    private BigDecimal value;
}
