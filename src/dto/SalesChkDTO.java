package dto;

/**
 *
 * @author xxseokkixx
 */
public class SalesChkDTO {

    private int salesNum;
    private int salesFoodNum;
    private String salesDate;
    private FoodInfoDTO foodDto;

    public int getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(int salesNum) {
        this.salesNum = salesNum;
    }

    public int getSalesFoodNum() {
        return salesFoodNum;
    }

    public void setSalesFoodNum(int salesFoodNum) {
        this.salesFoodNum = salesFoodNum;
    }

    public String getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    public FoodInfoDTO getFoodDto() {
        return foodDto;
    }

    public void setFoodDto(FoodInfoDTO foodDto) {
        this.foodDto = foodDto;
    }

}
