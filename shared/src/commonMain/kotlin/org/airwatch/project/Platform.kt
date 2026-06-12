package org.airwatch.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform