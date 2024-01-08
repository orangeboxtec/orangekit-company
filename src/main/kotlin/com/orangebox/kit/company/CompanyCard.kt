package com.orangebox.kit.company

import com.orangebox.kit.company.cnae.Cnae
import com.orangebox.kit.core.address.AddressInfo

class CompanyCard {
    var id: String? = null
    var name: String? = null
    var address: String? = null
    var rating: Double? = null
    var serviceFeatured: String? = null
    var priceFeatured: Double? = null
    var distance: Double? = null
    var addressInfo: AddressInfo? = null
    var info: Map<String, Any>? = null
    var secondaryCnae: Cnae? = null
}
