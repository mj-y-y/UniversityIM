package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class choose implements ActionListener {
    public JFrame f=new JFrame("选择应用");
    private JButton stu;
    private JButton tea;
    private JButton sub;
    private JButton grade;

    public  choose() {
        f.setLayout(null);
        f.setSize(500,400);
        f.setLocation(300,200);

        //添加窗口按钮
        stu = new JButton("学生信息管理系统");
        tea = new JButton("教师工信息管理系统");
        sub = new JButton("课程信息管理系统");
        grade = new JButton("成绩管理系统");
        f.add(stu);
        f.add(tea);
        f.add(sub);
        f.add(grade);
        stu.setBounds(160,60,150,20);
        tea.setBounds(160,100,150,20);
        sub.setBounds(160,140,150,20);
        grade.setBounds(160,180,150,20);
        stu.addActionListener(this);
        tea.addActionListener(this);
        sub.addActionListener(this);
        grade.addActionListener(this);

        f.setVisible(true);
    }
    //事务
    @Override
    public void actionPerformed(ActionEvent e){

            if(e.getSource().equals(stu)) {
                f.dispose();
                Student student = new Student();
                student.start();
            }
            else if (e.getSource().equals(tea)) {
                f.dispose();
                Teacher teacher = new Teacher();
                teacher.start();
            }
            else if (e.getSource().equals(sub)) {
                f.dispose();
                Subject subject = new Subject();
                subject.start();
            }

            else if (e.getSource().equals(grade)) {
                f.dispose();
                Grade grade = new Grade();
                grade.start();
            }
        }

    public static void main(String[] args) {
        choose c = new choose();
    }
}
