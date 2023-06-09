package com.orangebox.kit.company

import com.orangebox.kit.admin.userb.UserBService
import com.orangebox.kit.core.address.AddressUtils
import com.orangebox.kit.core.dao.OperationEnum
import com.orangebox.kit.core.dao.SearchBuilder
import com.orangebox.kit.core.dto.ResponseList
import com.orangebox.kit.core.exception.BusinessException
import com.orangebox.kit.core.photo.GalleryItem
import com.orangebox.kit.core.utils.BusinessUtils
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class CompanyService {
    private val COMPANIES_PAGE = 10

    @Inject
    private lateinit var userBService: UserBService

    @Inject
    private lateinit var companyDAO: CompanyDAO

    fun listAll(): List<Company>? {
        return companyDAO.listAll()
    }

    fun listActiveCards(): List<CompanyCard>? {
        var listCompanyCard: MutableList<CompanyCard>? = null
        val listComp = listActives()
        if (listComp != null) {
            listCompanyCard = ArrayList()
            for (company in listComp) {
                val card = createCompanyCard(company)
                listCompanyCard.add(card)
            }
        }
        return listCompanyCard
    }

    fun listActives(): List<Company>? {
        return companyDAO.search(
            companyDAO.createBuilder()
                .appendParamQuery("status", "ACTIVE")
                .build()
        )
    }

    fun save(company: Company) {
        if (company.document != null) {
            if (company.id == null) {
                val builder = SearchBuilder()
                builder.appendParamQuery("document", company.document!!)
                val listComp = companyDAO.search(builder.build())
                if (!listComp.isNullOrEmpty()) {
                    throw BusinessException("document_already_used")
                }
            } else {
                val companyBase = retrieve(company.id!!) ?: throw BusinessException("company_not_found")
                if (companyBase.document != null && companyBase.document != company.document) {
                    throw BusinessException("document_cannot_be_changed")
                }
            }
        }
        if (company.id == null) {
            company.creationDate = Date()
            company.status = "ACTIVE"
        }
        if (company.rating == null) {
            company.rating = 2.5
        }
        if (company.businessHours != null) {
            company.businessHoursDesc = WorkingHourUtils.businessHourDesc(company.businessHours)
        }
        BusinessUtils(companyDAO).basicSave(company)
    }

    fun retrieve(id: String): Company? {
        return companyDAO.retrieve(id)
    }

    fun retrieveByCode(code: String): Company? {
        return companyDAO.retrieve(
            companyDAO.createBuilder()
                .appendParamQuery("code", code)
                .build()
        )
    }

    fun retrieveByDocument(code: String): Company? {
        return companyDAO.retrieve(
            companyDAO.createBuilder()
                .appendParamQuery("document", code)
                .build()
        )
    }

    fun loadByField(field: String?, value: String?): Company? {
        return companyDAO.retrieve(
            companyDAO.createBuilder()
                .appendParamQuery(field, value!!)
                .build()
        )
    }

    fun addPhoto(idCompany: String?, idPhoto: String?) {
        val company = companyDAO.retrieve(Company(idCompany))
        if (company!!.gallery == null) {
            company.gallery = ArrayList()
        }
        company.gallery!!.add(GalleryItem(idPhoto))
        companyDAO.update(company)
    }

    fun removePhoto(idCompany: String, idPhoto: String) {
        val company = companyDAO.retrieve(Company(idCompany))
        if(company != null) {
            if (company.gallery != null) {
                val item = company.gallery?.find { p -> p.id == idPhoto }
                if(item != null){
                    company.gallery!!.remove(item)
                }
            }
            companyDAO.update(company)
        }
    }

    fun update(company: Company): Company {
        companyDAO.update(company)
        return company
    }

    fun changeStatus(id: String) {
        val company = retrieve(id)
        if (company!!.status != null && company.status == "ACTIVE") {
            company.status = "BLOCKED"
        } else {
            company.status = "ACTIVE"
        }
        companyDAO.update(company)
    }

    fun search(search: CompanySearch): List<CompanyCard>? {
        var list: MutableList<CompanyCard>? = null
        val builder: SearchBuilder = companyDAO.createBuilder()
        if (search.status != null) {
            builder.appendParamQuery("status", search.status!!)
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
        builder.setFirst(COMPANIES_PAGE * search.page!!)
        builder.setMaxResults(COMPANIES_PAGE)
        val listComp = companyDAO.search(builder.build())
        if (listComp != null) {
            list = ArrayList()
            for (company in listComp) {
                val card = createCompanyCard(company)
                list.add(card)
            }
        }
        return list
    }

    fun createCompanyCard(company: Company?): CompanyCard {
        val card = CompanyCard()
        card.address = AddressUtils.textualAddress(company!!.addressInfo)
        card.id = company.id
        card.name = company.fantasyName
        card.rating = company.rating
        card.addressInfo = company.addressInfo
        if (company.info != null) {
            card.info = company.info
        }
        return card
    }

    fun createCompanyCard(idCompany: String?): CompanyCard {
        val card: CompanyCard
        val company = companyDAO.retrieve(Company(idCompany))
        card = createCompanyCard(company)
        return card
    }

    fun listByIdParent(idParent: String): List<CompanyCard>? {
        var list: MutableList<CompanyCard>? = null
        val builder: SearchBuilder = companyDAO.createBuilder()
        builder.appendParamQuery("idParent", idParent)
        builder.appendParamQuery("status", "ACTIVE")
        builder.appendSort("fantasyName", 1)
        val listComps = companyDAO.search(builder.build())
        if (listComps != null) {
            list = ArrayList()
            for (company in listComps) {
                list.add(createCompanyCard(company))
            }
        }
        return list
    }

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