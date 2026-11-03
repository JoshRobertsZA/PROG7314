@echo off
rem Runs the same three checks as the GitHub Actions workflow (.github/workflows/android-ci.yml):
rem   1. Lint            ./gradlew lintDebug --stacktrace
rem   2. Unit tests      ./gradlew testDebugUnitTest --stacktrace
rem   3. Assemble debug  ./gradlew assembleDebug --stacktrace
rem Like CI, each check runs even if an earlier one fails, then a summary is shown.
setlocal
cd /d "%~dp0"

set "LINT=FAILED"
set "TESTS=FAILED"
set "BUILD=FAILED"

echo.
echo ===== 1 of 3: Lint =====
call gradlew.bat lintDebug --stacktrace
if not errorlevel 1 set "LINT=PASSED"

echo.
echo ===== 2 of 3: Unit tests =====
call gradlew.bat testDebugUnitTest --stacktrace
if not errorlevel 1 set "TESTS=PASSED"

echo.
echo ===== 3 of 3: Assemble debug APK =====
call gradlew.bat assembleDebug --stacktrace
if not errorlevel 1 set "BUILD=PASSED"

echo.
echo ================= SUMMARY =================
echo   Lint:         %LINT%
echo   Unit tests:   %TESTS%
echo   Debug build:  %BUILD%
echo ===========================================
echo.
echo Reports:
echo   Lint:   app\build\reports\lint-results-debug.html
echo   Tests:  app\build\reports\tests\testDebugUnitTest\index.html
echo.

set "EXITCODE=0"
if not "%LINT%"=="PASSED" set "EXITCODE=1"
if not "%TESTS%"=="PASSED" set "EXITCODE=1"
if not "%BUILD%"=="PASSED" set "EXITCODE=1"

if "%EXITCODE%"=="0" (echo All checks passed. CI should be green.) else (echo One or more checks failed. CI would fail too.)
echo.
pause
endlocal & exit /b %EXITCODE%
