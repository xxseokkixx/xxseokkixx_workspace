/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import dto.AdminInfoDTO;
import dto.CustomerInfoDTO;
import dto.FoodInfoDTO;
import dto.OrderInfoDTO;
import dto.SalesChkDTO;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;
import javax.xml.ws.http.HTTPBinding;
import model.YogioInter;
import model.YogioModel;

/**
 *
 * @author xxseokkixx
 */
public class YogioGUI extends javax.swing.JFrame {

    // 주문패널을 위한 CardLayout
    private CardLayout card;
    // 관리자패널을 위한 CardLayout
    private CardLayout card2;
    private YogioInter model;
    private StringBuilder str;
    // 회원 로그인창에서 입력받은 id를 임시저장하기위한 멤버필드 변수.
    private String custid;
    // 관리자 이름 저장하기위한 변수.
    private String adminName;
    // row의 번호를 저장하기위해.
    private int priNum;
    // 카테고리명을 전달하기 위한 멤버필드 변수.
    private String category;
    // 음식 상세보기 뷰
    private FoodDetailView fdview;

    public CardLayout getCard() {
        return card;
    }

    public JPanel getPp() {
        return pp;
    }

    /**
     * Creates new form yogiyoGUI
     */
    public YogioGUI() {
        model = new YogioModel();
        fdview = new FoodDetailView(YogioGUI.this);
        initComponents();
        // 회원메뉴 CardLayout
        card = (CardLayout) pp.getLayout();
        // 관리자메뉴 CardLayout
        card2 = (CardLayout) adminDown.getLayout();
        // 관리자 페이지 버튼.
        adminPageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 관리자 로그인 페이지로 이동.
                card.show(pp, "adminLoginC");
            }
        });
        // 관리자 로그인 기능.
        adminLogInBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminLogin();
                // StringBuilder생성
                str = new StringBuilder();
                // 로그인한 아이디 설정
                str.append(adminName).append(" 님 접속중.");
                nowAdminId.setText(str.toString());
                card2.show(adminDown, "adminListC");
            }
        });
        // 초기화면으로 돌아가기
        backFirstBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(pp, "logInC");
            }
        });
        // 관리자 목록 출력.
        adminListBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card2.show(adminDown, "adminListC");
                loadAllAdmin();
            }
        });
        // 검색조건을 통해 받아온 관리자 목록을 출력.
        printAdminBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String search = searchv.getText().trim();// 검색할 값.
                int index = combo.getSelectedIndex(); // combox메뉴값.
                try {
                    loadSearchAdminList(search, index);
                    searchv.setText("");
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        });
        // 판매내역을 출력.
        salesFoodBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card2.show(adminDown, "salesFoodC");
                loadAllSalesFood();
            }
        });
        // 판매매출합계를 출력.
        calSalesTotalBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                str = new StringBuilder();
                int totalSales = model.sumFoodPrice();
                str.append(totalSales).append("원");
                total.setText(str.toString());
            }
        });
        // 회원 로그인버튼.
        logInBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                custLogin();
            }
        });
        // 회원가입 패널로 이동.
        signInBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(pp, "signInC");
            }
        });
        // id중복체크.
        idChkBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String res = model.findId(signIdv.getText().trim());
                JOptionPane.showMessageDialog(YogioGUI.this, res);
            }
        });
        // 회원가입에 입력한 정보를 등록
        insertBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomerInfoDTO signVo = new CustomerInfoDTO();
                signVo.setCustId(signIdv.getText().trim());
                signVo.setCustPw(new String(signPwv.getPassword()));
                signVo.setCustName(signNamev.getText().trim());
                if (signAgev.getText().trim().equals("")) {
                    signVo.setCustAge(0);
                } else {
                    signVo.setCustAge(Integer.parseInt(signAgev.getText().trim()));
                }
                signVo.setCustGender(signGenderv.getText().trim());
                signVo.setCustAddr(signAddrv.getText().trim());
                signVo.setCustTel(signTelv.getText().trim());
                boolean res = model.addCustomer(signVo);
                str = new StringBuilder();
                if (!res) {
                    str.append("회원가입에 실패하였습니다.").append("\n");
                    str.append("입력정보를 다시 확인해 주세요.");
                } else {
                    str.append("회원가입에 성공하였습니다.");
                    card.show(pp, "logInC");
                }
                JOptionPane.showMessageDialog(YogioGUI.this, str.toString());
            }
        });
        backLogBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(pp, "logInC");
            }
        });
        // 이전에 저장된 회원정보를 출력.
        myInfoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getCustInfo();
            }
        });
        // 회원정보를 수정.
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                custInfoUpdate();
            }
        });
        // 뒤로가는 메뉴버튼.
        backMenuBtn.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                card.show(pp, "orderMenuC");
            }
        });
        // 클릭한 음식번호를 저장
        foodTable.addMouseListener(
                new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e
            ) {
                int row = foodTable.getSelectedRow();
                priNum = Integer.parseInt(foodTable.getValueAt(row, 0).toString());
                // 음식 클릭시 음식번호 출력,.
                System.out.println(foodTable.getValueAt(row, 0));
            }
        });
        // 한식 카테고리 출력.
        korFoodBtn.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                category = "한식";
                loadFoodInfo(category);
            }
        });
        // 중식 카테고리 출력.
        chnFoodBtn.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                category = "중식";
                loadFoodInfo(category);
            }
        });
        // 일식 카테고리 출력.
        jpnFoodBtn.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                category = "일식";
                loadFoodInfo(category);
            }
        });

        // 음식 상세보기 페이지로 이동
        foodDetailBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                if (priNum == 0) {
                    JOptionPane.showMessageDialog(YogioGUI.this, "음식을 선택해주세요.");
                } else {
                    fdview.detailVisible(true, priNum);
                }
            }
        });
        orderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (priNum > 0) {
                    card.show(pp, "orderc");
                    setImg(priNum);
                    getfoodCustInfo(custid, priNum);
                } else {
                    JOptionPane.showMessageDialog(YogioGUI.this, "음식을 선택해주세요.");
                }
            }
        });
        // 주문취소버튼.
        cancelOrderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(pp, "orderMenuC");
            }
        });
        // 결제완료버튼.
        confirmOrderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OrderInfoDTO vo = new OrderInfoDTO();
                vo.setCustId(custid);
                vo.setFoodNum(Integer.parseInt(orderFoodNumv.getText().trim()));
                vo.setFoodName(orderFoodNamev.getText().trim());
                vo.setOrderName(orderCustNamev.getText().trim());
                vo.setOrderTel(orderCustTelv.getText().trim());
                vo.setOrderAddr(orderCustAddrv.getText().trim());
                boolean res = model.addOrderList(vo);
                if (res == true) {
                    JOptionPane.showMessageDialog(pp, "결제완료");
                    card.show(pp, "orderMenuC");
                } else {
                    JOptionPane.showMessageDialog(pp, "결제 실패 하셨습니다 ");

                }

            }
        });
        // 주문내역 조회버튼.
        orderListBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                custNowId.setText(custid);
                loadOrderList(custid);
                card.show(pp, "orderListc");
            }
        });
        // 주문내역조회화면에서 이전으로 돌아가는 버튼.
        listBackBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(pp, "orderMenuC");
            }
        });
    }

    // 이미지 출력하는 메소드
    public void setImg(int num) {
        num = priNum;
        orderimage.setIcon(new javax.swing.ImageIcon(getClass().getResource("../foodimage/" + num + ".jpg")));
    }

    // 검색조건에 따른 관리자 검색내역 출력
    private void loadSearchAdminList(String search, int index) {
        ArrayList<AdminInfoDTO> adminArr = model.searchPrintAdmin(search, index);
        adminTable.setModel(new AbstractTableModel() {
            String[] name = {"관리자번호", "관리자ID", "관리자명", "관리자직책", "입사일자"};

            @Override
            public int getRowCount() {
                return adminArr.size();
            }

            @Override
            public int getColumnCount() {
                return name.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Object obj = null;
                switch (columnIndex) {
                    case 0:
                        obj = adminArr.get(rowIndex).getAdminNum();
                        break;
                    case 1:
                        obj = adminArr.get(rowIndex).getAdminId();
                        break;
                    case 2:
                        obj = adminArr.get(rowIndex).getAdminName();
                        break;
                    case 3:
                        obj = adminArr.get(rowIndex).getAdminPosition();
                        break;
                    case 4:
                        obj = adminArr.get(rowIndex).getAdminHireDate();
                        break;
                }
                return obj;
            }

            @Override
            public String getColumnName(int column) {
                return name[column];
            }
        });
    }

    // 관리자 목록 전체 출력.
    private void loadAllAdmin() {
        ArrayList<AdminInfoDTO> adminArr = model.searchAllAdmin();
        adminTable.setModel(new AbstractTableModel() {
            String[] name = {"관리자번호", "관리자ID", "관리자명", "관리자직책", "입사일자"};

            @Override
            public int getRowCount() {
                return adminArr.size();
            }

            @Override
            public int getColumnCount() {
                return name.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Object obj = null;
                switch (columnIndex) {
                    case 0:
                        obj = adminArr.get(rowIndex).getAdminNum();
                        break;
                    case 1:
                        obj = adminArr.get(rowIndex).getAdminId();
                        break;
                    case 2:
                        obj = adminArr.get(rowIndex).getAdminName();
                        break;
                    case 3:
                        obj = adminArr.get(rowIndex).getAdminPosition();
                        break;
                    case 4:
                        obj = adminArr.get(rowIndex).getAdminHireDate();
                        break;
                }
                return obj;
            }

            @Override
            public String getColumnName(int column) {
                return name[column];
            }
        });
    }

    // 총 판매내역 출력.
    private void loadAllSalesFood() {
        ArrayList<SalesChkDTO> salesArr = model.showSalesFood();
        salesTable.setModel(new AbstractTableModel() {
            String[] name = {"판매번호", "음식번호", "음식분류", "음식명", "가격", "판매날짜"};

            @Override
            public int getRowCount() {
                return salesArr.size();
            }

            @Override
            public int getColumnCount() {
                return name.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Object obj = null;
                SalesChkDTO vo = new SalesChkDTO();
                // join한 음식테이블의 정보를 가져오기위한 DTO선언
                vo.setFoodDto(new FoodInfoDTO());
                switch (columnIndex) {
                    case 0:
                        obj = salesArr.get(rowIndex).getSalesNum();
                        break;
                    case 1:
                        obj = salesArr.get(rowIndex).getSalesFoodNum();
                        break;
                    case 2:
                        obj = salesArr.get(rowIndex).getFoodDto().getFoodCategory();
                        break;
                    case 3:
                        obj = salesArr.get(rowIndex).getFoodDto().getFoodName();
                        break;
                    case 4:
                        obj = salesArr.get(rowIndex).getFoodDto().getFoodPrice();
                        break;
                    case 5:
                        obj = salesArr.get(rowIndex).getSalesDate();
                        break;
                }
                return obj;
            }

            @Override
            public String getColumnName(int column) {
                return name[column];
            }
        });
    }

    // 음식 카테고리별 목록 출력.
    private void loadFoodInfo(String category) {
        ArrayList<FoodInfoDTO> foodArr = model.listCategory(category);
        // DAO에서 전송받은 값을 테이블로 출력.
        foodTable.setModel(new AbstractTableModel() {
            String[] cname = {"음식번호", "음식분류", "음식명", "음식설명", "가격"};

            @Override
            public int getRowCount() {
                return foodArr.size();
            }

            @Override
            public int getColumnCount() {
                return cname.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Object obj = null;
                switch (columnIndex) {
                    case 0:
                        obj = foodArr.get(rowIndex).getFoodNum();
                        break;
                    case 1:
                        obj = foodArr.get(rowIndex).getFoodCategory();
                        break;
                    case 2:
                        obj = foodArr.get(rowIndex).getFoodName();
                        break;
                    case 3:
                        obj = foodArr.get(rowIndex).getFoodDetail();
                        break;
                    case 4:
                        obj = foodArr.get(rowIndex).getFoodPrice();
                        break;
                }
                return obj;
            }

            @Override
            public String getColumnName(int column) {
                return cname[column];
            }
        });
    }

    // 회원의 주문 내역 출력
    private void loadOrderList(String custid) {
        custid = this.custid;
        ArrayList<OrderInfoDTO> orderArr = model.printOrderList(custid);
        listTable.setModel(new AbstractTableModel() {
            String[] name = {"주문 번호", "음식 카테고리", "음식명", "음식 가격", "주문자 전화번호",
                "주문자 주소", "주문 날짜"};

            @Override
            public int getRowCount() {
                return orderArr.size();
            }

            @Override
            public int getColumnCount() {
                return name.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Object obj = null;
                OrderInfoDTO orderVo = new OrderInfoDTO();
                // join한 음식테이블의 정보를 가져오기위한 DTO선언
                orderVo.setFoodInfoDTO(new FoodInfoDTO());
                switch (columnIndex) {
                    case 0:
                        obj = orderArr.get(rowIndex).getOrderHis();
                        break;
                    case 1:
                        obj = orderArr.get(rowIndex).getFoodInfoDTO().getFoodCategory();
                        break;
                    case 2:
                        obj = orderArr.get(rowIndex).getFoodName();
                        break;
                    case 3:
                        obj = orderArr.get(rowIndex).getFoodInfoDTO().getFoodPrice();
                        break;
                    case 4:
                        obj = orderArr.get(rowIndex).getOrderTel();
                        break;
                    case 5:
                        obj = orderArr.get(rowIndex).getOrderAddr();
                        break;
                    case 6:
                        obj = orderArr.get(rowIndex).getOrderDate();
                        break;
                }
                return obj;
            }

            @Override
            public String getColumnName(int column) {
                return name[column];
            }
        });
    }

    // 음식 정보가져오기
    public void getfoodCustInfo(String custid, int num) {
        custid = this.custid;
        orderCustIdv.setText(custid);
        num = priNum;
        // 선택한 음식정보의 상세정보.
        FoodInfoDTO foodVo = model.selectFood(num);
        // 음식번호
        orderFoodNumv.setText(String.valueOf(foodVo.getFoodNum()));
        // 음식이름
        orderFoodNamev.setText(foodVo.getFoodName());
        // 음식가격
        orderFoodPricev.setText(String.valueOf(foodVo.getFoodPrice()));
        // 주문한 회원의 정보
        CustomerInfoDTO custVo = model.custDetail(custid);
        // 회원명
        orderCustNamev.setText(custVo.getCustName());
        // 연락처
        orderCustTelv.setText(custVo.getCustTel());
        // 주소
        orderCustAddrv.setText(custVo.getCustAddr());
    }

    // 회원 로그인 메소드
    private void custLogin() {
        str = new StringBuilder();
        CustomerInfoDTO vo = new CustomerInfoDTO();
        // 로그인 입력창 ID
        custid = logInId_v.getText().trim();
        nowloginId.setText(custid);
        vo.setCustId(logInId_v.getText().trim());
        // 로그인 입력창 Password
        vo.setCustPw(new String(logInPw_v.getPassword()));
        Map<String, String> map = model.login(vo);
        // 검색과 일치하는 유저이름 변수저장.
        if (map.get("cnt").equals("1")) {
            str.append(map.get("custname")).append(" 님, 환영합니다.");
            JOptionPane.showMessageDialog(YogioGUI.this, str.toString());
            card.show(pp, "orderMenuC");
        } else {
            str.append("가입된 정보가 존재하지 않습니다.");
            JOptionPane.showMessageDialog(YogioGUI.this, str.toString());
        }
    }

    // 가입시 입력했던 정보를 가져오는 메소드
    private void getCustInfo() {
        CustomerInfoDTO vo = model.custDetail(custid);
        updateidv.setText(vo.getCustId()); // 사용자ID
        updatepwv.setText(vo.getCustPw()); // 사용자 PW
        updatenamev.setText(vo.getCustName()); // 사용자이름
        updateagev.setText(String.valueOf(vo.getCustAge()));// 나이
        updategenderv.setText(vo.getCustGender());// 성별
        updateaddv.setText(vo.getCustAddr());// 주소
        updatetelv.setText(vo.getCustTel());// 전화번호
        card.show(pp, "updateC");
    }

    // 회원정보를 수정하는 메소드
    private void custInfoUpdate() {
        boolean res = true;
        str = new StringBuilder();
        CustomerInfoDTO vo = new CustomerInfoDTO();
        vo.setCustId(updateidv.getText().trim()); // ****지우면안됨.=> update조건문.
        if (!new String(updatepwv.getPassword()).equals("")) {
            vo.setCustPw(new String(updatepwv.getPassword())); // 비밀번호.
        } else {
            JOptionPane.showMessageDialog(YogioGUI.this, "비밀번호를 입력해 주세요.");
            res = false;
        }
        vo.setCustName(updatenamev.getText().trim()); // 사용자 이름
        if (updateagev.getText().trim().equals("")) { // 사용자 나이
            vo.setCustAge(0);
        } else {
            vo.setCustAge(Integer.parseInt(updateagev.getText().trim()));
        }
        vo.setCustGender(updategenderv.getText().trim()); // 사용자 성별
        vo.setCustAddr(updateaddv.getText().trim()); // 사용자 주소
        vo.setCustTel(updatetelv.getText().trim()); // 사용자 전화번호
        if (res == true) {
            str.append(vo.getCustName()).append(" 님의 회원정보가 수정되었습니다.").append("\n");
            JOptionPane.showMessageDialog(YogioGUI.this, str.toString());
            model.updateCustomer(vo);
        } else {
            str.append(vo.getCustName()).append(" 님의 회원정보 수정이 실패하였습니다..").append("\n");
            str.append("수정한 정보를 다시 확인해주세요.");
            JOptionPane.showMessageDialog(YogioGUI.this, str.toString());
        }
    }

    // 관리자 로그인 메소드
    private void adminLogin() {
        str = new StringBuilder();
        // 텍스트창에서 입력받은 관리자 ID
        String adminId = adminidv.getText().trim();
        // 텍스트창에서 입력받은 관리자 PW
        String adminPw = new String(adminpwv.getPassword());
        // 관리자 login 메소드
        Map<String, String> map = model.loginAdmin(adminId, adminPw);
        adminName = map.get("adminname");
        // 검색과 일치하는 유저이름 변수저장.
        if (map.get("cnt").equals("1")) {
            str.append(adminName).append(" 님이 접속하셨습니다.");
            JOptionPane.showMessageDialog(YogioGUI.this, str.toString());
            card.show(pp, "adminMenuC");
        } else {
            str.append("관리자 정보가 존재하지 않습니다.");
            JOptionPane.showMessageDialog(YogioGUI.this, str.toString());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        pp = new javax.swing.JPanel();
        logInP = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();
        id = new javax.swing.JLabel();
        logInId_v = new javax.swing.JTextField();
        pw = new javax.swing.JLabel();
        logInPw_v = new javax.swing.JPasswordField();
        logInBtn = new javax.swing.JButton();
        signInBtn = new javax.swing.JButton();
        adminPageBtn = new javax.swing.JButton();
        signInP = new javax.swing.JPanel();
        custIdL = new javax.swing.JLabel();
        signIdv = new javax.swing.JTextField();
        idChkBtn = new javax.swing.JButton();
        custPw = new javax.swing.JLabel();
        signPwv = new javax.swing.JPasswordField();
        custName = new javax.swing.JLabel();
        signNamev = new javax.swing.JTextField();
        custAge = new javax.swing.JLabel();
        signAgev = new javax.swing.JTextField();
        custGender = new javax.swing.JLabel();
        signGenderv = new javax.swing.JTextField();
        custAddr = new javax.swing.JLabel();
        signAddrv = new javax.swing.JTextField();
        custTel = new javax.swing.JLabel();
        signTelv = new javax.swing.JTextField();
        insertBtn = new javax.swing.JButton();
        backLogBtn = new javax.swing.JButton();
        updateP = new javax.swing.JPanel();
        jLabel_update_id = new javax.swing.JLabel();
        updateidv = new javax.swing.JTextField();
        jLabel_update_pw = new javax.swing.JLabel();
        updatepwv = new javax.swing.JPasswordField();
        jLabel_update_name = new javax.swing.JLabel();
        updatenamev = new javax.swing.JTextField();
        jLabel_update_age = new javax.swing.JLabel();
        updateagev = new javax.swing.JTextField();
        jLabel_update_gender = new javax.swing.JLabel();
        updategenderv = new javax.swing.JTextField();
        jLabel_update_addr = new javax.swing.JLabel();
        updateaddv = new javax.swing.JTextField();
        jLabel_update_tel = new javax.swing.JLabel();
        updatetelv = new javax.swing.JTextField();
        updateBtn = new javax.swing.JButton();
        backMenuBtn = new javax.swing.JButton();
        orderMenuP = new javax.swing.JPanel();
        orderMenuSplit = new javax.swing.JSplitPane();
        upP = new javax.swing.JPanel();
        korFoodBtn = new javax.swing.JButton();
        chnFoodBtn = new javax.swing.JButton();
        jpnFoodBtn = new javax.swing.JButton();
        myInfoBtn = new javax.swing.JButton();
        orderListBtn = new javax.swing.JButton();
        nowloginId = new javax.swing.JTextField();
        downP = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        foodTable = new javax.swing.JTable();
        foodDetailBtn = new javax.swing.JButton();
        orderBtn = new javax.swing.JButton();
        orderP = new javax.swing.JPanel();
        orderL = new javax.swing.JLabel();
        orderCustIdv = new javax.swing.JTextField();
        foodnumL = new javax.swing.JLabel();
        orderFoodNumv = new javax.swing.JTextField();
        foodnameL = new javax.swing.JLabel();
        orderFoodNamev = new javax.swing.JTextField();
        foodpriceL = new javax.swing.JLabel();
        orderFoodPricev = new javax.swing.JTextField();
        orderimage = new javax.swing.JLabel();
        orderCustNameL = new javax.swing.JLabel();
        orderCustNamev = new javax.swing.JTextField();
        orderCustTelL = new javax.swing.JLabel();
        orderCustTelv = new javax.swing.JTextField();
        orderCustAddrL = new javax.swing.JLabel();
        orderCustAddrv = new javax.swing.JTextField();
        cancelOrderBtn = new javax.swing.JButton();
        confirmOrderBtn = new javax.swing.JButton();
        orderListP = new javax.swing.JPanel();
        custNowId = new javax.swing.JTextField();
        titleL = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listTable = new javax.swing.JTable();
        listBackBtn = new javax.swing.JButton();
        adminP = new javax.swing.JPanel();
        adminIdL = new javax.swing.JLabel();
        adminPwL = new javax.swing.JLabel();
        adminidv = new javax.swing.JTextField();
        adminpwv = new javax.swing.JPasswordField();
        adminLogInBtn = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        adminMenuP = new javax.swing.JPanel();
        adminMenuSplit = new javax.swing.JSplitPane();
        adminUp = new javax.swing.JPanel();
        nowAdminId = new javax.swing.JTextField();
        backFirstBtn = new javax.swing.JButton();
        adminListBtn = new javax.swing.JButton();
        salesFoodBtn = new javax.swing.JButton();
        adminDown = new javax.swing.JPanel();
        adminListP = new javax.swing.JPanel();
        adminListSplit = new javax.swing.JScrollPane();
        adminTable = new javax.swing.JTable();
        combo = new javax.swing.JComboBox<>();
        searchv = new javax.swing.JTextField();
        printAdminBtn = new javax.swing.JButton();
        salesFoodP = new javax.swing.JPanel();
        salesFoodSplit = new javax.swing.JScrollPane();
        salesTable = new javax.swing.JTable();
        calSalesTotalBtn = new javax.swing.JButton();
        total = new javax.swing.JTextField();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        pp.setBackground(new java.awt.Color(255, 255, 255));
        pp.setForeground(new java.awt.Color(0, 0, 0));
        pp.setPreferredSize(new java.awt.Dimension(650, 800));
        pp.setLayout(new java.awt.CardLayout());

        logInP.setBackground(new java.awt.Color(255, 255, 255));

        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logo.png"))); // NOI18N

        id.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        id.setForeground(new java.awt.Color(0, 0, 0));
        id.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        id.setText("아이디 : ");
        id.setPreferredSize(new java.awt.Dimension(85, 30));

        logInId_v.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logInId_vActionPerformed(evt);
            }
        });

        pw.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        pw.setForeground(new java.awt.Color(0, 0, 0));
        pw.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pw.setText("비밀번호 : ");
        pw.setPreferredSize(new java.awt.Dimension(85, 30));

        logInBtn.setText("log in");
        logInBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logInBtnActionPerformed(evt);
            }
        });

        signInBtn.setText("sign in");
        signInBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signInBtnActionPerformed(evt);
            }
        });

        adminPageBtn.setText("admin");

        javax.swing.GroupLayout logInPLayout = new javax.swing.GroupLayout(logInP);
        logInP.setLayout(logInPLayout);
        logInPLayout.setHorizontalGroup(
            logInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logInPLayout.createSequentialGroup()
                .addGroup(logInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(logInPLayout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(logInPLayout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addGroup(logInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pw, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(logInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(logInId_v)
                            .addComponent(logInPw_v, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))
                        .addGap(78, 78, 78)
                        .addGroup(logInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(signInBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(logInBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(98, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, logInPLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(adminPageBtn)
                .addGap(22, 22, 22))
        );
        logInPLayout.setVerticalGroup(
            logInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logInPLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(adminPageBtn)
                .addGap(18, 18, 18)
                .addComponent(logo)
                .addGap(18, 18, 18)
                .addGroup(logInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(logInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(logInBtn))
                    .addComponent(logInId_v, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(logInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(logInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pw, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(logInPw_v, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(signInBtn))
                .addContainerGap(478, Short.MAX_VALUE))
        );

        pp.add(logInP, "logInC");

        signInP.setBackground(new java.awt.Color(204, 204, 255));

        custIdL.setForeground(new java.awt.Color(0, 0, 0));
        custIdL.setText("아이디 :");

        idChkBtn.setText("중복체크");
        idChkBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idChkBtnActionPerformed(evt);
            }
        });

        custPw.setForeground(new java.awt.Color(0, 0, 0));
        custPw.setText("비밀번호 :");

        signPwv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signPwvActionPerformed(evt);
            }
        });

        custName.setForeground(new java.awt.Color(0, 0, 0));
        custName.setText("이름 : ");

        signNamev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signNamevActionPerformed(evt);
            }
        });

        custAge.setForeground(new java.awt.Color(0, 0, 0));
        custAge.setText("나이 : ");

        custGender.setForeground(new java.awt.Color(0, 0, 0));
        custGender.setText("성별 : ");

        custAddr.setForeground(new java.awt.Color(0, 0, 0));
        custAddr.setText("주소 : ");

        custTel.setForeground(new java.awt.Color(0, 0, 0));
        custTel.setText("연락처 : ");

        signTelv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signTelvActionPerformed(evt);
            }
        });

        insertBtn.setText("회원가입");
        insertBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertBtnActionPerformed(evt);
            }
        });

        backLogBtn.setText("로그인 화면으로");
        backLogBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backLogBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout signInPLayout = new javax.swing.GroupLayout(signInP);
        signInP.setLayout(signInPLayout);
        signInPLayout.setHorizontalGroup(
            signInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(signInPLayout.createSequentialGroup()
                .addGap(118, 118, 118)
                .addGroup(signInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(signInPLayout.createSequentialGroup()
                        .addComponent(insertBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(backLogBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(signInPLayout.createSequentialGroup()
                        .addGroup(signInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(custAddr)
                            .addComponent(custPw)
                            .addComponent(custIdL)
                            .addComponent(custGender)
                            .addComponent(custAge)
                            .addComponent(custName)
                            .addComponent(custTel))
                        .addGap(18, 18, 18)
                        .addGroup(signInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(signPwv)
                            .addComponent(signNamev)
                            .addComponent(signAgev)
                            .addComponent(signGenderv)
                            .addComponent(signAddrv)
                            .addComponent(signTelv)
                            .addComponent(signIdv, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(97, 97, 97)
                        .addComponent(idChkBtn)))
                .addContainerGap(122, Short.MAX_VALUE))
        );
        signInPLayout.setVerticalGroup(
            signInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(signInPLayout.createSequentialGroup()
                .addGap(203, 203, 203)
                .addGroup(signInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(idChkBtn)
                    .addGroup(signInPLayout.createSequentialGroup()
                        .addGroup(signInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(custIdL)
                            .addComponent(signIdv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(signInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(custPw)
                            .addComponent(signPwv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(signInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(custName)
                            .addComponent(signNamev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(signInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(custAge)
                            .addComponent(signAgev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(signInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(custGender)
                            .addComponent(signGenderv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(signInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(custAddr)
                            .addComponent(signAddrv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(signInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(custTel)
                            .addComponent(signTelv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                .addGroup(signInPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backLogBtn)
                    .addComponent(insertBtn))
                .addGap(213, 213, 213))
        );

        pp.add(signInP, "signInC");

        updateP.setBackground(new java.awt.Color(255, 204, 204));
        updateP.setPreferredSize(new java.awt.Dimension(650, 800));

        jLabel_update_id.setFont(new java.awt.Font("돋움", 0, 15)); // NOI18N
        jLabel_update_id.setText("아이디 : ");

        updateidv.setEditable(false);

        jLabel_update_pw.setFont(new java.awt.Font("돋움", 0, 15)); // NOI18N
        jLabel_update_pw.setText("비밀번호 : ");

        updatepwv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatepwvActionPerformed(evt);
            }
        });

        jLabel_update_name.setFont(new java.awt.Font("돋움", 0, 15)); // NOI18N
        jLabel_update_name.setText("이름 : ");

        jLabel_update_age.setFont(new java.awt.Font("돋움", 0, 15)); // NOI18N
        jLabel_update_age.setText("나이 : ");

        updateagev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateagevActionPerformed(evt);
            }
        });

        jLabel_update_gender.setFont(new java.awt.Font("돋움", 0, 15)); // NOI18N
        jLabel_update_gender.setText("성별 : ");

        updategenderv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updategendervActionPerformed(evt);
            }
        });

        jLabel_update_addr.setFont(new java.awt.Font("돋움", 0, 15)); // NOI18N
        jLabel_update_addr.setText("주소 : ");

        jLabel_update_tel.setFont(new java.awt.Font("돋움", 0, 15)); // NOI18N
        jLabel_update_tel.setText("전화번호 : ");

        updateBtn.setText("정보수정");

        backMenuBtn.setText("주문화면으로");

        javax.swing.GroupLayout updatePLayout = new javax.swing.GroupLayout(updateP);
        updateP.setLayout(updatePLayout);
        updatePLayout.setHorizontalGroup(
            updatePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatePLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(updatePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel_update_tel)
                    .addComponent(jLabel_update_gender)
                    .addComponent(jLabel_update_addr)
                    .addComponent(jLabel_update_age)
                    .addComponent(jLabel_update_pw)
                    .addComponent(jLabel_update_name)
                    .addComponent(jLabel_update_id))
                .addGap(30, 30, 30)
                .addGroup(updatePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(updateidv, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updatenamev, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(updatePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(updatepwv, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(updatetelv, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                        .addComponent(updateagev, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                        .addComponent(updategenderv, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                        .addComponent(updateaddv, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)))
                .addContainerGap(205, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updatePLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(backMenuBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );
        updatePLayout.setVerticalGroup(
            updatePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatePLayout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addGroup(updatePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_update_id)
                    .addComponent(updateidv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(updatePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_update_pw)
                    .addComponent(updatepwv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(updatePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_update_name)
                    .addComponent(updatenamev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(updatePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_update_age)
                    .addComponent(updateagev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(updatePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_update_gender)
                    .addComponent(updategenderv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(updatePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_update_addr)
                    .addComponent(updateaddv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(updatePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_update_tel)
                    .addComponent(updatetelv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(90, 90, 90)
                .addGroup(updatePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backMenuBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(272, Short.MAX_VALUE))
        );

        updateidv.getAccessibleContext().setAccessibleName("");
        updateidv.getAccessibleContext().setAccessibleDescription("");

        pp.add(updateP, "updateC");

        orderMenuP.setBackground(new java.awt.Color(204, 204, 255));

        orderMenuSplit.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        upP.setBackground(new java.awt.Color(255, 51, 51));

        korFoodBtn.setBackground(new java.awt.Color(255, 255, 255));
        korFoodBtn.setForeground(new java.awt.Color(0, 0, 0));
        korFoodBtn.setText("한식");
        korFoodBtn.setMaximumSize(new java.awt.Dimension(60, 40));
        korFoodBtn.setMinimumSize(new java.awt.Dimension(60, 40));

        chnFoodBtn.setBackground(new java.awt.Color(255, 255, 255));
        chnFoodBtn.setForeground(new java.awt.Color(0, 0, 0));
        chnFoodBtn.setText("중식");

        jpnFoodBtn.setBackground(new java.awt.Color(255, 255, 255));
        jpnFoodBtn.setForeground(new java.awt.Color(0, 0, 0));
        jpnFoodBtn.setText("일식");

        myInfoBtn.setBackground(new java.awt.Color(255, 255, 255));
        myInfoBtn.setForeground(new java.awt.Color(0, 0, 0));
        myInfoBtn.setText("내 정보 수정");

        orderListBtn.setBackground(new java.awt.Color(255, 255, 255));
        orderListBtn.setForeground(new java.awt.Color(0, 0, 0));
        orderListBtn.setText("주문내역확인");

        javax.swing.GroupLayout upPLayout = new javax.swing.GroupLayout(upP);
        upP.setLayout(upPLayout);
        upPLayout.setHorizontalGroup(
            upPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, upPLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(upPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(upPLayout.createSequentialGroup()
                        .addComponent(korFoodBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                        .addComponent(chnFoodBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57)
                        .addComponent(jpnFoodBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE))
                    .addGroup(upPLayout.createSequentialGroup()
                        .addComponent(nowloginId, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(upPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(myInfoBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(orderListBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                .addGap(0, 20, Short.MAX_VALUE))
        );
        upPLayout.setVerticalGroup(
            upPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, upPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(upPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(myInfoBtn)
                    .addComponent(nowloginId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(upPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(korFoodBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpnFoodBtn)
                    .addComponent(chnFoodBtn)
                    .addComponent(orderListBtn))
                .addGap(15, 15, 15))
        );

        orderMenuSplit.setTopComponent(upP);

        downP.setBackground(new java.awt.Color(204, 204, 255));

        foodTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(foodTable);

        foodDetailBtn.setText("상세보기");

        orderBtn.setText("주문하기");

        javax.swing.GroupLayout downPLayout = new javax.swing.GroupLayout(downP);
        downP.setLayout(downPLayout);
        downPLayout.setHorizontalGroup(
            downPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, downPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(foodDetailBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(orderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        downPLayout.setVerticalGroup(
            downPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(downPLayout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addGroup(downPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(foodDetailBtn)
                    .addComponent(orderBtn))
                .addContainerGap(55, Short.MAX_VALUE))
        );

        orderMenuSplit.setRightComponent(downP);

        javax.swing.GroupLayout orderMenuPLayout = new javax.swing.GroupLayout(orderMenuP);
        orderMenuP.setLayout(orderMenuPLayout);
        orderMenuPLayout.setHorizontalGroup(
            orderMenuPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(orderMenuSplit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        orderMenuPLayout.setVerticalGroup(
            orderMenuPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(orderMenuSplit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pp.add(orderMenuP, "orderMenuC");

        orderP.setBackground(new java.awt.Color(204, 255, 255));
        orderP.setPreferredSize(new java.awt.Dimension(650, 800));

        orderL.setFont(new java.awt.Font("돋움", 1, 24)); // NOI18N
        orderL.setForeground(new java.awt.Color(0, 0, 0));
        orderL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        orderL.setText("< 주문하기 >");

        orderCustIdv.setEditable(false);

        foodnumL.setForeground(new java.awt.Color(0, 0, 0));
        foodnumL.setText("메뉴번호 : ");

        foodnameL.setForeground(new java.awt.Color(0, 0, 0));
        foodnameL.setText("주문메뉴 : ");

        foodpriceL.setForeground(new java.awt.Color(0, 0, 0));
        foodpriceL.setText("음식가격 : ");

        orderFoodPricev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderFoodPricevActionPerformed(evt);
            }
        });

        orderCustNameL.setForeground(new java.awt.Color(0, 0, 0));
        orderCustNameL.setText("주문자 : ");

        orderCustTelL.setForeground(new java.awt.Color(0, 0, 0));
        orderCustTelL.setText("연락받으실 번호 :");

        orderCustAddrL.setForeground(new java.awt.Color(0, 0, 0));
        orderCustAddrL.setText("배달받으실 주소 : ");

        cancelOrderBtn.setText("돌아가기");

        confirmOrderBtn.setText("결제하기");

        javax.swing.GroupLayout orderPLayout = new javax.swing.GroupLayout(orderP);
        orderP.setLayout(orderPLayout);
        orderPLayout.setHorizontalGroup(
            orderPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderPLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(orderPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(orderPLayout.createSequentialGroup()
                        .addGroup(orderPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(orderCustNameL)
                            .addComponent(orderCustTelL))
                        .addGap(18, 18, 18)
                        .addGroup(orderPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(orderCustNamev, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(orderCustTelv, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, orderPLayout.createSequentialGroup()
                        .addGroup(orderPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(orderPLayout.createSequentialGroup()
                                .addComponent(orderCustAddrL)
                                .addGap(18, 18, 18)
                                .addComponent(orderCustAddrv))
                            .addGroup(orderPLayout.createSequentialGroup()
                                .addGroup(orderPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(orderPLayout.createSequentialGroup()
                                        .addComponent(foodnumL)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(orderFoodNumv, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(orderPLayout.createSequentialGroup()
                                        .addComponent(foodnameL)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(orderFoodNamev))
                                    .addGroup(orderPLayout.createSequentialGroup()
                                        .addComponent(foodpriceL)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(orderFoodPricev, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                                .addComponent(orderimage, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(60, 60, 60))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, orderPLayout.createSequentialGroup()
                        .addGroup(orderPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(orderPLayout.createSequentialGroup()
                                .addComponent(cancelOrderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(confirmOrderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(orderPLayout.createSequentialGroup()
                                .addComponent(orderL, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(orderCustIdv, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(39, 39, 39))))
        );
        orderPLayout.setVerticalGroup(
            orderPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderPLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(orderPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(orderCustIdv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(orderL))
                .addGap(72, 72, 72)
                .addGroup(orderPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(orderimage, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(orderPLayout.createSequentialGroup()
                        .addGroup(orderPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(foodnumL)
                            .addComponent(orderFoodNumv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(orderPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(foodnameL)
                            .addComponent(orderFoodNamev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(orderPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(foodpriceL)
                            .addComponent(orderFoodPricev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(27, 27, 27)
                .addGroup(orderPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(orderCustNameL)
                    .addComponent(orderCustNamev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(orderPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(orderCustTelL)
                    .addComponent(orderCustTelv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(orderPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(orderCustAddrL)
                    .addComponent(orderCustAddrv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 213, Short.MAX_VALUE)
                .addGroup(orderPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cancelOrderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmOrderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );

        pp.add(orderP, "orderc");

        orderListP.setBackground(new java.awt.Color(255, 204, 255));

        custNowId.setEditable(false);

        titleL.setForeground(new java.awt.Color(0, 0, 0));
        titleL.setText("님의 주문 내역");

        listTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(listTable);

        listBackBtn.setText("돌아가기");
        listBackBtn.setToolTipText("");

        javax.swing.GroupLayout orderListPLayout = new javax.swing.GroupLayout(orderListP);
        orderListP.setLayout(orderListPLayout);
        orderListPLayout.setHorizontalGroup(
            orderListPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderListPLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(orderListPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(listBackBtn)
                    .addGroup(orderListPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(orderListPLayout.createSequentialGroup()
                            .addComponent(custNowId, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(titleL, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        orderListPLayout.setVerticalGroup(
            orderListPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderListPLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(orderListPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(custNowId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(titleL))
                .addGap(62, 62, 62)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                .addComponent(listBackBtn)
                .addGap(65, 65, 65))
        );

        pp.add(orderListP, "orderListc");

        adminP.setBackground(new java.awt.Color(255, 255, 255));

        adminIdL.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        adminIdL.setForeground(new java.awt.Color(0, 0, 0));
        adminIdL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        adminIdL.setText("아이디 : ");
        adminIdL.setPreferredSize(new java.awt.Dimension(85, 30));

        adminPwL.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        adminPwL.setForeground(new java.awt.Color(0, 0, 0));
        adminPwL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        adminPwL.setText("비밀번호 : ");
        adminPwL.setPreferredSize(new java.awt.Dimension(85, 30));

        adminidv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminidvActionPerformed(evt);
            }
        });

        adminLogInBtn.setText("log in");
        adminLogInBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminLogInBtnActionPerformed(evt);
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/admin_icon.png"))); // NOI18N

        javax.swing.GroupLayout adminPLayout = new javax.swing.GroupLayout(adminP);
        adminP.setLayout(adminPLayout);
        adminPLayout.setHorizontalGroup(
            adminPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminPLayout.createSequentialGroup()
                .addContainerGap(117, Short.MAX_VALUE)
                .addGroup(adminPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, adminPLayout.createSequentialGroup()
                        .addGroup(adminPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(adminPwL, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(adminIdL, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(adminPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(adminidv)
                            .addComponent(adminpwv, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(77, 77, 77)
                        .addComponent(adminLogInBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, adminPLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(193, 193, 193))))
        );
        adminPLayout.setVerticalGroup(
            adminPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, adminPLayout.createSequentialGroup()
                .addContainerGap(151, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(47, 47, 47)
                .addGroup(adminPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(adminPLayout.createSequentialGroup()
                        .addGroup(adminPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(adminIdL, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(adminidv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(adminPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(adminPwL, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(adminpwv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(adminPLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(adminLogInBtn)))
                .addGap(274, 274, 274))
        );

        pp.add(adminP, "adminLoginC");

        adminMenuP.setBackground(new java.awt.Color(255, 255, 255));
        adminMenuP.setPreferredSize(new java.awt.Dimension(650, 800));

        adminMenuSplit.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        adminUp.setBackground(new java.awt.Color(255, 153, 255));

        nowAdminId.setEditable(false);

        backFirstBtn.setText("초기화면");

        adminListBtn.setBackground(new java.awt.Color(255, 153, 255));
        adminListBtn.setForeground(new java.awt.Color(255, 255, 255));
        adminListBtn.setText("관리자목록 출력");

        salesFoodBtn.setBackground(new java.awt.Color(255, 153, 255));
        salesFoodBtn.setForeground(new java.awt.Color(255, 255, 255));
        salesFoodBtn.setText("매출내역확인");

        javax.swing.GroupLayout adminUpLayout = new javax.swing.GroupLayout(adminUp);
        adminUp.setLayout(adminUpLayout);
        adminUpLayout.setHorizontalGroup(
            adminUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminUpLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(adminUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(adminListBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(nowAdminId))
                .addGroup(adminUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(adminUpLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(backFirstBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22))
                    .addGroup(adminUpLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(salesFoodBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(309, Short.MAX_VALUE))))
        );
        adminUpLayout.setVerticalGroup(
            adminUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminUpLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(adminUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nowAdminId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(backFirstBtn))
                .addGap(16, 16, 16)
                .addGroup(adminUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(adminListBtn)
                    .addComponent(salesFoodBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        adminMenuSplit.setTopComponent(adminUp);

        adminDown.setBackground(new java.awt.Color(255, 255, 255));
        adminDown.setPreferredSize(new java.awt.Dimension(650, 800));
        adminDown.setLayout(new java.awt.CardLayout());

        adminListP.setBackground(new java.awt.Color(255, 255, 255));
        adminListP.setPreferredSize(new java.awt.Dimension(650, 800));

        adminTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        adminListSplit.setViewportView(adminTable);

        combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "관리자ID", "관리자명", "직책" }));

        printAdminBtn.setBackground(new java.awt.Color(204, 204, 204));
        printAdminBtn.setForeground(new java.awt.Color(0, 0, 0));
        printAdminBtn.setText("검색");

        javax.swing.GroupLayout adminListPLayout = new javax.swing.GroupLayout(adminListP);
        adminListP.setLayout(adminListPLayout);
        adminListPLayout.setHorizontalGroup(
            adminListPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminListPLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(adminListPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(adminListSplit, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(adminListPLayout.createSequentialGroup()
                        .addComponent(combo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(searchv, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(printAdminBtn)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        adminListPLayout.setVerticalGroup(
            adminListPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminListPLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(adminListSplit, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addGroup(adminListPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(printAdminBtn))
                .addContainerGap(87, Short.MAX_VALUE))
        );

        adminDown.add(adminListP, "adminListC");
        adminListP.getAccessibleContext().setAccessibleName("");

        salesFoodP.setBackground(new java.awt.Color(255, 255, 255));
        salesFoodP.setPreferredSize(new java.awt.Dimension(650, 800));

        salesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        salesFoodSplit.setViewportView(salesTable);

        calSalesTotalBtn.setBackground(new java.awt.Color(204, 204, 204));
        calSalesTotalBtn.setForeground(new java.awt.Color(0, 0, 0));
        calSalesTotalBtn.setText("매출합계");

        total.setEditable(false);

        javax.swing.GroupLayout salesFoodPLayout = new javax.swing.GroupLayout(salesFoodP);
        salesFoodP.setLayout(salesFoodPLayout);
        salesFoodPLayout.setHorizontalGroup(
            salesFoodPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(salesFoodPLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(salesFoodPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(salesFoodSplit, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(salesFoodPLayout.createSequentialGroup()
                        .addComponent(calSalesTotalBtn)
                        .addGap(18, 18, 18)
                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        salesFoodPLayout.setVerticalGroup(
            salesFoodPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(salesFoodPLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(salesFoodSplit, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addGroup(salesFoodPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(calSalesTotalBtn)
                    .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(83, Short.MAX_VALUE))
        );

        adminDown.add(salesFoodP, "salesFoodC");
        salesFoodP.getAccessibleContext().setAccessibleName("");

        adminMenuSplit.setRightComponent(adminDown);

        javax.swing.GroupLayout adminMenuPLayout = new javax.swing.GroupLayout(adminMenuP);
        adminMenuP.setLayout(adminMenuPLayout);
        adminMenuPLayout.setHorizontalGroup(
            adminMenuPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(adminMenuSplit, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        adminMenuPLayout.setVerticalGroup(
            adminMenuPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(adminMenuSplit, javax.swing.GroupLayout.PREFERRED_SIZE, 818, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pp.add(adminMenuP, "adminMenuC");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logInBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logInBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_logInBtnActionPerformed

    private void signInBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signInBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_signInBtnActionPerformed

    private void signNamevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signNamevActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_signNamevActionPerformed

    private void idChkBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idChkBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idChkBtnActionPerformed

    private void signTelvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signTelvActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_signTelvActionPerformed

    private void insertBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_insertBtnActionPerformed

    private void updatepwvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatepwvActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updatepwvActionPerformed

    private void updateagevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateagevActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updateagevActionPerformed

    private void updategendervActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updategendervActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updategendervActionPerformed

    private void backLogBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backLogBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_backLogBtnActionPerformed

    private void logInId_vActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logInId_vActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_logInId_vActionPerformed

    private void signPwvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signPwvActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_signPwvActionPerformed

    private void orderFoodPricevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderFoodPricevActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_orderFoodPricevActionPerformed

    private void adminLogInBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminLogInBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_adminLogInBtnActionPerformed

    private void adminidvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminidvActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_adminidvActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(YogioGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(YogioGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(YogioGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(YogioGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new YogioGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel adminDown;
    private javax.swing.JLabel adminIdL;
    private javax.swing.JButton adminListBtn;
    private javax.swing.JPanel adminListP;
    private javax.swing.JScrollPane adminListSplit;
    private javax.swing.JButton adminLogInBtn;
    private javax.swing.JPanel adminMenuP;
    private javax.swing.JSplitPane adminMenuSplit;
    private javax.swing.JPanel adminP;
    private javax.swing.JButton adminPageBtn;
    private javax.swing.JLabel adminPwL;
    private javax.swing.JTable adminTable;
    private javax.swing.JPanel adminUp;
    private javax.swing.JTextField adminidv;
    private javax.swing.JPasswordField adminpwv;
    private javax.swing.JButton backFirstBtn;
    private javax.swing.JButton backLogBtn;
    private javax.swing.JButton backMenuBtn;
    private javax.swing.JButton calSalesTotalBtn;
    private javax.swing.JButton cancelOrderBtn;
    private javax.swing.JButton chnFoodBtn;
    private javax.swing.JComboBox<String> combo;
    private javax.swing.JButton confirmOrderBtn;
    private javax.swing.JLabel custAddr;
    private javax.swing.JLabel custAge;
    private javax.swing.JLabel custGender;
    private javax.swing.JLabel custIdL;
    private javax.swing.JLabel custName;
    private javax.swing.JTextField custNowId;
    private javax.swing.JLabel custPw;
    private javax.swing.JLabel custTel;
    private javax.swing.JPanel downP;
    private javax.swing.JButton foodDetailBtn;
    private javax.swing.JTable foodTable;
    private javax.swing.JLabel foodnameL;
    private javax.swing.JLabel foodnumL;
    private javax.swing.JLabel foodpriceL;
    private javax.swing.JLabel id;
    private javax.swing.JButton idChkBtn;
    private javax.swing.JButton insertBtn;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel_update_addr;
    private javax.swing.JLabel jLabel_update_age;
    private javax.swing.JLabel jLabel_update_gender;
    private javax.swing.JLabel jLabel_update_id;
    private javax.swing.JLabel jLabel_update_name;
    private javax.swing.JLabel jLabel_update_pw;
    private javax.swing.JLabel jLabel_update_tel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton jpnFoodBtn;
    private javax.swing.JButton korFoodBtn;
    private javax.swing.JButton listBackBtn;
    private javax.swing.JTable listTable;
    private javax.swing.JButton logInBtn;
    private javax.swing.JTextField logInId_v;
    private javax.swing.JPanel logInP;
    private javax.swing.JPasswordField logInPw_v;
    private javax.swing.JLabel logo;
    private javax.swing.JButton myInfoBtn;
    private javax.swing.JTextField nowAdminId;
    private javax.swing.JTextField nowloginId;
    private javax.swing.JButton orderBtn;
    private javax.swing.JLabel orderCustAddrL;
    private javax.swing.JTextField orderCustAddrv;
    private javax.swing.JTextField orderCustIdv;
    private javax.swing.JLabel orderCustNameL;
    private javax.swing.JTextField orderCustNamev;
    private javax.swing.JLabel orderCustTelL;
    private javax.swing.JTextField orderCustTelv;
    private javax.swing.JTextField orderFoodNamev;
    private javax.swing.JTextField orderFoodNumv;
    private javax.swing.JTextField orderFoodPricev;
    private javax.swing.JLabel orderL;
    private javax.swing.JButton orderListBtn;
    private javax.swing.JPanel orderListP;
    private javax.swing.JPanel orderMenuP;
    private javax.swing.JSplitPane orderMenuSplit;
    private javax.swing.JPanel orderP;
    private javax.swing.JLabel orderimage;
    private javax.swing.JPanel pp;
    private javax.swing.JButton printAdminBtn;
    private javax.swing.JLabel pw;
    private javax.swing.JButton salesFoodBtn;
    private javax.swing.JPanel salesFoodP;
    private javax.swing.JScrollPane salesFoodSplit;
    private javax.swing.JTable salesTable;
    private javax.swing.JTextField searchv;
    private javax.swing.JTextField signAddrv;
    private javax.swing.JTextField signAgev;
    private javax.swing.JTextField signGenderv;
    private javax.swing.JTextField signIdv;
    private javax.swing.JButton signInBtn;
    private javax.swing.JPanel signInP;
    private javax.swing.JTextField signNamev;
    private javax.swing.JPasswordField signPwv;
    private javax.swing.JTextField signTelv;
    private javax.swing.JLabel titleL;
    private javax.swing.JTextField total;
    private javax.swing.JPanel upP;
    private javax.swing.JButton updateBtn;
    private javax.swing.JPanel updateP;
    private javax.swing.JTextField updateaddv;
    private javax.swing.JTextField updateagev;
    private javax.swing.JTextField updategenderv;
    private javax.swing.JTextField updateidv;
    private javax.swing.JTextField updatenamev;
    private javax.swing.JPasswordField updatepwv;
    private javax.swing.JTextField updatetelv;
    // End of variables declaration//GEN-END:variables
}
