#!/bin/bash

ROOT_PATH=`pwd`
SOURCE_PATH="${ROOT_PATH}/src"
MAIN_PATH="${SOURCE_PATH}/main"
JAVA_SOURCE_PATH="${MAIN_PATH}/java"
PROCESSOR_PATH="${MAIN_PATH}/procesor"
AGENT_PATH="${MAIN_PATH}/agent"
AGENT_META_PATH="${AGENT_PATH}/META-INF"

CURRENT_TIMESTAMP=$(date +%s)

TARGET_PATH="${ROOT_PATH}/target"
TARGET_CLASS_PATH="${TARGET_PATH}/classes"

if [ ! -d "${TARGET_PATH}" ]; then
  mkdir "${TARGET_PATH}"
else
  touch "${TARGET_PATH}/temp_${CURRENT_TIMESTAMP}"
  rm -r ${TARGET_PATH}/*
fi
mkdir "${TARGET_CLASS_PATH}"

if [ ! -d "${MAIN_PATH}" ]; then
  echo "Main Path Is Not Exist"
  exit 1
fi

if [ ! -d "${JAVA_SOURCE_PATH}" ]; then
  echo "Source Path Is Not Exist"
  exit 1
fi


buildAgent () {
  if [ ! -f "${AGENT_PATH}/Agent.java" ]; then
    echo "Agent File Not Find"
    exit 1
  fi

  if [ -d "${AGENT_META_PATH}" ]; then
    # echo "${AGENT_META_PATH} ${TARGET_PATH}/agent/META-INF"
    mkdir ${TARGET_PATH}/agent
    cp -r ${AGENT_META_PATH} "${TARGET_PATH}/agent/META-INF"
  fi

  previousPath=`pwd`
  cd ${AGENT_PATH}
  javac Agent.java -d ${TARGET_PATH}/agent
  cd ${TARGET_PATH}/agent
  jar -cvfm ${TARGET_PATH}/agent/Agent.jar ${TARGET_PATH}/agent/META-INF/MANIFEST.MF *.class
  cd ${previousPath}
}

# Build AnnotationProcessor
BUILD_PROCESSOR_COMMAND="javac -cp ${PROCESSOR_PATH} -d ${TARGET_CLASS_PATH} ${PROCESSOR_PATH}/**/*.java"
echo "${BUILD_PROCESSOR_COMMAND}"
$(${BUILD_PROCESSOR_COMMAND})

# 统计文件 `META-INF/services/javax.annotation.processing.Processor` 的行数
LINE_NUM=$(cat ${JAVA_SOURCE_PATH}/META-INF/services/javax.annotation.processing.Processor | wc -l)
LINE_NUM=$((LINE_NUM+1))

PROCESSORS=$(cat ${JAVA_SOURCE_PATH}/META-INF/services/javax.annotation.processing.Processor | awk '{ { printf $0 } if(NR < "'"${LINE_NUM}"'") { printf "," } }')

# Build Main-Class with processor
BUILD_MAIN_COMMAND="javac -cp ${JAVA_SOURCE_PATH}:${TARGET_CLASS_PATH} -d ${TARGET_CLASS_PATH} -processor ${PROCESSORS} ${JAVA_SOURCE_PATH}/**/*.java"
echo "${BUILD_MAIN_COMMAND}"
$(${BUILD_MAIN_COMMAND})
# -proc:none

if [ "${DEMETER_BUILD_MODE}" = "agent" ]; then
  buildAgent
fi
