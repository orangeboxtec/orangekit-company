package com.orangebox.kit.company.service

import com.orangebox.kit.company.dao.CompanyDAO
import com.orangebox.kit.company.model.Company
import com.orangebox.kit.company.util.WorkingHourUtils
import com.orangebox.kit.core.dao.OperationEnum
import com.orangebox.kit.core.dao.SearchBuilder
import com.orangebox.kit.core.exception.BusinessException
import com.orangebox.kit.core.utils.BusinessUtils
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import java.util.*

@ApplicationScoped
class CompanySaveService {

    @Inject
    private lateinit var companyDAO: CompanyDAO

    fun save(company: Company) {
        company.document = company.document?.replace(Regex("(\\.|-|/)"), "")
        if (company.document != null) {
            val builder = SearchBuilder()
            builder.appendParamQuery("document", company.document!!)
            if (company.id != null) {
                builder.appendParamQuery("_id", company.id!!, OperationEnum.NOT)
            }
            val listComp = companyDAO.search(builder.build())
            if (!listComp.isNullOrEmpty()) {
                throw BusinessException("document_already_used")
            }
        }
        if (company.code != null) {
            val builder = SearchBuilder()
            builder.appendParamQuery("code", company.code!!)
            if (company.id != null) {
                builder.appendParamQuery("_id", company.id!!, OperationEnum.NOT)
            }
            val listComp = companyDAO.search(builder.build())
            if (!listComp.isNullOrEmpty()) {
                throw BusinessException("code_already_used")
            }
        }
        if (company.id == null) {
            company.creationDate = Date()
        }
        if (company.rating == null) {
            company.rating = 2.5
        }
        if (company.businessHours != null) {
            company.businessHoursDesc = WorkingHourUtils.businessHourDesc(company.businessHours)
        }
        BusinessUtils(companyDAO).basicSave(company)
    }
}