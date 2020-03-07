package br.com.vitor.pokermao.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HealthResponse (
    val status: String
): Parcelable