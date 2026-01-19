package com.github.tivecs.skillcard.core.converters

import kotlin.reflect.KClass

data class TypeConverter<TSource : Any, TTarget : Any>(
    val sourceType: KClass<TSource>,
    val targetType: KClass<TTarget>,
    val name: String,
    private val converter: (TSource) -> TTarget?
) {
    @Suppress("UNCHECKED_CAST")
    fun convert(value: Any): TTarget? = converter(value as TSource)
}