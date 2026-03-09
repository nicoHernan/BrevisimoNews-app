package com.example.brevisimo_news.data.repository.ai

import com.example.brevisimo_news.domain.repository.AIRepository
import com.google.firebase.ai.GenerativeModel
import javax.inject.Inject

class AIRepositoryImpl @Inject constructor(
    private val generativeModel: GenerativeModel
): AIRepository {


    override suspend fun extractKeyEntities(text: String): List<String> {
        if (text.isBlank()) return emptyList()

        val extractionPrompt = """
        Dado el siguiente artículo de noticias, identifica la entidad o el sujeto principal...
        Responde ÚNICAMENTE con el nombre de la entidad principal. No añadas explicaciones ni texto adicional.
        Artículo: "$text"
    """.trimIndent()

        val principalEntity = try {
            val response = generativeModel.generateContent(prompt = extractionPrompt)
            response.text?.trim() ?: ""
        } catch (e: Exception) {
            return emptyList()
        }

        if (principalEntity.isEmpty()) return emptyList()

        val descriptionPrompt = """
        Proporciona una descripción breve (máximo 40 palabras) de la siguiente entidad, 
        adecuada para un usuario de una aplicación de noticias. 
        Entidad: "$principalEntity"
    """.trimIndent()

        val entityDescription = try {
            val response = generativeModel.generateContent(prompt = descriptionPrompt)
            response.text?.trim() ?: ""
        } catch (e: Exception) {
            return emptyList()
        }

        return listOf(principalEntity, entityDescription)
    }
}