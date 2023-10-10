package com.customercontrollerpackage;

import java.util.List;
import java.util.Objects;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

import com.customermodelpackage.Customer;
import com.customerservicepackage.CustomerService;
import com.customerservicepackage.CustomerServiceImpl;

public class CustomerController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;
	
	Customer customer = null;
	CustomerService customerService=new CustomerServiceImpl();

	@Wire
	Intbox customerIntbox;

	@Wire
	Textbox firstNameTextbox, emailTextbox;

	@Wire
	Button saveCustomerButton;

	@Wire
	Datebox birthdateDatebox;
	
	@Wire
	Radiogroup genderRadiobox;

	@Wire
	Radio genderRadioboxMale,genderRadioboxFemale;

	@Wire
	Checkbox travelCheckbox, readingCheckbox, singingCheckbox, trekCheckbox, swimmingCheckbox, recordCheckbox;

	@Wire
	Listbox pastUserListbox;

	@Wire
	Grid customerlistGrid, customerFormGrid;

	@Wire
	Paging paging;

	List<Customer> allCustomers;
	ListModelList<Customer> customerListModel;
	private int currentPage = 0;
	private int pageSize = 5;

	// To get list of customer
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		paging.setPageSize(pageSize);
		paging.setActivePage(currentPage);
		List<Customer> customerList = customerService.getAllCustomer();
		ListModelList<Customer> listModelList = new ListModelList<>(customerList);
		customerListModel = listModelList;
		updatePagingInfo();
		refreshCustomerData();
		customerlistGrid.setModel(listModelList);
	}

	public boolean validate() throws Exception {
		try {
			if (firstNameTextbox.getValue().isBlank() || !firstNameTextbox.getValue().matches("^[a-zA-Z]*$")) {
				throw new Exception("Please provide valid first name");
			} else if (!emailTextbox.getValue().matches("[a-z0-9]+@[a-z]+\\.[a-z]{2,3}")) {
				throw new Exception("Please provide valid email address.");
			} else if (!genderRadioboxMale.isChecked() && !genderRadioboxFemale.isChecked()) {
				throw new Exception("Please select  gender field");
			} else if (!isEmailUnique()) {
				throw new WrongValueException(emailTextbox, "Email alredy exists");
			} else {
				return true;
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), "warning", customerFormGrid, "top_center", 5000, true);
		}
		return false;
	}

	public boolean isEmailUnique() {
		if (Objects.isNull(customer)) {
			String userEmail = emailTextbox.getValue();
			List<Customer> customerList = customerService.getEmail();
			for (Customer customer : customerList) {
				if (userEmail.equalsIgnoreCase(customer.getEmail())) {
					return false;
				}
			}
		}
		return true;
	}

	// For insert
	@Listen("onClick = #saveCustomerButton")
	public void save() {
		try {
			if (validate()) {
				if (Objects.isNull(customer)) {
					customer = new Customer();
				}
				customer.setFirstName(firstNameTextbox.getValue());
				customer.setBirthDate(birthdateDatebox.getValue());
				String selectedGender = genderRadiobox.getSelectedItem().getLabel();
				boolean isMale = "Male".equalsIgnoreCase(selectedGender);
				customer.setGender(isMale);
				customer.setEmail(emailTextbox.getValue());
				StringBuilder selectedHobbies = new StringBuilder();
				if (travelCheckbox.isChecked()) {
					selectedHobbies.append("Travel, ");
				}
				if (readingCheckbox.isChecked()) {
					selectedHobbies.append("Reading, ");
				}
				if (singingCheckbox.isChecked()) {
					selectedHobbies.append("Singing, ");
				}
				if (trekCheckbox.isChecked()) {
					selectedHobbies.append("Trekking, ");
				}
				if (swimmingCheckbox.isChecked()) {
					selectedHobbies.append("Swimming, ");
				}
				// Remove the trailing comma and space, if any
				if (selectedHobbies.length() > 0) {
					selectedHobbies.setLength(selectedHobbies.length() - 2);
				}
				customer.setHobby(selectedHobbies.toString());
				customer.setRecord(recordCheckbox.isChecked());
				customer.setPastUser(Integer.parseInt((String) pastUserListbox.getSelectedItem().getValue()));
				customerService.save(customer);
				Executions.sendRedirect(null);
			}
		} catch (WrongValueException e) {
			System.out.println("Wrong value");
		} catch (Exception e) {
			System.out.println("Problem in hobby");
		}
	}

	// For paging
	private void updatePagingInfo() {
		int totalSize = customerService.getAllCustomer().size();
		paging.setTotalSize(totalSize);
		paging.setPageSize(pageSize);
		paging.setActivePage(currentPage);
	}

	public void refreshCustomerData() {
		try {
			List<Customer> allCustomers = customerService.getAllCustomer();
			int startIndex = currentPage * pageSize;
			int endIndex = Math.min(startIndex + pageSize, allCustomers.size());
			List<Customer> customers = allCustomers.subList(startIndex, endIndex);
			customerListModel.clear();
			customerListModel.addAll(customers);
			updatePagingInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Listen("onPaging = #paging")
	public void onPaging() {
		currentPage = paging.getActivePage();
		refreshCustomerData();
	}

	// For delete
	@Listen("onDeleteClick = #windowid")
	public void deleteCustomer(ForwardEvent event) {
		Toolbarbutton toolbarbutton = (Toolbarbutton) event.getOrigin().getTarget();
		Component parent = toolbarbutton.getParent();
		while (Objects.nonNull(parent) && !(parent instanceof Row)) {
			parent = parent.getParent();
			if (parent instanceof Row) {
				Row row = (Row) parent;
				final Customer customerclass = (Customer) row.getValue();
				Messagebox.show("Are you sure want delete this ", "Confirmation message",
					Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
					@Override
					public void onEvent(final Event evt) throws InterruptedException {
						if (Messagebox.ON_YES.equals(evt.getName())) {
							customerService.deleteCustomer(customerclass);
							Executions.sendRedirect(null);
							Clients.showNotification("Customer delete succesfully.", "info", customerFormGrid, "top_center", 1500, true);
						} else {
							Clients.showNotification("Customer data is safe.", "warning", customerFormGrid, "top_center", 1500, true);
						}
					}
				});
			}
		}
	}

	// For update
	@Listen("onUpdate = #windowid")
	public void updateUser(ForwardEvent event) {
		Toolbarbutton toolbarbutton = (Toolbarbutton) event.getOrigin().getTarget();
		Component parent = toolbarbutton.getParent();
		while (Objects.nonNull(parent) && !(parent instanceof Row)) {
			parent = parent.getParent();
		}
		if (parent instanceof Row) {
			Row row = (Row) parent;
			customer = (Customer) row.getValue();
		}
		customerIntbox.setValue(customer.getId().intValue());
		firstNameTextbox.setValue(customer.getFirstName());
		birthdateDatebox.setValue(customer.getBirthDate());
		if (customer.isGender()) {
			genderRadioboxMale.setChecked(true);
			genderRadioboxFemale.setChecked(false);
		} else {
			genderRadioboxMale.setChecked(false);
			genderRadioboxFemale.setChecked(true);
		}		emailTextbox.setValue(customer.getEmail());
		String hobbiesString = customer.getHobby();
		String[] selectedHobbies = hobbiesString.split(",");
		if (Objects.nonNull(selectedHobbies)) {
			for (String hobby : selectedHobbies) {
				String trimmedHobby = hobby.trim();
				switch (trimmedHobby) {
				case "Travel":
					travelCheckbox.setChecked(true);
					break;
				case "Reading":
					readingCheckbox.setChecked(true);
					break;
				case "Singing":
					singingCheckbox.setChecked(true);
					break;
				case "Trekking":
					trekCheckbox.setChecked(true);
					break;
				case "Swimming":
					swimmingCheckbox.setChecked(true);
					break;
				}
			}
		}
		saveCustomerButton.setLabel(Objects.isNull(customer.getId()) ? "Save" : "Update");
		recordCheckbox.setChecked(customer.getRecord());
		String string = String.valueOf(customer.getPastUser());
		for (Listitem item : pastUserListbox.getItems()) {
			if (item.getValue() != null && item.getValue().equals(string)) {
				pastUserListbox.setSelectedItem(item);
				break;
			}
		}
	}

}