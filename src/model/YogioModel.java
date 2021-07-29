package model;

import dao.AdminDao;
import dao.CustomerDao;
import dao.FoodDao;
import dao.OrderDao;
import dao.SalesFoodDao;
import dto.AdminInfoDTO;
import dto.CustomerInfoDTO;
import dto.FoodInfoDTO;
import dto.OrderInfoDTO;
import dto.SalesChkDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xxseokkixx
 */
public class YogioModel implements YogioInter {

    // ID중복체크 => count값 1 or 0 의 여부에 따라 메세지 출력.
    @Override
    public String findId(String id) {
        String msg = null;
        int res = CustomerDao.getDao().findUserId(id);
        if (res == 0) {
            msg = "사용 가능한 아이디 입니다.";
            return msg;
        } else {
            msg = "이미 사용중인 아이디 입니다.";
            return msg;
        }
    }

    // login시 입력한 id, pw를 저장하는 메소드
    @Override
    public Map<String, String> login(CustomerInfoDTO dto) {
        return CustomerDao.getDao().loginProcess(dto);
    }

    // 계정일치 정보를 true or false로 판별 후 값 전달.
    @Override
    public boolean loginTF(CustomerInfoDTO dto) {
        Map<String, String> map = new HashMap<>();
        map = CustomerDao.getDao().loginProcess(dto);
        if (map.get("cnt").equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    // insert여부를 True or False로 반환.
    @Override
    public boolean addCustomer(CustomerInfoDTO dto) {
        boolean res = CustomerDao.getDao().addCustomer(dto);
        return res;
    }

    // 로그인한 id를 parameter값으로 전달하고, 상세정보를 출력하는 method.
    @Override
    public CustomerInfoDTO custDetail(String custid) {
        return CustomerDao.getDao().custDetail(custid);
    }

    // 회원정보를 update하는 method
    @Override
    public boolean updateCustomer(CustomerInfoDTO dto) {
        return CustomerDao.getDao().updateCustomer(dto);
    }

    // 카테고리별 음식을 출력하는 메소드.
    @Override
    public ArrayList<FoodInfoDTO> listCategory(String category) {
        return FoodDao.getDao().listCategory(category);
    }

    // 선택한 음식의 번호를 조건으로 상세정보 출력하는 메소드.
    @Override
    public FoodInfoDTO selectFood(int num) {
        return FoodDao.getDao().selectFood(num);
    }

    // 주문한 내역을 테이블에 저장.
    @Override
    public boolean addOrderList(OrderInfoDTO dto) {
        return OrderDao.getDao().addOrderList(dto);
    }

    // 로그인한 회원의 주문내역리스트를 출력
    @Override
    public ArrayList<OrderInfoDTO> printOrderList(String custId) {
        return OrderDao.getDao().printOrderList(custId);
    }

     // 관리자 로그인
    @Override
    public Map<String, String> loginAdmin(String adminid, String adminpw) {
        return AdminDao.getDao().loginAdmin(adminid, adminpw);
    }

    // 검색조건에 따른 관리자 검색.
    @Override
    public ArrayList<AdminInfoDTO> searchPrintAdmin(String input, int index) {
        return AdminDao.getDao().searchPrintAdmin(input, index);
    }

     // 모든 관리자 출력
    @Override
    public ArrayList<AdminInfoDTO> searchAllAdmin() {
        return AdminDao.getDao().searchAllAdmin();
    }

    // 음식판매내역을 출력하는 메소드
    @Override
    public ArrayList<SalesChkDTO> showSalesFood() {
        return SalesFoodDao.getDao().showSalesFood();
    }

    @Override
    public int sumFoodPrice() {
        return SalesFoodDao.getDao().sumFoodPrice();
    }
   
}
