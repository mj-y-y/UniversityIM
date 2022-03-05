package com.company;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Pattern;

import static com.company.Student.isDig;

class teacherSql extends dataProcessing {

    @Override
    boolean addData(String ss[],String UTR,String NAME,String PASSWORD) {
        try{//加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");//加载驱动
            Connection coon = DriverManager.getConnection(UTR,NAME,PASSWORD);//创建连接对象

            String sql=" INSERT  INTO  tbl_teacher   VALUES  ('"+ss[0]+"','"+ss[1]+"','"+ss[2]+"','"+ss[3]+"','"+ss[4]+"','"+ss[5]+"')";
            //建立查询语句
            Statement stmt = coon.createStatement();
            int  result = stmt.executeUpdate(sql);
            coon.close();
            stmt.close();

        }catch (Exception e1){//如何判断学号已经有了？？
            e1.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    boolean deleteData(String ss[],String UTR,String NAME,String PASSWORD) {
        int result = 0;
        try{//加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");//加载驱动
            Connection coon = DriverManager.getConnection(UTR,NAME,PASSWORD);//创建连接对象

            String sql="delete from tbl_teacher where Tid='"+ss[0]+"'";
            System.out.println(sql);
            Statement stmt = coon.createStatement();
            result = stmt.executeUpdate(sql);

            coon.close();
            stmt.close();
        }catch (Exception e1){

            e1.printStackTrace();
            return false;
        }
        return result != 0;
    }

    @Override
    boolean updateData(String ss[],String UTR, String NAME, String PASSWORD) {
        try{//加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");//加载驱动
            Connection coon = DriverManager.getConnection(UTR,NAME,PASSWORD);//创建连接对象

            String sql=" UPDATE tbl_teacher SET Tname='"+ss[1]+"',Tsex='"+ss[2]+"',dept='"+ss[3]+"',phone='"+ss[4]+"', "
                    + " address='"+ss[5]+"' where Tid='"+ss[0]+"' ";
            Statement stmt = coon.createStatement();
            int  result = stmt.executeUpdate(sql);
            coon.close();
            stmt.close();
            if(result > 0) {
                return true;
            }else {
                return false;
            }


        }catch (Exception e1){
            e1.printStackTrace();
        }
        return true;
    }


    @Override
    Object[][] findData(String ss[],String UTR,String NAME,String PASSWORD) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection coon = DriverManager.getConnection(UTR, NAME, PASSWORD);//创建连接对象
            String sql = "select * from tbl_teacher where ";
            boolean flag = false;

            //全空就查询所有数据
            if (ss[0].equals("") && ss[1].equals("") && ss[2].equals("") && ss[3].equals("请选择学院") && ss[4].equals("") && ss[5].equals("")) {
                System.out.println("查询所有老师信息");
                sql = " select * from tbl_teacher ;";
            }
            if (!ss[0].equals("") && isDig(ss[0])) {//学号是数字 就按学号查询
                //System.out.println("按学号查询");
                sql += " Tid = " + "'" + ss[0] + "'";
                flag = true;
            }
            if (!ss[1].equals("")) {//姓名不为空时 按姓名查询
                if (flag) {
                    sql += " AND ";
                }
                flag = true;
                sql += " Tname like " + "'" + "%" + ss[1] + "%" + "'";
            }
            if (ss[2].equals("男") || ss[2].equals("女")) {
                if (flag) {
                    sql += " AND ";
                }
                flag = true;
                sql += " Tsex = " + "'" + ss[2] + "'";
            }
            if (!ss[3].equals("请选择学院")) {//班级有效
                if (flag) {
                    sql += " AND ";
                }
                flag = true;
                sql += " dept = " + "'" + ss[3] + "'";
            }
            if (isDig(ss[4]) && ss[4].length() == 11) {//如果有电话号码

                if (flag) {
                    sql += " AND";
                }
                flag = true;
                sql += " phone = " + "'" + ss[4] + "'";
            }
            if (!ss[5].equals("")) {//如果有电话号码

                if (flag) {
                    sql += " AND ";
                }
                flag = true;
                sql += " address = " + "'" + ss[5] + "'";
            }

//            System.out.println(sql + "查询结果");
            Statement stmt = coon.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;
            while(rs.next()){
                count++;
            }

            Object[][] info = new Object[count][6];
            count = 0;
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                info[count][0] = rs.getString("Tid");
                info[count][1] = rs.getString("Tname");
                info[count][2] = rs.getString("Tsex");
                info[count][3] = rs.getString("dept");
                info[count][4] = rs.getString("phone");
                info[count][5] = rs.getString("address");
                count++;
            }
            return info;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}



public class Teacher implements ActionListener {
    public JFrame f=new JFrame("教师工信息管理系统");
    private JButton re=new JButton("返回");
    static private String UTR = "jdbc:mysql://localhost:3306/university_im?"
            + "useSSL=false&serverTimezone=UTC&characterEncoding=UTF8&allowPublicKeyRetrieval=true";
    static private String NAME = "root";
    static private String PASSWORD = "applemysql";

