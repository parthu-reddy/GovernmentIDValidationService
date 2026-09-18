# What the `dev` profile bypasses

`SPRING_PROFILES_ACTIVE=dev` is deliberate. The Oracle box is a **development** deployment, and the
profile switches off third-party validations so development is not gated on external providers.
Nothing here is a defect.

This file exists for one reason: when a production deployment happens, these are the checks that must
come back on. They are scattered across six classes and each one fails open — a bypassed check
returns *success*, not an error — so a forgotten one is silent.

Inventory as of 2026-09-18. Every branch is `if ("dev".equalsIgnoreCase(activeProfile) ||
"test".equalsIgnoreCase(activeProfile))`.

| Class | Line | What happens on `dev` |
|---|---|---|
| `BiometricVerificationService` | 32 | Liveness/face-match AI skipped. Returns live=true, confidence 0.995 — **every selfie passes** |
| `DrivingLicenseVerificationService` | 51 | DL check mocked: valid, "MOCK DEV USER", LMV, expiry 2030-12-31 |
| `VehicleVerificationService` | 48 | RC check mocked: valid, "MOCK DEV OWNER", FIT, expiry 2030-12-31 |
| `FinancialVerificationService` | 35 | Banking gateway and name match skipped. Match score 1.0, status APPROVED |
| `BrandVerificationService` | 48, 91, 120 | GSTIN and bank verification bypassed; `isSuccess = true` |
| `VerificationController` | 56 | Document upload returns a fake `http://localhost:8080/mock-upload-url/...` instead of a real presigned URL — **uploads go nowhere** |

## Consequences worth knowing while on dev

- Driver identity is not verified. Any selfie, DL number or RC number onboards successfully.
- The biometric lockout path (three consecutive failures → suspend the driver) is downstream of a
  check that never fails, so it cannot trigger. The suspension itself was fixed on 2026-09-17 and is
  covered by `BiometricLockoutSuspensionTest`; it is simply unreachable on this profile.
- KYC documents are not stored. The upload URL is a placeholder.

## Before production

Setting `SPRING_PROFILES_ACTIVE` to something other than `dev`/`test` re-enables all six at once.
What that then requires:

- Real credentials for the DL, RC, GSTIN and banking providers
- `storageService` configured with working object-storage credentials for presigned uploads
- The biometric provider wired in — `simulateBiometricApi` is still a simulation on the non-dev
  branch, so that one needs a real implementation, not just a profile change

The last point is the trap: flipping the profile makes five of these six call real providers, but
biometric verification falls through to `simulateBiometricApi(selfieUrl)`, which passes unless the
URL contains the literal string "fail". Do not read a profile change as "biometrics are now real".
