package controller.order;

import model.OrderDetail;
import util.CrudUtil;

import java.sql.SQLException;
import java.util.List;

public class OrderDetailController {

    public boolean addOrderDetail(List<OrderDetail> orderDetails){
        for (OrderDetail orderDetail : orderDetails){
           boolean isAdded = addOrderDetail(orderDetail);
           if (!isAdded){
               return false;
           }
        }
        return true;
    }

    public boolean addOrderDetail(OrderDetail orderDetails){
        String SQL = "INSERT INTO orderdetail VALUES(?,?,?,?)";
        try {
            Object execute = CrudUtil.execute(SQL,
                    orderDetails.getOrderID(),
                    orderDetails.getItemCode(),
                    orderDetails.getQuantity(),
                    orderDetails.getDiscount()
            );
            if (execute != null){
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
