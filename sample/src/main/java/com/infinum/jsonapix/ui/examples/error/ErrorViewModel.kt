package com.infinum.jsonapix.ui.examples.error

import com.infinum.jsonapix.core.resources.DefaultError
import com.infinum.jsonapix.data.api.SampleApiService
import com.infinum.jsonapix.data.assets.JsonAssetReader
import com.infinum.jsonapix.retrofit.asJsonXHttpException
import com.infinum.jsonapix.ui.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import retrofit2.HttpException

@HiltViewModel
class ErrorViewModel @Inject constructor(
    private val sampleApiService: SampleApiService,
    private val jsonAssetReader: JsonAssetReader
) : BaseViewModel<ErrorState, ErrorEvent>() {

    @Suppress("UnusedPrivateMember")
    fun fetchError() {
        launch {
            showLoading()
            val bodyString = io { jsonAssetReader.readJsonAsset("responses/error.json") }
            try {
                val person = io { sampleApiService.fetchError() }
            } catch (e: HttpException) {
                e.asJsonXHttpException().errors?.first()?.let {
                    if (it is DefaultError) {
                        showError(it.detail)
                    } else {
                        showError("Not a default error")
                    }
                } ?: showError("Unable to parse to JsonXHttpException")
            }

            hideLoading()
        }
    }
}
