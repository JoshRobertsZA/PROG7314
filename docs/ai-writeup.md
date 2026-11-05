# AI usage

AI coding assistants were used selectively during development. This covers what they
were used for, how their output was checked, and where that's cited.

## Tools and what they were used for

**Anthropic Claude**, via [Claude Code](https://claude.com/claude-code), for larger,
multi-file tasks: implementing a planned feature end to end from a short brief (e.g. the
email sign-up/sign-in screens, the offline sync outbox, the API test suite), writing
unit and integration tests against existing code, drafting documentation from the actual
code and git history, and debugging from pasted logcat output and stack traces.

**Gemini**, via Android Studio's inline code completion, for small, single-line
suggestions while typing (boilerplate like `data class` fields, import statements,
repetitive `when` branches).

## How output was checked

Every AI-assisted change went through the same path as hand-written code before
reaching `dev`:

1. The team member driving the session read and understood each change before accepting
   it, not merged blindly
2. It was tested: unit tests (`./gradlew testDebugUnitTest`, `npm test` in `api/`) and,
   for UI or behaviour changes, a manual run on a physical device
3. Bugs found during manual testing (e.g. a layout overlap in `EmailAuthScreen`, a
   `DELETE /account` 404 traced to a stale Cloud Run deployment) were fixed and
   re-verified the same way before being committed
4. It was committed under a team member's own authorship, and went through the same
   [CI pipeline](https://github.com/JoshRobertsZA/PROG7314/actions) - lint, unit tests,
   API tests, build - and a teammate's PR review, as every other change; nobody merges
   their own PR
5. For the API test suite specifically, a genuine bug in the test setup itself (a Jest
   manual mock required via the wrong path, so tests silently ran against an unmocked
   Firebase client) was caught because the first test run failed with an unexpected
   500, not because the AI flagged it

## What AI was *not* used for

- UI/UX design decisions and the overall architecture (MVVM, offline-first sync design,
  which API to use for what)
- Business logic correctness - suggested logic was read, tested, and understood before
  being kept, not trusted as-is
- Git actions with real consequences - branch creation, pushes, merges, and edits to CI
  workflow files all required a team member's explicit go-ahead, never inferred from an
  agreeable reply
- Any assessment content outside code and documentation - screenshots, the demo video,
  and this write-up's factual claims are the team's own work

## Citation

Anthropic (2026) *Claude Code*. Available at: https://claude.com/claude-code (Accessed:
3 November 2026).

Google (2024) *Gemini in Android Studio*. Available at:
https://developer.android.com/studio/gemini/overview (Accessed: 3 November 2026).
