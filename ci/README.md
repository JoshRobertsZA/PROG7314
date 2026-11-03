# CI signing key

`ci-release.jks` is a throwaway keystore used only by the release workflow
(`.github/workflows/release.yml`) so the pipeline can produce a signed bundle
without any repository secrets.

| Setting | Value |
|---|---|
| Store password | `waypoint-ci` |
| Key alias | `ci` |
| Key password | `waypoint-ci` |

It is public on purpose and protects nothing. It is NOT the upload key. The real
key (`waypoint-release.jks`) stays on the maintainer's machine and is never
committed. A bundle signed with this CI key cannot be used to update the app on
Google Play.
