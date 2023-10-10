package com.customerdaopackage;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.customermodelpackage.Customer;

public interface CustomerDao {

	public void updateCustomer(SqlSession sqlSession, Customer customer);

	public void saveCustomer(SqlSession sqlSession, Customer customer);

	public List<Customer> getAllCustomer(SqlSession sqlSession);

	public void deleteCustomer(SqlSession sqlSession, Customer customer);

	public List<Customer> getEmail(SqlSession sqlSession);

}