package com.orangebox.kit.company.service

import com.orangebox.kit.company.dao.CompanyDAO
import com.orangebox.kit.company.model.Company
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class CompanyLoadByFieldService {

    @Inject
    private lateinit var companyDAO: CompanyDAO

    fun loadByField(field: String?, value: String?): Company? {
        return companyDAO.retrieve(
            companyDAO.createBuilder()
                .appendParamQuery(field, value!!)
                .build()
        )
    }
}