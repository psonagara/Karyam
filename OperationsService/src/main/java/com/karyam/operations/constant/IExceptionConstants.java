package com.karyam.operations.constant;

public interface IExceptionConstants {

	String USER_ALREADY_EXIST = "User already exist for given email";
	String REGISTRATION_FAILED = "Registration Failed";
	String USER_NOT_REGISTERED = "User not registered for given email";
	String WRONG_PASSWORD = "Wrong Password";
	String INACTIVE_PROFILE = "Your Profile is not active, contact Admin";
	String WRONG_ROLE = "You don't have permission for this role";
	String PERMISSION_DENIED = "You don't have permission for this operation";
	String PROJECT_EXISTS = "Project already exists with given name";
	String PROJECT_CREATION_FAIL = "Failed to create project";
	String PROJECT_NOT_FOUND = "Project not found for given id";
	String PROJECT_UPDATE_FAIL = "Failed to update project";
	String LABOR_EXISTS = "Labor already exists with given phone number";
	String LABOR_CREATION_FAIL = "Failed to create labor";
	String LABOR_NOT_FOUND = "Labor not found for given id";
	String LABOR_UPDATE_FAIL = "Failed to update labor details";
	String LABOR_NOT_ASSIGNED_TO_PROJECT = "Labor not assigned to any project";
	String ATTNEDANCE_FAIL = "Attendance marking failed";
	String CSV_GENERATION_FAIL = "Failed to generate CSV export";
	String VENDOR_EXISTS = "Vendor already exists with given phone or email";
	String VENDOR_CREATION_FAIL = "Failed to create Vendor";
	String VENDOR_NOT_FOUND = "Vendor not found for given id";
	String VENDOR_UPDATE_FAIL = "Failed to update Vendor details";
	String PAYMENT_EXCEED_DUE_AMOUNT = "Payment amount exceed Vendor due amount";
	String PAYMENT_CREATION_FAIL = "Failed to create Vendor Payment";
	String EXPENSE_CREATION_FAIL = "Failed to register Expense";
	String EXPENSE_NOT_FOUND = "Expense not found for given id";
	String EXPENSE_UPDATE_FAIL = "Failed to update Expense details";
	String EXPENSE_INVALID_UPDATE = "Invalid request for expense update";
	String APPROVAL_NOT_FOUND = "Approval not found for given id";
	String EXPENSE_APPROVED_FAIL = "Expense Approve Failed";
	String EXPENSE_REJECTED_FAIL = "Expense Reject Failed";
	String PAYROLL_NOT_FOUND = "Payroll not found for given id";
	String PAYROLL_PAID_FAIL = "Payroll marking as paid failed";
}
