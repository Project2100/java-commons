/*
 *
 */

plugins {
    // Apply the java-library plugin to add support for Java Library
    `java-library`

    // Required by JitPack.io for publishing library artifact through GitHub repo
    `maven-publish`
}

group = "com.github.project2100";

// This section is required to force an actual publication by the maven publishing task
// See https://github.com/jitpack/jitpack.io/issues/4110 for related issue
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
