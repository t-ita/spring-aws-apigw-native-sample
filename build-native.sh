#!/bin/bash

docker run --memory=8g -v "$(pwd)":/work/build --rm -i awslambda-native-builder:latest /bin/bash <<EOF
source "/root/.sdkman/bin/sdkman-init.sh"
mvn -Pnative-image clean package -Dmaven.test.skip=true
chmod 775 ./src/shell/bootstrap
zip ./target/SpringAwsApigwNativeSampleApplication -j ./src/shell/bootstrap ./target/SpringAwsApigwNativeSampleApplication
EOF
