package com.zhuinden.skopedservicesexample

import com.zhuinden.skopedservices.ScopeKey
import com.zhuinden.skopedservices.ServiceBuilder
import com.zhuinden.skopedservices.Services
import com.zhuinden.skopedservices.add

private val service = Object()

class ExampleKey : ScopeKey {
    override val serviceBinder: ServiceBuilder.() -> Unit
        get() = {
            add("MyService", service)
        }

    override val scopeTag: String
        get() = "The scope"
}

fun blah() {
    val services = Services()
    val exampleKey = ExampleKey()
    exampleKey.bindServices(services)
}