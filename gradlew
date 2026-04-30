workflows:
android-build:
name: Android Build
max_build_duration: 60

```
environment:
  java: 17

scripts:
  - name: Build Debug APK
    script: |
      set -e

      echo "=== FIX gradlew (CRLF issue) ==="
      tr -d '\r' < gradlew > gradlew_fixed
      mv gradlew_fixed gradlew

      chmod +x gradlew

      echo "=== JAVA VERSION ==="
      java -version

      echo "=== GRADLE VERSION ==="
      ./gradlew --version

      echo "=== BUILD START ==="
      ./gradlew assembleDebug --stacktrace

artifacts:
  - app/build/outputs/**/*.apk
```
