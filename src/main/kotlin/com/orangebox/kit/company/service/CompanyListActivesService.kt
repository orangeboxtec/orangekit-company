package com.orangebox.kit.company.service

import com.orangebox.kit.company.dao.CompanyDAO
import com.orangebox.kit.company.model.Company
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class CompanyListActivesService {

    @Inject
    private lateinit var companyDAO: CompanyDAO

    fun listActives(): List<Company>? {
        return companyDAO.search(
            companyDAO.createBuilder()
                .appendParamQuery("status", "ACTIVE")
                .appendSort("socialName", 1)
                .build()
        )
    }
}