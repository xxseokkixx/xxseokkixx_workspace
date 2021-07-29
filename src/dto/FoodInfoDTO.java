package dto;

/**
 *
 * @author xxseokkixx
 */
public class FoodInfoDTO {

    private int foodNum;
    private String foodCategory;
    private String foodName;
    private String foodDetail;
    private int foodPrice;

    public int getFoodNum() {
        return foodNum;
    }

    public void setFoodNum(int foodNum) {
        this.foodNum = foodNum;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDetail() {
        return foodDetail;
    }

    public void setFoodDetail(String foodDetail) {
        this.foodDetail = foodDetail;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

}
