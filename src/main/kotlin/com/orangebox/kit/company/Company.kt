package com.orangebox.kit.company

import com.orangebox.kit.company.cnae.Cnae
import com.orangebox.kit.core.address.AddressInfo
import com.orangebox.kit.core.address.AddressUtils
import com.orangebox.kit.core.annotation.OKEntity
import com.orangebox.kit.core.annotation.OKId
import com.orangebox.kit.core.photo.GalleryItem
import java.util.*

@OKEntity("company")
class Company {
    @OKId
    var id: String? = null
    var socialName: String? = null
    var code: String? = null
    var idUser: String? = null
    var document: String? = null
    var documentType: String? = null
    var fantasyName: String? = null
    var rating: Double? = null
    var desc: String? = null
    var colorImage: String? = null
    var status: String? = null
    var fgFeatured: Boolean? = null
    var fgBranch: Boolean? = null
    var branchName: String? = null
    var phone: String? = null
    var type: String? = null
    var idParent: String? = null
    var phoneNumber: Long? = null
    var phoneCountryCode: Int? = null
    var addressInfo: AddressInfo? = null
    var contactName: String? = null
    var contactPhone: String? = null
    var contactPhoneNumber: Long? = null
    var contactPhoneCountryCode: Int? = null
    var contactEmail: String? = null
    var fgOpen: Boolean? = null
    var businessHours: List<WorkingHour>? = null
    var scheduleException: List<ScheduleException>? = null
    var gallery: ArrayList<GalleryItem>? = null
    var info: HashMap<String, Any>? = null
    var businessHoursDesc: String? = null
    var creationDate: Date? = null
    var foundationDate: Date? = null
    var distance: Double? = null
    var companyTax: Double? = null
    var salesManTax: Double? = null
    var idSalesMan: String? = null
    var urlImage: String? = null
    var fgHeadOffice: String? = null
    var cnae: Cnae? = null
    var secondaryCnaes: ArrayList<Cnae>? = null
    var idCategory: String? = null
    var nameCategory: String? = null

    constructor()
    constructor(id: String?) {
        this.id = id
    }

    fun toCard(): CompanyCard {
        val card = CompanyCard()
        card.address = AddressUtils.textualAddress(addressInfo)
        card.id = id
        card.fantasyName = fantasyName
        card.socialName = socialName
        card.document = document
        card.rating = rating
        card.addressInfo = addressInfo
        return card
    }
}