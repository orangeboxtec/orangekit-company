package com.orangebox.kit.company

import com.orangebox.kit.core.dao.AbstractDAO
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class CompanyDAO : AbstractDAO<Company>(Company::class.java) {
    override fun getId(bean: Company): Any? {
        return bean.id
    }
}