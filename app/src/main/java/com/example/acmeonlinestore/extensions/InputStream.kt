package com.example.acmeonlinestore.extensions

import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringWriter
import java.io.Writer

fun InputStream.readAsString(): String? = try {
  val buffer = CharArray(1024)
  val writer: Writer = StringWriter()
  val reader: Reader = BufferedReader(InputStreamReader(this, "UTF-8"))
  var n: Int
  while (reader.read(buffer).also { n = it } != -1) {
    writer.write(buffer, 0, n)
  }
  writer.toString()
} catch (t: Throwable) {
  Log.e("InputStream", "readAsString: ", t)
  null
}
