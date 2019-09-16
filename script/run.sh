#!/bin/bash

ROOT_PATH=`pwd`
SOURCE_PATH="${ROOT_PATH}/src"
MAIN_PATH="${SOURCE_PATH}/main"

CURRENT_TIMESTAMP=$(date +%s)

TARGET_PATH="${ROOT_PATH}/target"

AGENT_PATH="${TARGET_PATH}/agent"
TARGET_CLASS_PATH="${TARGET_PATH}/classes"
TARGET_OUTPUT_PATH="${TARGET_PATH}/output"

# cd ${TARGET_PATH}
runnerClass () {
  RUNNER_PATH="${TARGET_PATH}/runner"

  if [ "${DEMETER_AGENT_MODE}" = "agent" ]; then
    if [ ! -f "${AGENT_PATH}/Agent.jar" ]; then
      echo "Agent.jar Not Find"
      exit 1
    fi

    java -javaagent:${AGENT_PATH}/Agent.jar -Dfile.encoding=UTF-8 -cp ${TARGET_CLASS_PATH} runner.Run
  else
    java -Dfile.encoding=UTF-8 -cp ${TARGET_CLASS_PATH} runner.Run
  fi
}

runnerJar () {
  MAIN_JAR_PATH="${TARGET_OUTPUT_PATH}/Runner.jar"
  if [ ! -f "${MAIN_JAR_PATH}" ]; then
    echo "Runner.jar Not Found"
    exit 1
  fi

  if [ "${DEMETER_AGENT_MODE}" = "agent" ]; then
    if [ ! -f "${AGENT_PATH}/Agent.jar" ]; then
      echo "Agent.jar Not Find"
      exit 1
    fi

    java -javaagent:${AGENT_PATH}/Agent.jar -Dfile.encoding=UTF-8 -cp ${MAIN_JAR_PATH} runner.Run
  else
    java -Dfile.encoding=UTF-8 -cp ${MAIN_JAR_PATH} runner.Run
  fi
}

if [ "${DEMETER_RUN_MODE}" = "jar" ]; then
  runnerJar
else
  runnerClass
fi
