package com.customermapperpackage;

import java.util.List;

import com.customermodelpackage.Customer;

public interface CustomerMapper {

	public Long saveCustomer(Customer customer);

	public List<Customer> getAllCustomer();

	public void deleteCustomer(Customer customer);

	public void updateCustomer(Customer customer);

	public List<Customer> getEmail();

}