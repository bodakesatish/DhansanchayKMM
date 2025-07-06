package com.bodakesatish.kmm.dhansanchay

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform