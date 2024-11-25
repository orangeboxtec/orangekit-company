package com.orangebox.kit.company.restservice

import com.orangebox.kit.admin.util.AdminBaseRestService
import com.orangebox.kit.admin.util.SecuredAdmin
import com.orangebox.kit.company.dto.CompanyCard
import com.orangebox.kit.company.dto.CompanySearch
import com.orangebox.kit.company.model.Company
import com.orangebox.kit.company.service.*
import com.orangebox.kit.core.dto.ResponseList
import com.orangebox.kit.core.file.FileUpload
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType

@Path("/company")
class CompanyRestService : AdminBaseRestService() {

    @Inject
    private lateinit var companyListAllService: CompanyListAllService

    @Inject
    private lateinit var companyListActiveCardsService: CompanyListActiveCardsService

    @Inject
    private lateinit var companyListActiveService: CompanyListActivesService

    @Inject
    private lateinit var companySaveService: CompanySaveService

    @Inject
    private lateinit var companyLoadService: CompanyLoadService

    @Inject
    private lateinit var companyLoadByCodeService: CompanyLoadByCodeService

    @Inject
    private lateinit var companyLoadByDocumentService: CompanyLoadByDocumentService

    @Inject
    private lateinit var companyListByIdParentService: CompanyListByIdParentService

    @Inject
    private lateinit var companySearchAdminService: CompanySearchAdminService

    @Inject
    private lateinit var companySaveAvatarService: CompanySaveAvatarService



    @SecuredAdmin
    @GET
    @Path("/listAll")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun listAll(): List<Company>? {
        return companyListAllService.listAll()
    }

    @SecuredAdmin
    @GET
    @Path("/listActiveCards")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun listActiveCards(): List<CompanyCard>? {
        return companyListActiveCardsService.listActiveCards()
    }

    @SecuredAdmin
    @GET
    @Path("/listActives")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun listActives(): List<Company>? {
        return companyListActiveService.listActives()
    }

    @SecuredAdmin
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/save")
    fun save(company: Company): Company {
        companySaveService.save(company)
        return company
    }


    @SecuredAdmin
    @GET
    @Path("/load/{idComp}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun load(@PathParam("idComp") idComp: String?): Company? {
        return companyLoadService.retrieve(idComp!!)
    }

    @SecuredAdmin
    @GET
    @Path("/retrieveByCode/{code}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun retrieveByCode(@PathParam("code") code: String): Company? {
        return companyLoadByCodeService.retrieveByCode(code)
    }

    @SecuredAdmin
    @GET
    @Path("/retrieveByDocument/{document}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun retrieveByDocument(@PathParam("document") document: String): Company? {
        return companyLoadByDocumentService.retrieveByDocument(document)
    }


    @SecuredAdmin
    @GET
    @Path("/loadCompany/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun loadCompany(@PathParam("id") id: String): Company? {
        return companyLoadService.retrieve(id)
    }

    @SecuredAdmin
    @GET
    @Path("/listByIdParent/{idObj}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun listByIdParent(@PathParam("idObj") idObj: String): List<CompanyCard>? {
        return companyListByIdParentService.listByIdParent(idObj)
    }

    @POST
    @SecuredAdmin
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/searchAdmin")
    fun searchAdmin(search: CompanySearch): ResponseList<Company>? {
        return companySearchAdminService.searchAdmin(search, userTokenSession!!)
    }

    @SecuredAdmin
    @POST
    @Consumes("application/json")
    @Path("/saveAvatar")
    fun saveAvatar(fileUpload: FileUpload) {
        return companySaveAvatarService.saveAvatar(fileUpload)
    }
}