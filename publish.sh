#!/usr/bin/env bash

cmd="sh -x gradlew clean"
username="$1"
key="$2"

cmd=${cmd}" && sh -x gradlew snake:generatePomFileForReleasePublication"
cmd=${cmd}" snake:publishReleasePublicationToMavenLocal"

cmd=${cmd}" snake-annotations:generatePomFileForMavenPublication"
cmd=${cmd}" snake-annotations:publishMavenPublicationToMavenLocal"

cmd=${cmd}" snake-compiler:generatePomFileForMavenPublication"
cmd=${cmd}" snake-compiler:publishMavenPublicationToMavenLocal"

#cmd=${cmd}" && sh -x gradlew build bintrayUpload -PbintrayUser=${username} -PbintrayKey=${key} -PdryRun=false"

eval ${cmd}