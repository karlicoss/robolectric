#!/bin/bash
set -e
set -x
mvn -DaltDeploymentRepository=repo::default::file:../robolectric-mvn-repo clean deploy -Dmaven.test.skip=true
echo "Do not forget to commit & push!"