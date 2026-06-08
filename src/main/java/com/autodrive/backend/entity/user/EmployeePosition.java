package com.autodrive.backend.entity.user;

public enum EmployeePosition {
	MANAGER,
	SALES_CONSULTANT,
	SERVICE_ADVISOR,
	MECHANIC,
	FINANCE_SPECIALIST,
	ADMINISTRATOR;

	public String authority() {
		return "POSITION_" + name();
	}
}
