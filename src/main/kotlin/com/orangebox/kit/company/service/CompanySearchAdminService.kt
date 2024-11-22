package com.orangebox.kit.company.service

import com.orangebox.kit.company.dao.CompanyDAO
import com.orangebox.kit.company.dto.CompanySearch
import com.orangebox.kit.company.model.Company
import com.orangebox.kit.core.dao.OperationEnum
import com.orangebox.kit.core.dao.SearchBuilder
import com.orangebox.kit.core.dto.ResponseList
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class CompanySearchAdminService {

    private val COMPANIES_PAGE = 10

    @Inject
    private lateinit var companyDAO: CompanyDAO

    fun searchAdmin(search: CompanySearch): ResponseList<Company>? {
        val builder: SearchBuilder = companyDAO.createBuilder()
        if (search.status != null) {
            builder.appendParamQuery("status", search.status!!)
        }
        if (search.idCategory != null) {
            builder.appendParamQuery("idCategory", search.idCategory!!)
        }
        if (search.idParent != null) {
            builder.appendParamQuery("idParent", search.idParent!!)
        }
        if (search.type != null) {
            builder.appendParamQuery("type", search.type!!)
        }
        if (search.queryString != null) {
            search.queryString = search.queryString?.replace(Regex("(\\.|-|/)"), "")
            builder.appendParamQuery(
                "fantasyName|socialName|addressInfo.street|addressInfo.district|addressInfo.city|cnae.desc|cnae.cnae|document",
                search.queryString!!,
                OperationEnum.OR_FIELDS_LIKE
            )
        }
        if (search.idCompanyIn != null && search.idCompanyIn!!.isNotEmpty()) {
            builder.appendParamQuery("_id", search.idCompanyIn!!, OperationEnum.IN)
        }
        if (search.city != null && search.city!!.isNotEmpty()) {
            builder.appendParamQuery("addressInfo.city", search.city!!)
        }
        if (search.info != null) {
            search.info?.keys?.forEach { key ->
                if(search.info!![key] != null){
                    builder.appendParamQuery("info.$key", search.info!![key]!!)
                }
            }
        }

        builder.setFirst(COMPANIES_PAGE * (search.page!! - 1))
        builder.setMaxResults(COMPANIES_PAGE)
        if (search.sort != null) {
            for (key in search.sort!!.keys) {
                builder.appendSort(key, search.sort!![key])
            }
        }

        return companyDAO.searchToResponse(builder.build())
    }
}