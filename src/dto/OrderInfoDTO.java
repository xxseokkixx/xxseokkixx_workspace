package dto;

/**
 *
 * @author xxseokkixx
 */
public class OrderInfoDTO {

    private int orderHis;
    private String custId;
    private String orderName;
    private int foodNum;
    private String foodName;
    private String orderTel;
    private String orderAddr;
    private String orderDate;
    private FoodInfoDTO foodInfoDTO;

    public int getOrderHis() {
        return orderHis;
    }

    public void setOrderHis(int orderHis) {
        this.orderHis = orderHis;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public int getFoodNum() {
        return foodNum;
    }

    public void setFoodNum(int foodNum) {
        this.foodNum = foodNum;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getOrderTel() {
        return orderTel;
    }

    public void setOrderTel(String orderTel) {
        this.orderTel = orderTel;
    }

    public String getOrderAddr() {
        return orderAddr;
    }

    public void setOrderAddr(String orderAddr) {
        this.orderAddr = orderAddr;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public FoodInfoDTO getFoodInfoDTO() {
        return foodInfoDTO;
    }

    public void setFoodInfoDTO(FoodInfoDTO foodInfoDTO) {
        this.foodInfoDTO = foodInfoDTO;
    }

}
