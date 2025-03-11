package org.example.vag.developer.options.helper.extensions

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.openapi.util.Key
import org.apache.commons.codec.binary.Hex
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private const val SECRET = "4eRata44B3sRBJQ57x6X"

fun List<String>.runInAdb() {
    // Prepare the command line. If adb is not in the PATH, provide its full path here.
    val commandLine = GeneralCommandLine(this)
    try {
        val processHandler = OSProcessHandler(commandLine)
        processHandler.addProcessListener(object : ProcessAdapter() {
            override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                // Print or log the output.
                // To update the UI, wrap UI changes in ApplicationManager.getApplication().invokeLater { ... }
                println(event.text)
            }
        })
        // Start the process asynchronously.
        processHandler.startNotify()
    } catch (e: Exception) {
        // Handle exceptions, e.g. if the command fails to start.
        e.printStackTrace()
    }
}

@Throws(Exception::class)
fun String.encode(): String {
    val sha256Hmac = Mac.getInstance("HmacSHA256")
    val secretKey = SecretKeySpec(SECRET.toByteArray(charset("UTF-8")), "HmacSHA256")
    sha256Hmac.init(secretKey)

    val result = Hex.encodeHex(sha256Hmac.doFinal(this.toByteArray(charset("UTF-8"))))

    return String(result)
}