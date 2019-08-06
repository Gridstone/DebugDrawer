Releasing
=========

 1. Change the version in `gradle.properties` to a non-SNAPSHOT version.
 2. Update the `CHANGELOG.md` for the impending release.
 3. Update the `README.md` with the new version.
 4. `git commit -am "Release X.Y."` (where X.Y is the new version)
 5. `git tag -a X.Y -m "Version X.Y"` (where X.Y is the new version)
 6. `git push && git push --tags`
 7. Login to https://oss.sonatype.org and close the staged repository.
 8. Update the `gradle.properties` to the next SNAPSHOT version.
 9. `git commit -am "Prepare next development version."`
 10. `git push`
