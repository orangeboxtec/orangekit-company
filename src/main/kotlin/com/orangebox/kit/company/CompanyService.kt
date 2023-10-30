package com.orangebox.kit.company

import com.orangebox.kit.core.bucket.BucketService
import com.orangebox.kit.core.dao.OperationEnum
import com.orangebox.kit.core.dao.SearchBuilder
import com.orangebox.kit.core.dto.ResponseList
import com.orangebox.kit.core.exception.BusinessException
import com.orangebox.kit.core.file.FileUpload
import com.orangebox.kit.core.file.GalleryItem
import com.orangebox.kit.core.utils.BusinessUtils
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class CompanyService {
    private val COMPANIES_PAGE = 10

    @Inject
    private lateinit var bucketService: BucketService

    @Inject
    private lateinit var companyDAO: CompanyDAO

    fun listAll(): List<Company>? {
        return companyDAO.listAll()
    }

    fun listActiveCards(): List<CompanyCard>? {
        return listActives()?.map { p -> p.toCard() }
    }

    fun listActives(): List<Company>? {
        return companyDAO.search(
            companyDAO.createBuilder()
                .appendParamQuery("status", "ACTIVE")
                .appendSort("socialName", 1)
                .build()
        )
    }

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

    fun createCompanyCard(company: Company?): CompanyCard? {
        return company?.toCard()
    }

    fun createCompanyCard(idCompany: String?): CompanyCard? {
        val company = companyDAO.retrieve(Company(idCompany))
        return createCompanyCard(company)
    }

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

    fun saveAvatar(file: FileUpload){
        val company = retrieve(file.idObject!!) ?: throw BusinessException("company_not_foud")
        val url = bucketService.saveFile(file, company.id!!, "userb", "image/jpg")
        company.urlImage = url
        companyDAO.update(company)
    }
}