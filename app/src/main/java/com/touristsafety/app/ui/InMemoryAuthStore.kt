package com.touristsafety.app.ui

object InMemoryAuthStore {
    private val identifierToPassword: MutableMap<String, String> = mutableMapOf()

    fun register(identifier: String, password: String) {
        identifierToPassword[identifier] = password
    }

    fun login(identifier: String, password: String): Boolean {
        val saved = identifierToPassword[identifier]
        return saved != null && saved == password
    }
}


