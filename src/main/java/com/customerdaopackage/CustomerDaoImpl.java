package com.customerdaopackage;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.customermapperpackage.CustomerMapper;
import com.customermodelpackage.Customer;

@Repository("customerDao")
public class CustomerDaoImpl implements CustomerDao {

	@Override
	public void updateCustomer(SqlSession sqlSession, Customer customer) {
		CustomerMapper customerMapper = sqlSession.getMapper(CustomerMapper.class);
		customerMapper.updateCustomer(customer);
	}

	@Override
	public void saveCustomer(SqlSession sqlSession, Customer customer) {
		CustomerMapper customerMapper = sqlSession.getMapper(CustomerMapper.class);
		customerMapper.saveCustomer(customer);
	}

	@Override
	public List<Customer> getAllCustomer(SqlSession sqlSession) {
		CustomerMapper customerMapper = sqlSession.getMapper(CustomerMapper.class);
		List<Customer> list = customerMapper.getAllCustomer();
		return list;
	}

	@Override
	public void deleteCustomer(SqlSession sqlSession, Customer customer) {
		CustomerMapper customerMapper = sqlSession.getMapper(CustomerMapper.class);
		customerMapper.deleteCustomer(customer);
	}

	@Override
	public List<Customer> getEmail(SqlSession sqlSession) {
		CustomerMapper customerMapper = sqlSession.getMapper(CustomerMapper.class);
		List<Customer> list = customerMapper.getEmail();
		return list;
	}

}