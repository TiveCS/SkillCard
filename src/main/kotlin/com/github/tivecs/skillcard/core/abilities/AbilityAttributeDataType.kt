package com.github.tivecs.skillcard.core.abilities

enum class AbilityAttributeDataType(val dataType: Class<*>) {
    STRING(String::class.java),
    DOUBLE(Double::class.java),
    BOOLEAN(Boolean::class.java),
    INT(Int::class.java),
    FLOAT(Float::class.java),
    SHORT(Short::class.java),
    CHAR(Char::class.java),
    BYTE(Byte::class.java),
    UNKNOWN(Any::class.java),
}