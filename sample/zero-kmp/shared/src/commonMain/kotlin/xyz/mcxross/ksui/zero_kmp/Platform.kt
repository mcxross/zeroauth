package xyz.mcxross.ksui.zero_kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform