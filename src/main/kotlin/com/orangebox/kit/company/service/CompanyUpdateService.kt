package com.orangebox.kit.company.service

import com.orangebox.kit.company.dao.CompanyDAO
import com.orangebox.kit.company.model.Company
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class CompanyUpdateService {

    @Inject
    private lateinit var companyDAO: CompanyDAO

    fun update(company: Company): Company {
        companyDAO.update(company)
        return company
    }
}