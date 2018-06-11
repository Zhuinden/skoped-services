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

import android.app.Activity
import android.content.Context

interface ScopeKey {
    fun bindServices(services: Services) {
        services.createIfAbsent(this, serviceBinder)
    }

    val serviceBinder: ServiceBuilder.() -> Unit

    val scopeTag: String
}

interface ServiceHost {
    val services: Services
}

inline fun <reified T> Context.lookup(scopeKey: ScopeKey, serviceTag: String = T::class.java.name): T = findActivity<Activity>().takeIf { it is ServiceHost }?.let { activity ->
    (activity as ServiceHost).services.get<T>(scopeKey, serviceTag)
} ?: throw IllegalArgumentException("The Activity must be a ServiceHost for the service look-up to work!")

fun Services.has(scopeKey: ScopeKey, serviceTag: String) = has(scopeKey.scopeTag, serviceTag)

fun <T> Services.get(scopeKey: ScopeKey, serviceTag: String) = get<T>(scopeKey.scopeTag, serviceTag)

inline fun <reified T> Services.add(scopeKey: ScopeKey, service: T) = add(scopeKey.scopeTag, T::class.java.name, service as Any)

fun Services.add(scopeKey: ScopeKey, serviceTag: String, service: Any) = add(scopeKey.scopeTag, serviceTag, service)

fun <T> Services.remove(scopeKey: ScopeKey, serviceTag: String): T? = remove(scopeKey.scopeTag, serviceTag)

fun Services.forEach(scopeKey: ScopeKey, action: (Any) -> Unit) = forEach(scopeKey.scopeTag, action)

fun Services.destroy(scopeKey: ScopeKey) = destroy(scopeKey.scopeTag)

fun Services.createIfAbsent(scopeKey: ScopeKey, initializer: ServiceBuilder.() -> Unit) = createIfAbsent(scopeKey.scopeTag) {
    ServiceBuilder(this, scopeKey).apply(initializer)
}

class ServiceBuilder(val services: Services, val scopeKey: ScopeKey) {
    fun has(serviceTag: String) = services.has(scopeKey, serviceTag)

    fun <T> get(serviceTag: String) = services.get<T>(scopeKey, serviceTag)

    inline fun <reified T> add(service: T) = add(T::class.java.name, service as Any)

    fun add(serviceTag: String, service: Any) = services.add(scopeKey, serviceTag, service)

    fun <T> remove(serviceTag: String): T? = services.remove(scopeKey, serviceTag)
}