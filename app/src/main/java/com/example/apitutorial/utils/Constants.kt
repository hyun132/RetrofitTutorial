package com.example.apitutorial.utils

object Constants {
    const val TAG: String = "로그"
}

enum class SEARCH_TYPE {
    PHOTO,
    USER
}

enum class RESPOMSE_STATE{
    OKAY,
    FAIL
}


object API {
    const val BASE_URL: String = "https://api.unsplash.com/"
    const val CLIENT_ID: String = "ku88obtQCPJB9LesjI2jAIhgpKelbAWK_Pblp7UhcgA"

    const val SEARCH_PHOTOS = "search/photos"
    const val SEARCH_USERS = "search/users"


}