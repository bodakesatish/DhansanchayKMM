package com.bodakesatish.kmm.dhansanchay.data.source.remote.api

import com.bodakesatish.kmm.dhansanchay.data.source.remote.model.SchemeNetworkModel
import com.bodakesatish.kmm.dhansanchay.domain.utils.NetworkResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.io.IOException

class SchemeApiService(
    val httpClient: HttpClient,
    val ioDispatcher: CoroutineDispatcher
) {
    private val tag = this::class.simpleName

    private val baseUrl = "https://api.mfapi.in"

    suspend fun fetchSchemesFromServer(): NetworkResult<List<SchemeNetworkModel>> {
//        Log.d(tag, "Fetching scheme list from remote using Ktor")
        return safeKtorCall {
            httpClient.get {
                url("$baseUrl/mf")
            }
        }
    }

    private suspend inline fun <reified T : Any> safeKtorCall(
        crossinline apiCall: suspend () -> HttpResponse
    ): NetworkResult<T> {
        return withContext(ioDispatcher) {
            try {
                val response: HttpResponse = apiCall()

                if (response.status.isSuccess()) {
                    try {
                        val body = response.body<T>()
                        NetworkResult.Success(body)
                    } catch (e: Exception) { // Catch deserialization errors specifically for success cases
                      //  Log.e(tag, "JSON Deserialization error for successful response: ${response.status}: ${e.message}", e)
                        NetworkResult.Error(
                            message = "Error deserializing response: ${e.message}",
                            code = response.status.value,
                            exception = e
                        )
                    }
                } else {
                    // For non-success responses that didn't throw Ktor exceptions (e.g. if expectSuccess = false was used)
                    // This block might be less frequently hit if Ktor's default behavior of throwing exceptions for non-2xx is active.
                    val statusCode = response.status.value
                    val errorBody = try { response.body<String>() } catch (e: Exception) { "" }
                    val errorMessage = parseErrorMessage(errorBody, statusCode, response.status.description, null)
                    //Log.w(tag, "API Error (non-exception path): $errorMessage, Code: $statusCode")
                    NetworkResult.Error(message = errorMessage, code = statusCode)
                }
            } catch (e: ClientRequestException) { // Handles 4xx errors
                val statusCode = e.response.status.value
                val errorBody = try { e.response.body<String>() } catch (ex: Exception) { "" }
                val errorMessage = parseErrorMessage(errorBody, statusCode, e.response.status.description, e)
               // Log.w(tag, "Client Error $statusCode: $errorMessage", e)
                NetworkResult.Error(message = errorMessage, code = statusCode, exception = e)
            } catch (e: ServerResponseException) { // Handles 5xx errors
                val statusCode = e.response.status.value
                val errorBody = try { e.response.body<String>() } catch (ex: Exception) { "" }
                val errorMessage = parseErrorMessage(errorBody, statusCode, e.response.status.description, e)
               // Log.e(tag, "Server Error $statusCode: $errorMessage", e)
                NetworkResult.Error(message = errorMessage, code = statusCode, exception = e)
            } catch (e: RedirectResponseException) { // Specific handling for redirects if necessary
              //  Log.w(tag, "Redirect Error ${e.response.status.value}: ${e.message}", e)
                NetworkResult.Error(
                    message = "Redirect error: ${e.message}",
                    code = e.response.status.value,
                    exception = e
                )
            }
            catch (e: IOException) { // For underlying network connectivity issues (e.g., no network)
              //  Log.e(tag, "Network (IO) Error: ${e.message}", e)
                NetworkResult.Error(
                    message = "Network Error: ${e.message ?: "Could not connect to server."}",
                    code = null, // No HTTP status code for this
                    exception = e
                )
            } catch (e: Exception) { // For other unexpected errors (e.g., Ktor setup, other serialization issues not caught above)
              //  Log.e(tag, "Unexpected Ktor error: ${e.message}", e)
                NetworkResult.Error(
                    message = "An unexpected error occurred: ${e.message ?: "Unknown error."}",
                    code = null,
                    exception = e
                )
            }
        }
    }

    // Helper function to parse error messages, similar to what you had before
    fun parseErrorMessage(errorBody: String, statusCode: Int, statusDescription: String, exception: Throwable?): String {
        val defaultMessage = "Error $statusCode: $statusDescription${exception?.let { " - ${it.message}" } ?: ""}"
        return if (errorBody.isNotEmpty()) {
            try {
                // Attempt to parse a standard error structure if your API provides one
                defaultMessage
            } catch (e: Exception) {
                //Log.w(tag, "Failed to parse error body JSON: $errorBody", e)
                "$defaultMessage (Raw: $errorBody)"
            }
        } else {
            defaultMessage
        }
    }

}