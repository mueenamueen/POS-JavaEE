package lk.ijse.gdse.javaee.posbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailDTO implements Serializable {
    private String orderId;
    private String itemCode;
    private String itemName;
    private BigDecimal itemPrice;
    private Integer quantity;
    private BigDecimal total;
}
