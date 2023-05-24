package com.orangebox.kit.company

import com.orangebox.kit.admin.util.AdminBaseRestService
import com.orangebox.kit.admin.util.SecuredAdmin
import com.orangebox.kit.core.dto.ResponseList
import com.orangebox.kit.core.photo.FileUpload
import com.orangebox.kit.core.photo.GalleryItem
import java.util.*
import java.util.function.Predicate
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/company")
class CompanyRestService : AdminBaseRestService() {

    @Inject
    private lateinit var companyService: CompanyService

    @SecuredAdmin
    @GET
    @Path("/listAll")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun listAll(): List<Company>? {
        return companyService.listAll()
    }

    @SecuredAdmin
    @GET
    @Path("/listActiveCards")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun listActiveCards(): List<CompanyCard>? {
        return companyService.listActiveCards()
    }

    @SecuredAdmin
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/save")
    fun save(company: Company): Company {
        companyService.save(company)
        return company
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/saveMobile")
    fun saveMobile(company: Company): Company {
        companyService.save(company)
        return company
    }

    @GET
    @Path("/load/{idComp}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun load(@PathParam("idComp") idComp: String?): Company? {
        return companyService.retrieve(idComp!!)
    }

    @GET
    @Path("/retrieveByCode/{code}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun retrieveByCode(@PathParam("code") code: String): Company? {
        return companyService.retrieveByCode(code)
    }

    @GET
    @Path("/retrieveByDocument/{document}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun retrieveByDocument(@PathParam("document") document: String): Company? {
        return companyService.retrieveByDocument(document)
    }


    private fun getGalleryItem(company: Company, photoUpload: FileUpload): GalleryItem {
        val gi: GalleryItem
        if (photoUpload.idSubObject == null) {
            gi = GalleryItem()
            gi.id = UUID.randomUUID().toString()
            if (company.gallery == null) {
                company.gallery = ArrayList()
            }
            company.gallery!!.add(gi)
        } else {
            gi = company.gallery!!.stream()
                .filter(Predicate { p: GalleryItem -> p.id == photoUpload.idSubObject })
                .findFirst()
                .orElse(null)
        }
        return gi
    }

    @DELETE
    @Path("/removePhoto/{idCompany}/{idPhoto}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun removePhoto(
        @PathParam("idCompany") idCompany: String,
        @PathParam("idPhoto") idPhoto: String
    ) {
        companyService.removePhoto(idCompany, idPhoto)
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/changeStatus")
    fun changeStatus(company: Company) {
        companyService.changeStatus(company.id!!)
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/search")
    fun search(search: CompanySearch): List<CompanyCard>? {
        return companyService.search(search)
    }

    @GET
    @Path("/loadCompany/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun loadCompany(@PathParam("id") id: String): Company? {
        return companyService.retrieve(id)
    }

    @GET
    @Path("/listByIdParent/{idParent}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun listByIdParent(@PathParam("idParent") idParent: String): List<CompanyCard>? {
        return companyService.listByIdParent(idParent)
    }

    @POST
    @SecuredAdmin
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/searchAdmin")
    fun searchAdmin(search: CompanySearch): ResponseList<Company>? {
        return companyService.searchAdmin(search)
    }

    @SecuredAdmin
    @POST
    @Consumes("application/json")
    @Path("/saveAvatar")
    fun saveAvatar(fileUpload: FileUpload) {
        return companyService.saveAvatar(fileUpload)
    }
}