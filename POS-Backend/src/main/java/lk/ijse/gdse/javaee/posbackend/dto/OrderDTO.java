package lk.ijse.gdse.javaee.posbackend.dto;

import jakarta.json.bind.annotation.JsonbDateFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO implements Serializable {
    private String orderId;
    @JsonbDateFormat("M/d/yyyy")
    private Date orderDate;
    private String customerId;
    private BigDecimal total;
    private BigDecimal subTotal;
    private BigDecimal paidAmount;
    private BigDecimal discount;
    private BigDecimal balance;
    private String address;
    private List<OrderDetailDTO> orderDetails;
}
