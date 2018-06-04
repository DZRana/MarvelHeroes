package com.example.danze.marvelheroes

import com.google.gson.annotations.SerializedName

data class Hero (
        @SerializedName("data") val data: Data
)

data class Data (
        @SerializedName("results") val results: List<Result> = listOf()
)

data class Result (
        @SerializedName("name") val name: String,
        @SerializedName("thumbnail") val thumbnail: Thumbnail
)

data class Thumbnail (
        @SerializedName("path") val path: String,
        @SerializedName("extension") val extension: String
)