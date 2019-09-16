#!/bin/bash

SOURCE_PATH=`pwd`
MAIN_PATH="${SOURCE_PATH}/main"

CURRENT_TIMESTAMP=$(date +%s)

TARGET_PATH="${SOURCE_PATH}/target"

AGENT_PATH="${TARGET_PATH}/agent"
TARGET_CLASS_PATH="${TARGET_PATH}/classes"
OUTPUT_PATH="${TARGET_PATH}/output"

# cd ${TARGET_PATH}
package () {
  if [ ! -d "${TARGET_CLASS_PATH}" ]; then
    echo "classpath Not Found"
    exit 1
  fi

  if [ -d "${OUTPUT_PATH}" ]; then
    rm -rf ${OUTPUT_PATH}
  fi
  mkdir ${OUTPUT_PATH}

  cd ${TARGET_CLASS_PATH}
  jar -cvf ${OUTPUT_PATH}/Runner.jar *
}
package
