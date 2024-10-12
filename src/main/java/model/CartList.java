package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartList {
    private String itemCode;
    private String itemName;
    private String packSize;
    private Integer quantity;
    private Double unitPrice;
    private Double total;
}