package app.epxec.patches.notizenwidget

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags

// ─────────────────────────────────────────────────────────────────────────────
// Fingerprints for NotiZen Widget (com.gustavcaves.notizenwidget)
//
// Architecture:
//   wh5.a() → isPremiumActive
//     ├── tc0.a() → SharedPreferences.getBoolean("premium_monthly_active", false)
//     └── isDeveloperOverrideEnabled (Kotlin lambda, dev-only path)
//
// We target tc0 — the BillingEntitlementStore — because its contract is
// anchored on the stable, non-obfuscated string "premium_monthly_active".
// ─────────────────────────────────────────────────────────────────────────────

// Anchors on the "premium_monthly_active" SharedPreferences key that tc0 uses.
// Both methods (reader + writer) live in the same class, so one classFingerprint
// serves both.
private object BillingEntitlementStoreClassFingerprint : Fingerprint(
    strings = listOf("premium_monthly_active")
)

// Matches tc0.a() — reads SharedPreferences key "premium_monthly_active" and
// returns it as a boolean. This is the isPremiumActive check used by wh5.a().
//
// Smali:
//   .method public final a()Z
//     iget-object p0, p0, Ltc0;->prefs:Landroid/content/SharedPreferences;
//     const-string v0, "premium_monthly_active"
//     const/4 v1, 0x0
//     invoke-interface {p0, v0, v1}, Landroid/content/SharedPreferences;->getBoolean(...)Z
//     move-result p0
//     return p0
object NotiZenPremiumReadFingerprint : Fingerprint(
    classFingerprint = BillingEntitlementStoreClassFingerprint,
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf(),
    filters = listOf(
        string("premium_monthly_active"),
        methodCall(
            definingClass = "Landroid/content/SharedPreferences;",
            name = "getBoolean"
        )
    )
)

// Matches tc0.b(Z) — writes the boolean value to SharedPreferences key
// "premium_monthly_active". Called by kp4.j(List) when Google Play Billing
// returns a purchase result. No-op'ing this prevents billing validation from
// ever writing false back to the flag.
//
// Smali:
//   .method public final b(Z)V
//     iget-object p0, p0, Ltc0;->prefs:Landroid/content/SharedPreferences;
//     invoke-interface ... ->edit()...
//     const-string v0, "premium_monthly_active"
//     invoke-interface ... ->putBoolean(...)...
//     invoke-interface ... ->apply()V
//     return-void
object NotiZenPremiumWriteFingerprint : Fingerprint(
    classFingerprint = BillingEntitlementStoreClassFingerprint,
    returnType = "V",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf("Z"),
    filters = listOf(
        string("premium_monthly_active"),
        methodCall(
            definingClass = "Landroid/content/SharedPreferences\$Editor;",
            name = "putBoolean"
        )
    )
)
