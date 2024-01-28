package xyz.mcxross.zero.model

sealed class Option<out T> {
  data class Some<out T>(val value: T) : Option<T>()

  data object None : Option<Nothing>()
}
