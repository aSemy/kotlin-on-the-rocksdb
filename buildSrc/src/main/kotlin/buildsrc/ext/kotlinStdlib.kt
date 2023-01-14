package buildsrc.ext

import java.util.Locale


fun String.uppercaseFirstChar(): String = replaceFirstChar {
  when {
    it.isLowerCase() -> it.titlecase(Locale.getDefault())
    else             -> it.toString()
  }
}


fun String.lowercaseFirstChar(): String = replaceFirstChar {
  when {
    it.isUpperCase() -> it.lowercaseChar()
    else             -> it
  }
}
