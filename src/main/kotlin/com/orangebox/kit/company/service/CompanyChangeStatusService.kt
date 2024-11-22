package com.orangebox.kit.company.service

import com.orangebox.kit.company.dao.CompanyDAO
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class CompanyChangeStatusService {

    @Inject
    private lateinit var companyDAO: CompanyDAO

    fun changeStatus(id: String) {
        val company = companyDAO.retrieve(id)
        if (company!!.status != null && company.status == "ACTIVE") {
            company.status = "BLOCKED"
        } else {
            company.status = "ACTIVE"
        }
        companyDAO.update(company)
    }
}