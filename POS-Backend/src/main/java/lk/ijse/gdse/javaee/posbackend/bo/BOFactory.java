package lk.ijse.gdse.javaee.posbackend.bo;

import lk.ijse.gdse.javaee.posbackend.bo.custom.impl.CustomerBOImpl;
import lk.ijse.gdse.javaee.posbackend.bo.custom.impl.ItemBOImpl;
import lk.ijse.gdse.javaee.posbackend.bo.custom.impl.PlaceOrderBOImpl;

public class BOFactory {
    private static BOFactory boFactory;
    public static BOFactory getBoFactory(){
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }
    public enum BOTypes{
        CUSTOMER,ITEM,PLACE_ORDER
    }
    public SuperBO getBO(BOTypes boTypes){
        switch (boTypes){
            case CUSTOMER:
                return new CustomerBOImpl();
            case ITEM:
                return new ItemBOImpl();
            case PLACE_ORDER:
                return new PlaceOrderBOImpl();
            default:
                return null;
        }
    }
}
