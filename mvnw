#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Wrapper Unix script
# ----------------------------------------------------------------------------

export MAVEN_PROJECTBASEDIR="${0%/*}"
MAVEN_DIR="$HOME/.m2/wrapper/dists/apache-maven-3.9.9"

if [ ! -d "$MAVEN_DIR" ]; then
    echo "Downloading Maven..."
    mkdir -p "$MAVEN_DIR"
    curl -fsSL https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip -o "$MAVEN_DIR/maven.zip"
    unzip -q -o "$MAVEN_DIR/maven.zip" -d "$MAVEN_DIR"
fi

exec "$MAVEN_DIR/apache-maven-3.9.9/bin/mvn" "$@"
