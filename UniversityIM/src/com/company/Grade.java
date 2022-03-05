package com.company;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import static com.company.Student.isDig;

class gradeSql extends dataProcessing {

    @Override
    boolean addData(String ss[],String UTR,String NAME,String PASSWORD) {
        try{//加载驱动中·
            Class.forName("com.mysql.cj.jdbc.Driver");//加载驱动
            Connection coon = DriverManager.getConnection(UTR,NAME,PASSWORD);//创建连接对象

            String sql=" INSERT  INTO  tbl_grade   VALUES  ('"+ss[0]+"','"+ss[1]+"','"+ss[2]+"')";
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

            String sql="delete from tbl_grade where Subid='"+ss[0]+"' AND Sid = '" +ss[1]+"' " ;
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

            String sql=" UPDATE tbl_grade SET grade='"+ss[2]+"' where Subid='"+ss[0]+"' AND Sid = '" +ss[1]+"' " ;
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
            String sql = "select * from tbl_grade where ";
            boolean flag = false;

            //全空就查询所有数据
            if (ss[0].equals("") && ss[1].equals("") && ss[2].equals("") ) {
                System.out.println("查询所有学生信息");
                sql = " select * from tbl_grade ;";
            }
            if (!ss[0].equals("") && isDig(ss[0])) {//课程是数字 就按学号查询
                //System.out.println("按学号查询");
                sql += " Subid = " + "'" + ss[0] + "'";
                flag = true;
            }
            if (!ss[1].equals("") && isDig(ss[1])) {//学号不为空时 按姓名查询
                if (flag) {
                    sql += " AND ";
                }
                flag = true;
                sql += " Sid = " + "'" + ss[1] + "'";
            }
            if (!ss[2].equals("")) {//姓名不为空时 按姓名查询
                if (flag) {
                    sql += " AND ";
                }
                flag = true;
                sql += " grade = " + "'" + ss[2] + "'";
            }


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
                info[count][1] = rs.getString("Sid");
                info[count][2] = rs.getString("grade");

                count++;
            }
            return info;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

public class Grade implements ActionListener{
    public JFrame f=new JFrame("成绩管理");
    private JButton re=new JButton("返回");
    static private String UTR = "jdbc:mysql://localhost:3306/university_im?"
            + "useSSL=false&serverTimezone=UTC&characterEncoding=UTF8";
    static private String NAME = "root";
    static private String PASSWORD = "applemysql";
    private JScrollPane scpDemo;
    private JTableHeader jth;
    private JTable tabDemo;
    private JButton btnShow;

    //判断数据输入是否正确
    public static boolean judgeGrade( String subid, String sid, String grade) {
        if (subid.equals("") || sid.equals("") || grade.equals("")) {
            JOptionPane.showMessageDialog(null, "信息填写不完整", "出错啦",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!isDig(subid)) {
            JOptionPane.showMessageDialog(null, "课程号必须是数字", "出错啦",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(!isDig(sid)){
            JOptionPane.showMessageDialog(null, "学号必须是数字", "出错啦",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isDouble(grade)) {
            JOptionPane.showMessageDialog(null, "成绩必须是数字", "出错啦",
                    JOptionPane.ERROR_MESSAGE);
            return  false;
        }
        float k = Float.parseFloat(grade);
        if (k < 0) {
            JOptionPane.showMessageDialog(null, "成绩不能为负数", "出错啦",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    //判断输入数据范围是否正确
    public static boolean isDig(String s) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(s).matches();
    }

    //判断数据范围或者数据为空
    public  static boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    //"返回"最初界面
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource().equals(re)) {
            f.dispose();
            choose tmp=new choose();
        }
    }

    // 界面初始化
    int tmp=0;
    public void init() {
        f=new JFrame("成绩管理");
        tmp++;
        if(tmp==1) {
            re.addActionListener(this);
        }
        f.setLayout(null);
        f.setSize(500,400);
        f.setLocation(300,200);
    }

    //成绩界面设计与功能实现
    public void start() {
        init();

        //输入
        JTextField subid=new JTextField(20);
        JTextField sid=new JTextField(20);
        JTextField core=new JTextField(20);
        f.add(subid);
        f.add(sid);
        f.add(core);
        subid.setBounds(180,40,150,20);
        sid.setBounds(180,80,150,20);
        core.setBounds(180,120,150,20);

        //提示字
        JLabel lsub=new JLabel("课程号：");
        JLabel lsid =new JLabel("学号：");
        JLabel lcore =new JLabel("分数：");
        f.add(lsub);
        f.add(lsid);
        f.add(lcore);
        lsub.setBounds(130,40,150,20);
        lsid.setBounds(130,80,150,20);
        lcore.setBounds(130,120,150,20);

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
                String []ss=new String [3];
                ss[0]=subid.getText();
                ss[1]=sid.getText();
                ss[2]=core.getText();


                //如果是添加按钮
                if(e.getSource().equals(ad)) {

                    if(!judgeGrade(ss[0],ss[1],ss[2])) {
                        return;
                    }
                    if(new gradeSql().addData(ss,UTR,NAME,PASSWORD)){
                        JOptionPane.showMessageDialog(null, "信息添加成功", "啦",
                                JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "提供的数据有误，无该学生成绩信息", "啦",
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
                    Object[][] info = new gradeSql().findData(ss,UTR,NAME,PASSWORD);
                    if( info == null){
                        JOptionPane.showMessageDialog(null, "提供的数据有误，无该学生成绩信息", "没有查询到数据！",
                                JOptionPane.INFORMATION_MESSAGE);
                        return ;
                    }


                    //表头
                    String[] title = {"课程号","学号","成绩"};

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

                            Object[][] info = new gradeSql().findData(ss,UTR,NAME,PASSWORD);

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
                                    subid.setText(finalJtable.getValueAt(j, 0).toString());
                                    sid.setText(finalJtable.getValueAt(j, 1).toString());
                                    core.setText(finalJtable.getValueAt(j, 2).toString());

                                }

                            }
                        }
                    });

                }

                //如果是修改按钮:通过学号更新
                else if(e.getSource().equals(update)) {

                    if(!judgeGrade(ss[0],ss[1],ss[2])) {
                        JOptionPane.showMessageDialog(null, "填写信息有误或者不完整", "出错啦",
                                JOptionPane.ERROR_MESSAGE);
                        return ;
                    }
                    if(new gradeSql().updateData(ss, UTR, NAME,PASSWORD) ){
                        JOptionPane.showMessageDialog(null, "修改成功", "成功了",
                                JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "修改失败，该学号学生没有该课程成绩,", "出错了",
                                JOptionPane.INFORMATION_MESSAGE);
                    }


                }

                //删除按钮:通过学号删除
                else if(e.getSource().equals(delete)){

                    if(!ss[0].equals("") && !isDig(ss[0])) {
                        JOptionPane.showMessageDialog(null, "课程号不能为空且必须为数字", "出错啦",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if(!ss[1].equals("") && !isDig(ss[1])) {
                        JOptionPane.showMessageDialog(null, "学号不能为空且必须为数字", "出错啦",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if(new gradeSql().deleteData(ss,UTR,NAME,PASSWORD)){
                        JOptionPane.showMessageDialog(null, "删除成功", "成功啦",
                                JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "提供的数据有误，无该学生成绩信息", "删除失败",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                }

                //清空按钮
                else{
                    subid.setText("");
                    sid.setText("");
                    core.setText("");

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

