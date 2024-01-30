plugins {
  id("com.android.application").version("8.1.2").apply(false)
  id("com.android.library").version("8.1.2").apply(false)
  kotlin("android").version("1.9.22").apply(false)
  kotlin("multiplatform").version("1.9.22").apply(false)
}

tasks.register("clean", Delete::class) { delete(rootProject.buildDir) }
