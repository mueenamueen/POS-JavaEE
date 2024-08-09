package lk.ijse.gdse.javaee.posbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Item {
    private String code;
    private String name;
    private int qty;
    private double unitPrice;
}
