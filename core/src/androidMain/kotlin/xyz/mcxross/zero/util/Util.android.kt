package xyz.mcxross.zero.util

import xyz.mcxross.sc.SuiCommons

actual fun generateKey(): String = SuiCommons.derive.newKey().address
