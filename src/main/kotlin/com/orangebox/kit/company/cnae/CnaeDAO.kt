package com.orangebox.kit.company.cnae

import com.orangebox.kit.core.dao.AbstractDAO
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class CnaeDAO : AbstractDAO<Cnae>(Cnae::class.java) {
    override fun getId(bean: Cnae): Any? {
        return bean.id
    }
}