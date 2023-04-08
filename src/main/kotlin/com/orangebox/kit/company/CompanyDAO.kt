package com.orangebox.kit.company

import com.orangebox.kit.core.dao.AbstractDAO

class CompanyDAO : AbstractDAO<Company>(Company::class.java) {
    override fun getId(bean: Company): Any? {
        return bean.id
    }
}