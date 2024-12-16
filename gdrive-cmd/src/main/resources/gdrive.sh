#!/bin/bash

GDRIVE_DIR=.
GDRIVE_ARTIFACT=${project.artifactId}-${project.version}

java -jar $GDRIVE_DIR/lib/$GDRIVE_ARTIFACT-spring-boot.jar "$@"
