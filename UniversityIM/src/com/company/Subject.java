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

class subjectSql extends dataProcessing {

    @Override
    boolean addData(String ss[],String UTR,String NAME,String PASSWORD) {
        try{//加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");//加载驱动
            Connection coon = DriverManager.getConnection(UTR,NAME,PASSWORD);//创建连接对象

            String sql=" INSERT  INTO  tbl_subject  VALUES  ('"+ss[0]+"','"+ss[1]+"','"+ss[2]+"')";
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

            String sql="delete from tbl_subject where Subid='"+ss[0]+"'";
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

            String sql=" UPDATE tbl_subject SET Subname='"+ss[1]+"',Tid='"+ss[2]+"' where Subid='"+ss[0]+"' ";
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
            String sql = "select * from tbl_subject where ";
            boolean flag = false;

            //全空就查询所有数据
            if (ss[0].equals("") && ss[1].equals("") && ss[2].equals("")) {
                System.out.println("查询所有学科信息");
                sql = " select * from tbl_subject ;";
            }
            if (!ss[0].equals("") && isDig(ss[0])) {
                //System.out.println("按学号查询");
                sql += " Subid = " + "'" + ss[0] + "'";
                flag = true;
            }
            if (!ss[1].equals("")) {//姓名不为空时 按姓名查询
                if (flag) {
                    sql += " AND ";
                }
                flag = true;
                sql += " Subname like " + "'" + "%" + ss[1] + "%" + "'";
            }

            if (!ss[2].equals("") && isDig(ss[2])) {//如果有电话号码

                if (flag) {
                    sql += " AND ";
                }
                flag = true;
                sql += " Tid = " + "'" + ss[2] + "'";
            }

//            System.out.println(sql + "查询结果");
            Statement stmt = coon.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;
            while(rs.next()){
                count++;
            }

            Object[][] info = new Object[count][3];
            count = 0;
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                info[count][0] = rs.getString("Subid");
                info[count][1] = rs.getString("Subname");
                info[count][2] = rs.getString("Tid");

                count++;
            }
            return info;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

public class Subject implements ActionListener{
    public JFrame f=new JFrame("课程管理系统");
    private JButton re=new JButton("返回");
    static private String UTR = "jdbc:mysql://localhost:3306/university_im?"
            + "useSSL=false&serverTimezone=UTC&characterEncoding=UTF8";
    static private String NAME = "root";
    static private String PASSWORD = "applemysql";

    //监听“返回” ：返回最初界面，然后释放该窗体资源
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource().equals(re)) {
            f.dispose();
            choose tmp=new choose();
        }
    }

    //判断输入的数据是否合法
    public static boolean judgeSubject(String Subid,String Subname,String tid) {
        if(Subid.equals("") || Subname.equals("") || tid.equals("")){
            JOptionPane.showMessageDialog(null, "填写信息不完整", "出错啦",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!isDig(Subid)){
            JOptionPane.showMessageDialog(null, "课程号必须为数字", "出错啦",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!isDig(tid)){
            JOptionPane.showMessageDialog(null, "教师工号号必须为数字", "出错啦",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    //用于判断输入数据范围
    public static boolean isDig(String s) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(s).matches();
    }

    //窗口初始化
    int tmp=0;
    public void init() {
        f=new JFrame("课程信息管理");
        tmp++;
        if(tmp==1) {
            re.addActionListener(this);
        }
        f.setLayout(null);
        f.setSize(500,400);
        f.setLocation(300,200);
    }

    //课程界面与功能设计
    public void start() {
        init();

        //输入框
        JTextField id=new JTextField(20);
        JTextField name=new JTextField(20);
        JTextField tid=new JTextField(20);
        f.add(id);
        f.add(name);
        f.add(tid);
        id.setBounds(180,80,150,20);
        name.setBounds(180,120,150,20);
        tid.setBounds(180,160,150,20);

        //提示字
        JLabel lid=new JLabel("课程号：");
        JLabel lname=new JLabel("课程名：");
        JLabel ltid = new JLabel("教师工号：");
        f.add(lid);
        f.add(lname);
        f.add(ltid);
        lid.setBounds(130,80,150,20);
        lname.setBounds(130,120,150,20);
        ltid.setBounds(130,160,150,20);

        //按钮
        JButton ad=new JButton("添加");
        JButton update=new JButton("修改");
        JButton delete=new JButton("删除");
        JButton query=new JButton("查找");
        JButton clear=new JButton("清空");
        f.add(re);   //返回 按钮
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
        re.setBounds(390,300,80,30);

        //事务
        class mylistener implements ActionListener{
            @SuppressWarnings("unused")
            @Override
            public void actionPerformed(ActionEvent e) {
                // ss 0 学号， 1 名字， 2 性别， 3 班级， 4 手机号 ， 5 户籍
                String []ss=new String [10];
                ss[0]=id.getText();
                ss[1]=name.getText();
                ss[2]=tid.getText();


                //如果是添加按钮
                if(e.getSource().equals(ad)) {

                    if(!judgeSubject(ss[0],ss[1],ss[2])) {
                        return;
                    }
                    if(new subjectSql().addData(ss,UTR,NAME,PASSWORD)){
                        JOptionPane.showMessageDialog(null, "信息添加成功", "出错啦",
                                JOptionPane.INFORMATION_MESSAGE);
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
                    Object[][] info = new subjectSql().findData(ss,UTR,NAME,PASSWORD);
                    if( info == null){
                        JOptionPane.showMessageDialog(null, "提供的数据有误，无该课程信息", "没有查询到数据！",
                                JOptionPane.INFORMATION_MESSAGE);
                        return ;
                    }


                    //表头
                    String[] title = {"课程号","课程名","老师工号"};

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

                            Object[][] info = new subjectSql().findData(ss,UTR,NAME,PASSWORD);

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
                                    id.setText(finalJtable.getValueAt(j, 0).toString());
                                    name.setText(finalJtable.getValueAt(j, 1).toString());
                                    tid.setText(finalJtable.getValueAt(j, 2).toString());

                                }

                            }
                        }
                    });

                }

                //如果是修改按钮:通过学号更新
                else if(e.getSource().equals(update)) {

                    if(!judgeSubject(ss[0],ss[1],ss[2])) {
                        JOptionPane.showMessageDialog(null, "填写信息有误或者不完整", "出错啦",
                                JOptionPane.ERROR_MESSAGE);
                        return ;
                    }
                    if(new subjectSql().updateData(ss, UTR, NAME,PASSWORD) ){
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
                        JOptionPane.showMessageDialog(null, "课程号不能为空", "出错啦",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if(new subjectSql().deleteData(ss,UTR,NAME,PASSWORD)){
                        JOptionPane.showMessageDialog(null, "删除成功", "成功啦",
                                JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "无该课程号信息", "删除失败",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                }

                //清空按钮
                else{
                    id.setText("");
                    name.setText("");
                    tid.setText("");

                }
            }
        }
        ad.addActionListener(new mylistener());
        delete.addActionListener(new mylistener());
        update.addActionListener(new mylistener());
        query.addActionListener(new mylistener());
        clear.addActionListener(new mylistener());

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