    //判断添加信息是否合法
    public static boolean judge(String tid, String name, String sex, String deptnumber, String phone, String address) {
        if (tid.equals("") || name.equals("") || sex.equals("") || deptnumber.equals("请选择学院") || phone.equals("") || address.equals("")) {
            JOptionPane.showMessageDialog(null, "填写信息不完整", "出错啦",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //判断学号
        if (!isDig(tid)) {
            JOptionPane.showMessageDialog(null, "输入的工号必须是数字", "出错啦",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //判断电话号码
        if (!isDig(phone)) {
            JOptionPane.showMessageDialog(null, "输入的手机号必须是数字", "出错啦",
                    JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        if (phone.length() != 11) {
            JOptionPane.showMessageDialog(null, "手机号长度必须是11位", "出错啦",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //判断性别是否正确
        if (!sex.equals("男") && !sex.equals("女")) {
            JOptionPane.showMessageDialog(null, "性别必须填‘男’或者‘女’", "出错啦",
                    JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        if (sex.length() > 2) {
            JOptionPane.showMessageDialog(null, "性别必须填‘男’或者‘女’", "出错啦",
                    JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        return true;
    }

    //判断数据范围是否正确
    public static boolean isDig(String s) {
        if(s.length()==0) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$"); //正则表达式
        return pattern.matcher(s).matches();   //Parttern类是正则表达式的编译表示
    }

    //“返回" ：返回最初界面窗体，释放该窗体资源
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource().equals(re)) {
            f.dispose();
            choose tmp=new choose();
        }
    }

    //初始化窗体
    int tmp=0;
    public void init() {
        f=new JFrame("教职工信息管理系统");
        tmp++;
        if(tmp==1) {
            re.addActionListener(this); //初始化窗口监听事件
        }
        f.setLayout(null);  //布局：默认
        f.setSize(500,400);
        f.setLocation(300,200);

        //设置窗口关闭和可见性
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    //教师窗口界面设计 与功能实现
    public void start() {
        init(); // 窗体初始化

        //提示字
        JLabel xuehao=new JLabel("工号：");
        JLabel xingming=new JLabel("姓名：");
        JLabel xingbie=new JLabel("性别：");
        JLabel dept=new JLabel("学院：");
        JLabel shoujihao=new JLabel("手机号：");
        JLabel huji=new JLabel("户籍：");
        f.add(xingbie);
        f.add(xuehao);
        f.add(xingming);
        f.add(dept);
        f.add(shoujihao);
        f.add(huji);
        xuehao.setBounds(30,40,150,20);
        xingming.setBounds(30,80,150,20);
        xingbie.setBounds(30,120,150,20);
        dept.setBounds(260,40,150,20);
        shoujihao.setBounds(260,80,150,20);
        huji.setBounds(260,120,150,20);

        //文本域设置
        String []className = {"请选择学院" , "cs","ds","es","fs"};  //下拉框选择
        JTextField tid=new JTextField(20); //文本域
        JTextField name=new JTextField(20);
        JTextField sex=new JTextField(20);
        JComboBox deptnumber=new JComboBox(className);
        JTextField phone=new JTextField(20);
        JTextField address=new JTextField(20);
        f.add(tid);
        f.add(name);
        f.add(sex);
        f.add(deptnumber);
        f.add(phone);
        f.add(address);
        tid.setBounds(80,40,150,20);
        name.setBounds(80,80,150,20);
        sex.setBounds(80,120,150,20);
        deptnumber.setBounds(310,40,150,20);
        phone.setBounds(310,80,150,20);
        address.setBounds(310,120,150,20);

        //按钮设置
        JButton ad=new JButton("添加");
        JButton update=new JButton("修改");
        JButton delete=new JButton("删除");
        JButton query=new JButton("查找");
        JButton clear=new JButton("清空");
        f.add(ad);
        f.add(query);
        f.add(update);
        f.add(delete);
        f.add(clear);
        ad.setBounds(30,300,80,30);
        update.setBounds(120,300,80,30);
        delete.setBounds(210,300,80,30);
        query.setBounds(300,300,80,30);
        clear.setBounds(210,260,80,30);
        f.add(re);    //返回 按钮
        re.setBounds(390,300,80,30);

        //监听器
        class mylistener implements ActionListener{
            @SuppressWarnings("unused")
            @Override
            public void actionPerformed(ActionEvent e) {
                // ss 0 学号， 1 名字， 2 性别， 3 班级， 4 手机号 ， 5 户籍
                String []ss=new String [10];
                ss[0]=tid.getText();
                ss[1]=name.getText();
                ss[2]=sex.getText();
                ss[3]=deptnumber.getSelectedItem().toString();
                ss[4]=phone.getText();
                ss[5]=address.getText();

                //如果是添加按钮
                if(e.getSource().equals(ad)) {

                    if(!judge(ss[0],ss[1],ss[2],ss[3],ss[4],ss[5])) {
                        return;
                    }
                    if(new teacherSql().addData(ss,UTR,NAME,PASSWORD)){
                        JOptionPane.showMessageDialog(null, "信息添加成功", "成功了",
                                JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "教师工号已经存在", "出错啦",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }

                //查找按钮
                //如果全空查询全部数据  如果有学号就查学号  如果有姓名就查询姓名
                // 如果有性别 就查询性别 如果有班级就班级 如果有手机号就查询手机号 如果有户籍就查询户籍
                else if(e.getSource().equals(query)) {


                    JFrame tmp = new JFrame("查询");
                    tmp.setSize(800,600);
                    tmp.setLocation(600,00);


                    //如果没有查询到数据
                    Object[][] info = new teacherSql().findData(ss,UTR,NAME,PASSWORD);
                    if( info == null){
                        JOptionPane.showMessageDialog(null, "提供的数据有误，无该老师信息", "没有查询到数据！",
                                JOptionPane.INFORMATION_MESSAGE);
                        return ;
                    }


                    //表头
                    String[] title = {"工号","姓名","性别","学院", "电话", "户籍"};

                    //创建表格
                    JTable jtable=null;
                    DefaultTableModel model = new DefaultTableModel(info, title);

                    //设置jtable参数
                    jtable = new JTable(model);
                    jtable.setBackground(new Color(Integer.parseInt("99CC99", 16)));
                    jtable.setPreferredScrollableViewportSize(new Dimension(100, 80));
                    jtable.setFillsViewportHeight(true);
                    jtable.getTableHeader().setBackground(new Color(Integer.parseInt("FFFFCC", 16)));

                    // 设置表格中的数据居中显示
                    DefaultTableCellRenderer r=new DefaultTableCellRenderer();
                    r.setHorizontalAlignment(JLabel.CENTER);
                    jtable.setDefaultRenderer(Object.class,r);

                    //滑动面板 将表格添加到滑动面板
                    JScrollPane jScrollPane = new JScrollPane();
                    jScrollPane.setViewportView(jtable);
                    Font font = new Font("宋体", Font.BOLD, 13);

                    //添加label
                    JLabel label = new JLabel("查询数据库中的数据");
                    label.setFont(font);
                    label.setBounds(1,10,240,30);

                    //通过panel组合button，label
                    JPanel panel =new JPanel();
                    panel.setBackground(new Color(Integer.parseInt("FFCC99", 16)));
                    panel.setSize(200,100);
                    panel.add(label);

                    //6，添加表格、滚动条到容器中
                    tmp.add(panel, BorderLayout.NORTH);
                    tmp.setVisible(true);
                    tmp.add(jScrollPane,BorderLayout.CENTER);
                    tmp.setVisible(true);

                    //刷新按钮
                    JButton update = new JButton("刷新");
                    JTable finalJtable1 = jtable;
                    update.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            Object[][] info = new teacherSql().findData(ss,UTR,NAME,PASSWORD);

                            TableModel tableModel = new DefaultTableModel(info,title);
                            finalJtable1.setModel(tableModel);

                        }
                    });

                    update.setFont(font);
                    update.setBounds(1,10,240,30);
                    panel.add(update);

                    JTable finalJtable = jtable;
                    jtable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                        @Override
                        public void valueChanged(ListSelectionEvent e) {
                            if (!e.getValueIsAdjusting()) {
                                //支持拖动多选
                                int[] rows = finalJtable.getSelectedRows();
                                // int[] cols = table.getSelectedColumns();//选中的列
                                for(int j : rows){
                                    tid.setText(finalJtable.getValueAt(j, 0).toString());
                                    name.setText(finalJtable.getValueAt(j, 1).toString());
                                    sex.setText(finalJtable.getValueAt(j, 2).toString());
                                    int sz = deptnumber.getItemCount();
                                    for (int i = 0; i < sz; i++) {
                                        if(deptnumber.getItemAt(i).equals(finalJtable.getValueAt(j, 3).toString())){
                                            // System.out.println(finalJtable.getValueAt(j, 3).toString());
                                            deptnumber.setSelectedIndex(i);
                                            break;
                                        }
                                    }
                                    phone.setText(finalJtable.getValueAt(j, 4).toString());
                                    address.setText(finalJtable.getValueAt(j, 5).toString());
                                }

                            }
                        }
                    });

                }

                //如果是修改按钮:通过学号更新
                else if(e.getSource().equals(update)) {

                    if(!judge(ss[0],ss[1],ss[2],ss[3],ss[4],ss[5])) {
                        JOptionPane.showMessageDialog(null, "填写信息有误或者不完整", "出错啦",
                                JOptionPane.ERROR_MESSAGE);
                        return ;
                    }
                    if(new teacherSql().updateData(ss, UTR, NAME,PASSWORD) ){
                        JOptionPane.showMessageDialog(null, "修改成功", "成功了",
                                JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "修改失败", "出错了",
                                JOptionPane.INFORMATION_MESSAGE);
                    }


                }

                //删除按钮:通过学号删除
                else if(e.getSource().equals(delete)){

                    if(!isDig(ss[0])) {
                        JOptionPane.showMessageDialog(null, "工号不能为空", "出错啦",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if(new teacherSql().deleteData(ss,UTR,NAME,PASSWORD)){
                        JOptionPane.showMessageDialog(null, "删除成功", "成功啦",
                                JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "无改对应老师信息", "删除失败",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                }

                //清空按钮
                else{
                    tid.setText("");
                    name.setText("");
                    sex.setText("");
                    deptnumber.setSelectedIndex(0);
                    phone.setText("");
                    address.setText("");
                }
            }
        }

        //给增删改查 按钮添加监听
        ad.addActionListener(new mylistener());
        delete.addActionListener(new mylistener());
        update.addActionListener(new mylistener());
        query.addActionListener(new mylistener());
        clear.addActionListener(new mylistener());
        System.out.println("老师");
    }


}

