package org.example.vag.developer.options.helper.extensions

import com.android.ddmlib.IDevice
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import org.jetbrains.android.sdk.AndroidSdkUtils

fun Project.getNotEmptyDevices(): Array<IDevice>? =
    AndroidSdkUtils.getDebugBridge(this)?.devices?.takeIf { it.isNotEmpty() }

fun Project.showNotification(content: String) {
    NotificationGroupManager.getInstance()
        .getNotificationGroup("developer options helper group")
        .createNotification(content, NotificationType.INFORMATION)
        .notify(this)
}