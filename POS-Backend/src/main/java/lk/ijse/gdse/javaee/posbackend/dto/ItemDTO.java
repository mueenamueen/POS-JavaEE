package lk.ijse.gdse.javaee.posbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDTO implements Serializable {
    private String code;
    private String name;
    private int qty;
    private double unitPrice;
}
