package com.orangebox.kit.company

import java.util.HashMap

class CompanySearch {
    var queryString: String? = null
    var idCategory: String? = null
    var idUser: String? = null
    var idParent: String? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var city: String? = null
    var type: String? = null
    var status: String? = null
    var contacEmail: String? = null
    var page: Int? = null
    var pageItensNumber: Int? = null
    var idCompanyIn: List<String>? = null
    var sort: Map<String, Int>? = null
    var info: HashMap<String, Any>? = null
}