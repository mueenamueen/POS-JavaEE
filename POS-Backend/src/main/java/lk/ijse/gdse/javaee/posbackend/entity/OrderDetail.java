package lk.ijse.gdse.javaee.posbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetail {
    private String orderId;
    private String itemCode;
    private String itemName;
    private BigDecimal itemPrice;
    private Integer quantity;
    private BigDecimal total;
}
