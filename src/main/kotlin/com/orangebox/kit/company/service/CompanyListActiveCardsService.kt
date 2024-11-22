package com.orangebox.kit.company.service

import com.orangebox.kit.company.dao.CompanyDAO
import com.orangebox.kit.company.dto.CompanyCard
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class CompanyListActiveCardsService {

    @Inject
    private lateinit var companyDAO: CompanyDAO

    fun listActiveCards(): List<CompanyCard>? {
        return companyDAO.search(companyDAO.createBuilder()
            .appendParamQuery("status", "ACTIVE")
            .appendSort("socialName", 1)
            .build()
        )?.map { p -> p.toCard() }
    }
}