package app.epxec.patches.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.SupportedAbi

object Constants {
    val COMPATIBILITY_Touch_The_Notch = Compatibility(
        name = "Touch The Notch", 
        packageName = "com.notch.touch", 
        apkFileType = ApkFileType.XAPK, 
        appIconColor = 0xFF0045,
        targets = listOf(
            AppTarget(
                version = "2.1.7"
            )
        )
    )

}
