package com.customerservicepackage;

import java.util.List;

import com.customermodelpackage.Customer;

public interface CustomerService {

	public void save(Customer customer);

	public List<Customer> getAllCustomer();

	public void deleteCustomer(Customer customer);

	public List<Customer> getEmail();

}