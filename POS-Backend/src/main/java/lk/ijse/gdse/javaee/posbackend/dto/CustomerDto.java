package lk.ijse.gdse.javaee.posbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDto implements Serializable {
    private String id;
    private String name;
    private String address;
    private double salary;
}
