package com.orangebox.kit.company.cnae

import com.orangebox.kit.core.annotation.OKEntity
import com.orangebox.kit.core.annotation.OKId

@OKEntity("cnae")
class Cnae {
    @OKId
    var id: String? = null
    var cnae: String? = null
    var desc: String? = null
    var cnaeFormated: String? = null
}