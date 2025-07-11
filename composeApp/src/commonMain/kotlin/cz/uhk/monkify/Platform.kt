package cz.uhk.monkify

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform