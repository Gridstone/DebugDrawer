#!/usr/bin/env bash

if [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
  echo "Skipping snapshot deployment: was pull request."
elif [ "$TRAVIS_BRANCH" != "master" ]; then
  echo "Skipping snapshot deployment; not on master branch."
else
  echo "Deploying..."

  openssl aes-256-cbc -K $encrypted_c6d623064c7c_key -iv $encrypted_c6d623064c7c_iv -in .travis/my.travis.gpg.enc -out my.travis.gpg -d

  echo signing.keyId="${GPG_KEY_ID}" > "$HOME/.gradle/gradle.properties"
  echo signing.password="${GPG_KEY_PASSPHRASE}" >> "$HOME/.gradle/gradle.properties"
  echo signing.secretKeyRingFile="${TRAVIS_BUILD_DIR}/my.travis.gpg" >> "$HOME/.gradle/gradle.properties"

  ./gradlew uploadArchives --info --no-daemon --no-parallel

  echo "Deployed!"
fi
