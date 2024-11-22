package com.orangebox.kit.company.service

import com.orangebox.kit.company.dao.CompanyDAO
import com.orangebox.kit.company.dto.CompanyCard
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import java.util.ArrayList

@ApplicationScoped
class CompanyListByIdParentService {

    @Inject
    private lateinit var companyDAO: CompanyDAO

    fun listByIdParent(idParent: String): List<CompanyCard> {
        val list = ArrayList<CompanyCard>()
        companyDAO.search(companyDAO.createBuilder()
            .appendParamQuery("idParent", idParent)
            .appendParamQuery("status", "ACTIVE")
            .appendSort("fantasyName", 1)
            .build())?.forEach { company ->
            list.add(company.toCard())
        }
        return list
    }
}