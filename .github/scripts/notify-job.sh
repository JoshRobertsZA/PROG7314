#!/usr/bin/env bash
# Posts a Discord message when one CI job finishes, e.g.
#   "PR 42 · Unit Tests 2/3 complete ✅"
# The N/total is derived live from the GitHub API (how many of this
# run's tracked jobs are already completed), so it's correct no matter
# which of the parallel jobs finishes first.
#
# Required env: DISCORD_WEBHOOK, GITHUB_TOKEN, GITHUB_REPOSITORY,
#               GITHUB_RUN_ID, JOB_NAME, JOB_STATUS, RUN_URL, REF_LABEL
# Optional env: TRACKED_JOBS (comma-separated job display names)
set -euo pipefail

TRACKED_JOBS="${TRACKED_JOBS:-Lint,Unit Tests,Assemble Debug APK}"
IFS=',' read -r -a TRACKED <<< "$TRACKED_JOBS"
TOTAL=${#TRACKED[@]}

# Count tracked jobs that have reached "completed" in this run.
JOBS_JSON=$(curl -s -H "Authorization: Bearer $GITHUB_TOKEN" \
  -H "Accept: application/vnd.github+json" \
  "https://api.github.com/repos/$GITHUB_REPOSITORY/actions/runs/$GITHUB_RUN_ID/jobs?per_page=50")

DONE=0
for name in "${TRACKED[@]}"; do
  status=$(echo "$JOBS_JSON" | jq -r --arg n "$name" '.jobs[] | select(.name==$n) | .status' | head -n1)
  if [ "$status" = "completed" ]; then DONE=$((DONE+1)); fi
done
# This job's own step runs before its status flips to "completed", so count it.
if ! echo "$JOBS_JSON" | jq -e --arg n "$JOB_NAME" '.jobs[] | select(.name==$n and .status=="completed")' >/dev/null; then
  DONE=$((DONE+1))
fi
[ "$DONE" -gt "$TOTAL" ] && DONE=$TOTAL

case "$JOB_STATUS" in
  success)   EMOJI="✅"; COLOR=3066993 ;;
  failure)   EMOJI="❌"; COLOR=15158332 ;;
  cancelled) EMOJI="⏹️"; COLOR=9807270 ;;
  *)         EMOJI="⚠️"; COLOR=16776960 ;;
esac

TITLE="$REF_LABEL · $JOB_NAME $DONE/$TOTAL complete $EMOJI"

curl -s -X POST "$DISCORD_WEBHOOK" \
  -H "Content-Type: application/json" \
  -d "$(jq -n --arg t "$TITLE" --arg d "[View run]($RUN_URL)" --argjson c "$COLOR" \
        '{embeds:[{title:$t, description:$d, color:$c}]}')"
