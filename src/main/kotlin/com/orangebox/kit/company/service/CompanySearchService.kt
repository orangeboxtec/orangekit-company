package com.orangebox.kit.company.service

import com.orangebox.kit.company.dao.CompanyDAO
import com.orangebox.kit.company.dto.CompanyCard
import com.orangebox.kit.company.dto.CompanySearch
import com.orangebox.kit.core.dao.OperationEnum
import com.orangebox.kit.core.dao.SearchBuilder
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class CompanySearchService {

    private val COMPANIES_PAGE = 10

    @Inject
    private lateinit var companyDAO: CompanyDAO

    fun search(search: CompanySearch): List<CompanyCard>? {
        var list: MutableList<CompanyCard>? = null
        val builder: SearchBuilder = companyDAO.createBuilder()
        if (search.status != null) {
            builder.appendParamQuery("status", search.status!!)
        }
        if (search.type != null) {
            builder.appendParamQuery("type", search.type!!)
        }
        if (search.idCategory != null) {
            builder.appendParamQuery("category.id", search.idCategory!!)
        }
        if (search.idParent != null) {
            builder.appendParamQuery("idParent", search.idParent!!)
        }
        if (search.contacEmail != null) {
            builder.appendParamQuery("contactEmail", search.contacEmail!!)
        }
        if (search.queryString != null) {
            builder.appendParamQuery("fantasyName", search.queryString!!, OperationEnum.LIKE)
        }
        if (search.idCompanyIn != null && search.idCompanyIn!!.isNotEmpty()) {
            builder.appendParamQuery("_id", search.idCompanyIn!!, OperationEnum.IN)
        }
        if (search.info != null) {
            search.info?.keys?.forEach { key ->
                builder.appendParamQuery("info.$key", search.info!![key]!!)
            }
        }
        builder.appendSort("creationDate", -1)
        builder.setFirst(COMPANIES_PAGE * search.page!!)
        builder.setMaxResults(COMPANIES_PAGE)
        return companyDAO.search(builder.build())?.map { p -> p.toCard() }
    }
}