package me.misik.api.domain.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class ListToStringConverter : AttributeConverter<List<String>, String> {

    override fun convertToDatabaseColumn(attribute: List<String>): String {
        val seperatorDeletedAttribute = attribute.map { it.replace(SEPERATOR, "") }

        return seperatorDeletedAttribute.joinToString(SEPERATOR)
    }

    override fun convertToEntityAttribute(dbData: String?): List<String> {
        return dbData?.split(SEPERATOR) ?: emptyList()
    }

    private companion object {
        private const val SEPERATOR = "|"
    }
}

