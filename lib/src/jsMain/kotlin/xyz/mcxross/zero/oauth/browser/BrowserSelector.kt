package xyz.mcxross.zero.oauth.browser

actual object BrowserSelector {
  /**
   * Selects a browser to use for authorization.
   *
   * @param browserMatcher The [BrowserMatcher] to use when selecting a browser to use for
   *   authorization.
   * @param browsers The list of browsers to select from.
   * @return The [BrowserDescriptor] to use for authorization, or `null` if no browser was selected.
   */
  actual fun select(
    browserMatcher: BrowserMatcher,
    browsers: List<BrowserDescriptor>
  ): BrowserDescriptor? {
    TODO("Not yet implemented")
  }
}
