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
        signatures = setOf(
            "5011d7f93478422063d4b69b524494ec407445ba20b03b728f553b7a5e2d19be"
        ),
        targets = listOf(
            AppTarget(
                version = "2.1.7"
            )
            
        )
    )

    val COMPATIBILITY_Decompile = Compatibility(
        name = "Decompile",
        packageName = "com.apktools.app.decompile",
        apkFileType = ApkFileType.XAPK,
        appIconColor = 0xFF1234,
        signatures = setOf(
            "f0af89ca6e9e415fa5bdbab307814e09c0788455b09748b8ba57899b18e6471a"
        ),
        targets = listOf(
            AppTarget(
                version = "3.3.2",
            )
        )
    )

}
