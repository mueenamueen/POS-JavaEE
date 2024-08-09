package lk.ijse.gdse.javaee.posbackend.entity;

import lk.ijse.gdse.javaee.posbackend.dto.OrderDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {
    private String orderId;
    private Date orderDate;
    private String customerId;
    private BigDecimal total;
    private BigDecimal subTotal;
    private BigDecimal paidAmount;
    private BigDecimal discount;
    private BigDecimal balance;
    private String address;
}
