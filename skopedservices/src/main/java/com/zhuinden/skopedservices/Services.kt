/*
 * Copyright 2018 Gabor Varadi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhuinden.skopedservices

class Services {
    private val services: MutableMap<String, MutableMap<String, Any>> = mutableMapOf()

    fun createIfAbsent(scopeTag: String, initializer: Services.() -> Unit) {
        if(services.containsKey(scopeTag)) {
            return
        }
        initializer(this)
    }

    val scopes : Set<String>
        get() = setOf(*services.keys.toTypedArray())

    fun has(scopeTag: String, serviceTag: String) =
        services.containsKey(scopeTag) && services[scopeTag]?.containsKey(serviceTag) ?: false

    @Suppress("UNCHECKED_CAST")
    fun <T> get(scopeTag: String, serviceTag: String): T =
        services.takeIf { has(scopeTag, serviceTag) }?.get(scopeTag)?.get(serviceTag) as T
            ?: throw IllegalArgumentException("Service [$serviceTag] is not found in scope [$scopeTag]")

    fun add(scopeTag: String, serviceTag: String, service: Any) {
        val scopeServices = services.takeIf { it.containsKey(scopeTag) }
            ?.get(scopeTag)
            ?: mutableMapOf<String, Any>().also {
                services[scopeTag] = it
            }
        scopeServices[serviceTag] = service
    }

    fun <T> remove(scopeTag: String, serviceTag: String): T? {
        val scopeServices = services.takeIf { it.containsKey(scopeTag) }
            ?.get(scopeTag)
            ?: mutableMapOf<String, Any>().also {
                services[scopeTag] = it
            }
        @Suppress("UNCHECKED_CAST")
        return scopeServices.remove(serviceTag) as T?
    }

    fun forEach(scopeTag: String, action: (Any) -> Unit) {
        services.takeIf { it.containsKey(scopeTag) }?.get(scopeTag)?.forEach(action)
            ?: throw IllegalArgumentException("The specified scope [$scopeTag] does not exist!")
    }

    fun destroy(scopeTag: String) {
        services.remove(scopeTag)
    }
}