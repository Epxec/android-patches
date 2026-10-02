package app.epxec.patches.notizenwidget

import app.epxec.patches.notizenwidget.NotiZenPremiumReadFingerprint
import app.epxec.patches.notizenwidget.NotiZenPremiumWriteFingerprint
import app.epxec.patches.shared.Constants.COMPATIBILITY_NotizenWidget
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.all.misc.fix.changepackageinstaller.changePackageInstallerPatch


@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Unlocks all premium features in NotiZen Widget for Notion.",
    default = true
) {

    compatibleWith(COMPATIBILITY_NotizenWidget)
    dependsOn(changePackageInstallerPatch())

    execute {
        // ── Patch 1: tc0.a() ─────────────────────────────────────────────────
        // Force the SharedPreferences read of "premium_monthly_active" to always
        // return true. This is the single boolean gate consumed by wh5.a()
        // (isPremiumActive), which in turn gates PremiumFeatureGateActivity,
        // PremiumWidgetConfigGateActivity, and all feature-gated UI branches.
        NotiZenPremiumReadFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x1
                return v0
            """
        )

        // ── Patch 2: tc0.b(Z) ────────────────────────────────────────────────
        // No-op the SharedPreferences writer so that Google Play Billing results
        // can never store false back into "premium_monthly_active". Without this,
        // the billing callback in kp4.j(List) would overwrite the premium flag on
        // every app start that fails to find an active purchase.
        NotiZenPremiumWriteFingerprint.method.addInstructions(
            0,
            """
                return-void
            """
        )
    }
}
