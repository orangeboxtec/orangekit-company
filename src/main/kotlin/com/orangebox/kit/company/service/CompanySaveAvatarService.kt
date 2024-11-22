package com.orangebox.kit.company.service

import com.orangebox.kit.company.dao.CompanyDAO
import com.orangebox.kit.core.bucket.BucketService
import com.orangebox.kit.core.exception.BusinessException
import com.orangebox.kit.core.file.FileUpload
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class CompanySaveAvatarService {

    @Inject
    private lateinit var bucketService: BucketService

    @Inject
    private lateinit var companyDAO: CompanyDAO


    fun saveAvatar(file: FileUpload){
        val company = companyDAO.retrieve(file.idObject!!) ?: throw BusinessException("company_not_foud")
        val url = bucketService.saveFile(file, company.id!!, "userb", "image/jpg")
        company.urlImage = url
        companyDAO.update(company)
    }
}