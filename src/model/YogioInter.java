package model;

import dto.AdminInfoDTO;
import dto.CustomerInfoDTO;
import dto.FoodInfoDTO;
import dto.OrderInfoDTO;
import dto.SalesChkDTO;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author xxseokkixx
 */
public interface YogioInter {

    // id 중복검색 method
    public String findId(String id);

    // 로그인 기능
    public Map<String, String> login(CustomerInfoDTO dto);

    // 로그인시 boolean 타입으로 True or False 전달**********보류
    public boolean loginTF(CustomerInfoDTO dto);
    // *****************************************************보류

    // 회원등록
    public boolean addCustomer(CustomerInfoDTO dto);

    // 회원상세정보
    public CustomerInfoDTO custDetail(String custid);

    // 회원정보업데이트
    public boolean updateCustomer(CustomerInfoDTO dto);

    // 카테고리별 음식출력
    public ArrayList<FoodInfoDTO> listCategory(String category);

    // 음식정보 상세출력
    public FoodInfoDTO selectFood(int num);

    // 결제버튼 클릭시 주문리스트 저장.
    public boolean addOrderList(OrderInfoDTO dto);
    
    // 로그인한 회원의 주문내역리스트를 출력
    public ArrayList<OrderInfoDTO> printOrderList(String custId);
    
    // 관리자 로그인
    public Map<String, String> loginAdmin(String adminid, String adminpw);
    
    // 모든 관리자 출력
    public ArrayList<AdminInfoDTO> searchAllAdmin();
    
    // 검색조건에 따른 관리자 검색.
    public ArrayList<AdminInfoDTO> searchPrintAdmin(String input, int index);
    
    // 음식판매내역을 출력하는 메소드
    public ArrayList<SalesChkDTO> showSalesFood();
    
    // 매출합계구하는 함수.
    public int sumFoodPrice();
}
