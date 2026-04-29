workflows:
  android-build:
    name: Android Build
    max_build_duration: 60

    environment:
      java: 17

    scripts:
      - name: Build Debug APK
        script: |
          set -e

          tmp_file="$(mktemp)"
          tr -d '\r' < gradlew > "$tmp_file"
          mv "$tmp_file" gradlew

          chmod +x gradlew

          echo "=== JAVA VERSION ==="
          java -version

          echo "=== GRADLE WRAPPER VERSION ==="
          ./gradlew --version

          echo "=== START BUILD ==="
          ./gradlew assembleDebug --stacktrace --info 2>&1 | tee build_full.log

    artifacts:
      - app/build/outputs/**/*.apk
      - build_full.log
