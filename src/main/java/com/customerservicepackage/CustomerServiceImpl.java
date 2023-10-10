package com.customerservicepackage;

import java.util.List;
import java.util.Objects;

import org.apache.ibatis.session.SqlSession;
import org.zkoss.zkplus.spring.SpringUtil;

import com.customerdaopackage.CustomerDao;
import com.customermodelpackage.Customer;
import com.utilpackage.MybatisUtil;

public class CustomerServiceImpl implements CustomerService {

	CustomerDao customerDao= (CustomerDao) SpringUtil.getBean("customerDao");
	SqlSession sqlSession = MybatisUtil.getSqlSessionFactory().openSession();

	@Override
	public void save(Customer customer) {
		try {
			if (Objects.isNull(customer.getId())) {
				customerDao.saveCustomer(sqlSession, customer);
			} else {
				customerDao.updateCustomer(sqlSession, customer);
			}
			sqlSession.commit();
		} catch (Exception e) {
			System.out.println("Problem in serviceimpl save method");
		} 
	}

	@Override
	public List<Customer> getAllCustomer() {
		List<Customer> list = customerDao.getAllCustomer(sqlSession);
		try {
			sqlSession.commit();
		} catch (Exception e) {
			System.out.println("Problem in serviceimpl getAllCustomer method");
		}
		return list;
	}

	@Override
	public void deleteCustomer(Customer customer) {
		try {
			customerDao.deleteCustomer(sqlSession, customer);
			sqlSession.commit();
		} catch (Exception e) {
			System.out.println("Problem in serviceimpl deleteCustomer method");
		} 
	}

	@Override
	public List<Customer> getEmail() {
		List<Customer> list = customerDao.getEmail(sqlSession);
		try {
			sqlSession.commit();
		} catch (Exception e) {
			System.out.println("Problem in serviceimpl getEmail method");
		}
		return list;
	}

}