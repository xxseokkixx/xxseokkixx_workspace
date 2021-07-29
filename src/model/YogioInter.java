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

    // id �ߺ��˻� method
    public String findId(String id);

    // �α��� ���
    public Map<String, String> login(CustomerInfoDTO dto);

    // �α��ν� boolean Ÿ������ True or False ����**********����
    public boolean loginTF(CustomerInfoDTO dto);
    // *****************************************************����

    // ȸ�����
    public boolean addCustomer(CustomerInfoDTO dto);

    // ȸ��������
    public CustomerInfoDTO custDetail(String custid);

    // ȸ������������Ʈ
    public boolean updateCustomer(CustomerInfoDTO dto);

    // ī�װ��� �������
    public ArrayList<FoodInfoDTO> listCategory(String category);

    // �������� �����
    public FoodInfoDTO selectFood(int num);

    // ������ư Ŭ���� �ֹ�����Ʈ ����.
    public boolean addOrderList(OrderInfoDTO dto);
    
    // �α����� ȸ���� �ֹ���������Ʈ�� ���
    public ArrayList<OrderInfoDTO> printOrderList(String custId);
    
    // ������ �α���
    public Map<String, String> loginAdmin(String adminid, String adminpw);
    
    // ��� ������ ���
    public ArrayList<AdminInfoDTO> searchAllAdmin();
    
    // �˻����ǿ� ���� ������ �˻�.
    public ArrayList<AdminInfoDTO> searchPrintAdmin(String input, int index);
    
    // �����Ǹų����� ����ϴ� �޼ҵ�
    public ArrayList<SalesChkDTO> showSalesFood();
    
    // �����հ豸�ϴ� �Լ�.
    public int sumFoodPrice();
}
