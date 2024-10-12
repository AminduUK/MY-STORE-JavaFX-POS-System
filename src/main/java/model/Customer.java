package model;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Customer {
    private String id;
    private String title;
    private String name;
    private LocalDate dob;
    private int contactNum;
    private String profession;
    private String address;
    private String city;

}
