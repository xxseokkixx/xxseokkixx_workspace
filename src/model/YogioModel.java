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

    // ID�ߺ�üũ => count�� 1 or 0 �� ���ο� ���� �޼��� ���.
    @Override
    public String findId(String id) {
        String msg = null;
        int res = CustomerDao.getDao().findUserId(id);
        if (res == 0) {
            msg = "��� ������ ���̵� �Դϴ�.";
            return msg;
        } else {
            msg = "�̹� ������� ���̵� �Դϴ�.";
            return msg;
        }
    }

    // login�� �Է��� id, pw�� �����ϴ� �޼ҵ�
    @Override
    public Map<String, String> login(CustomerInfoDTO dto) {
        return CustomerDao.getDao().loginProcess(dto);
    }

    // ������ġ ������ true or false�� �Ǻ� �� �� ����.
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

    // insert���θ� True or False�� ��ȯ.
    @Override
    public boolean addCustomer(CustomerInfoDTO dto) {
        boolean res = CustomerDao.getDao().addCustomer(dto);
        return res;
    }

    // �α����� id�� parameter������ �����ϰ�, �������� ����ϴ� method.
    @Override
    public CustomerInfoDTO custDetail(String custid) {
        return CustomerDao.getDao().custDetail(custid);
    }

    // ȸ�������� update�ϴ� method
    @Override
    public boolean updateCustomer(CustomerInfoDTO dto) {
        return CustomerDao.getDao().updateCustomer(dto);
    }

    // ī�װ��� ������ ����ϴ� �޼ҵ�.
    @Override
    public ArrayList<FoodInfoDTO> listCategory(String category) {
        return FoodDao.getDao().listCategory(category);
    }

    // ������ ������ ��ȣ�� �������� ������ ����ϴ� �޼ҵ�.
    @Override
    public FoodInfoDTO selectFood(int num) {
        return FoodDao.getDao().selectFood(num);
    }

    // �ֹ��� ������ ���̺� ����.
    @Override
    public boolean addOrderList(OrderInfoDTO dto) {
        return OrderDao.getDao().addOrderList(dto);
    }

    // �α����� ȸ���� �ֹ���������Ʈ�� ���
    @Override
    public ArrayList<OrderInfoDTO> printOrderList(String custId) {
        return OrderDao.getDao().printOrderList(custId);
    }

     // ������ �α���
    @Override
    public Map<String, String> loginAdmin(String adminid, String adminpw) {
        return AdminDao.getDao().loginAdmin(adminid, adminpw);
    }

    // �˻����ǿ� ���� ������ �˻�.
    @Override
    public ArrayList<AdminInfoDTO> searchPrintAdmin(String input, int index) {
        return AdminDao.getDao().searchPrintAdmin(input, index);
    }

     // ��� ������ ���
    @Override
    public ArrayList<AdminInfoDTO> searchAllAdmin() {
        return AdminDao.getDao().searchAllAdmin();
    }

    // �����Ǹų����� ����ϴ� �޼ҵ�
    @Override
    public ArrayList<SalesChkDTO> showSalesFood() {
        return SalesFoodDao.getDao().showSalesFood();
    }

    @Override
    public int sumFoodPrice() {
        return SalesFoodDao.getDao().sumFoodPrice();
    }
   
}
