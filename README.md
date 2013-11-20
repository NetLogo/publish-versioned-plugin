# publish-versioned Plugin

This plugin provides a `publish-versioned` SBT console command, which performs `publish` with a dynamically-determined `version` string, relying up two SBT settings: `version` and `isSnapshot`.  If `isSnapshot` is `false`, your artifacts will be published using the original setting for `version`.  Otherwise, the plugin will call out to Git to determine the SHA of the current commit and append that to the version number, and will also append "-dirty" to the version number if the Git repo has uncommitted changes.

There is also a `publish-local-versioned` for doing a similar thing with `publish-local` instead of `publish`.

## Example Artifact Names

(Assume that `name` is `CoolArtifact` and `version` is `1.0` for all of the following)

* **CoolArtifact-1.0.jar** (`isSnapshot` is `false`)
* **CoolArtifact-1.0-abc123d.jar** (`isSnapshot` is `true`; Git HEAD SHA is `abc123d`)
* **CoolArtifact-1.0-abc123d-dirty.jar** (`isSnapshot` is `true`; Git HEAD SHA is `abc123d`; uncommitted changes to tracked files in Git)

## Motivation Behind Design

We, the authors of this plugin, like having reproducible builds, which requires having well-versioned dependencies, and so we'd like to publish artifacts from our projects as well-versioned dependencies of other projects.  The version-naming scheme described above allows for the "well-versioned" part of that pretty well, and the inclusion of the `-dirty` suffix allows us to do it without *forcing* people to make commits if they just want to try something else, while simultaneously sending the signal that such an artifact is not to be depended upon in any real commit.  Great—wonderful—there's just one minor problem: SBT doesn't seem to make this totally easy to implement.

`publish` in SBT uses `version`, but `version` is an eagerly-evaluated setting (rather than a task).  So, basically, whatever `version` was set to when SBT was loaded up (or last `reload`ed) is the `version` value that it will use for all attempts at publishing.  This is... very annoying.  It would be sufficient to require that users always have to reload before publishing, but there's no obvious way of enforcing that in SBT, and anyone would forgets to do that would compromise the entire goal of the plugin (sanity through properly-versioned dependencies and reproducible builds).  So... the best solution that we've found within the constraints of SBT is to add this somewhat-kludgy command to the build to make things operate how we want them to.  It's not wonderful or perfect, but it's currently good enough for our purposes.

**Credit where credit's due**: Our solution is based upon Rogach's self-accepted StackOverflow answer [here](http://stackoverflow.com/questions/14262798/how-to-change-a-version-setting-inside-a-single-sbt-command).

## Building

Simply run the `package` SBT command to build a new version of the plugin `.jar`.  Then, set your SBT project's `plugins.sbt` to reference/fetch the `.jar`.

## Terms of Use

[![CC0](http://i.creativecommons.org/p/zero/1.0/88x31.png)](http://creativecommons.org/publicdomain/zero/1.0/)

The publish-versioned plugin is in the public domain.  To the extent possible under law, Uri Wilensky has waived all copyright and related or neighboring rights.
