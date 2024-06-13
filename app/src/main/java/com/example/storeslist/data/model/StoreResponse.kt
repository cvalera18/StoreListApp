package com.example.storeslist.data.model

// Modelo principal de la respuesta de la API
data class StoreResponse(
    val data: List<StoreData>,
    val meta: Meta,
    val links: Links
)

// Modelo de datos de las tiendas
data class StoreData(
    val id: String,
    val type: String,
    val attributes: StoreAttributes
)

// Atributos de la tienda
data class StoreAttributes(
    val name: String,
    val code: String,
    val full_address: String
)

// Metadatos de la respuesta
data class Meta(
    val pagination: Pagination
)

// Información de paginación
data class Pagination(
    val current_page: Int,
    val total: Int,
    val per_page: Int
)

// Enlaces de navegación
data class Links(
    val prev: String?,
    val next: String?,
    val first: String,
    val last: String,
    val self: String
)
