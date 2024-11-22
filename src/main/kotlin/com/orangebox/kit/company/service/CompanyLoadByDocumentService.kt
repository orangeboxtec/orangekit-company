package com.orangebox.kit.company.service

import com.orangebox.kit.company.dao.CompanyDAO
import com.orangebox.kit.company.model.Company
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class CompanyLoadByDocumentService {

    @Inject
    private lateinit var companyDAO: CompanyDAO

    fun retrieveByDocument(code: String): Company? {
        return companyDAO.retrieve(
            companyDAO.createBuilder()
                .appendParamQuery("document", code)
                .build()
        )
    }
}