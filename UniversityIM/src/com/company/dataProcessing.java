package com.company;

/**
 * 数据操作抽象类
 */
public abstract class dataProcessing {

    abstract boolean addData(String ss[], String UTR, String NAME, String PASSWORD);  //添加数据

    abstract boolean deleteData(String ss[], String UTR, String NAME, String PASSWORD);  //删除数据

    abstract boolean updateData(String ss[], String UTR, String NAME, String PASSWORD);    //修改数据

    abstract Object[][] findData(String ss[], String UTR, String NAME, String PASSWORD);   //查询数据
}
