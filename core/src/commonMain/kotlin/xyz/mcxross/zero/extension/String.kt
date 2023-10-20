package xyz.mcxross.zero.extension

import com.eygraber.uri.Uri

fun String.toUri() : Uri = Uri.parse(this)
