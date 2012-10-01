#!/bin/bash
# Hudson script used to promote a nightly snapshot build to development milestone or stable release.

# these should not need to change
DESTINATION=tools@filemgmt.jboss.org:/downloads_htdocs/tools/
PARENT_FOLDER=soa-tooling/
OPERATION=COPY

# develoment or stable
BUILD_TYPE=$1

# indigo, juno, kepler, ...
TARGET_PLATFORM=$2

# 0.5.0.Beta3, 0.6.0.Final, ...
TARGET_FOLDER=$3

# switchyard, modeshape, droolsjbpm, ...
PROJECT_NAME=$4

# should be ${JOB_NAME}/all/repo, but may also be something else, eg., for SwitchYard-Tools, use ${JOB_NAME}/eclipse
if [[ ${JOB_NAME} ]]; then PUBLISH_PATH=${JOB_NAME//-publish}; PUBLISH_PATH=${PUBLISH_PATH//-promote}/all/repo; fi
if [[ $5 ]]; then PUBLISH_PATH=$5; fi

if [[ $4 ]]; then
  echo "mkdir ${BUILD_TYPE}" | sftp tools@filemgmt.jboss.org:/downloads_htdocs/tools/updates/
  echo "mkdir ${BUILD_TYPE}/${TARGET_PLATFORM}" | sftp tools@filemgmt.jboss.org:/downloads_htdocs/tools/updates/
  echo "mkdir ${BUILD_TYPE}/${TARGET_PLATFORM}/${PARENT_FOLDER}" | sftp tools@filemgmt.jboss.org:/downloads_htdocs/tools/updates/
  echo "mkdir ${BUILD_TYPE}/${TARGET_PLATFORM}/${PARENT_FOLDER}${PROJECT_NAME}" | sftp tools@filemgmt.jboss.org:/downloads_htdocs/tools/updates/
  if [[ ${OPERATION} ==  "MOVE" ]]; then
    echo -e "rename builds/staging/${PUBLISH_PATH} updates/${BUILD_TYPE}/${TARGET_PLATFORM}/${PARENT_FOLDER}${PROJECT_NAME}/${TARGET_FOLDER}" | sftp ${DESTINATION}
  else
    rsync -arzq --protocol=28 ${DESTINATION}/builds/staging/${PUBLISH_PATH}/* ${WORKSPACE}/${JOB_NAME}/
    rsync -arzq --protocol=28 --delete ${WORKSPACE}/${JOB_NAME}/* ${DESTINATION}/updates/${BUILD_TYPE}/${TARGET_PLATFORM}/${PARENT_FOLDER}${PROJECT_NAME}/${TARGET_FOLDER}/
  fi
  echo "Site promoted by ${OPERATION} to: http://download.jboss.org/jbosstools/updates/${BUILD_TYPE}/${TARGET_PLATFORM}/${PARENT_FOLDER}${PROJECT_NAME}/${TARGET_FOLDER}/"
else
  echo "Usage  : $0 \${BUILD_TYPE} \${TARGET_PLATFORM} \${TARGET_FOLDER} \${PROJECT_NAME} [PUBLISH_PATH]"
  echo "Example: $0 development juno 3.3.0.Beta3 modeshape"
  echo "Example: $0 stable indigo 0.5.0.Final switchyard ${JOB_NAME}/eclipse"
  exit 1
fi

# JBIDE-12662: regenerate composite metadata in updates/${BUILD_TYPE}/${TARGET_PLATFORM}/${PARENT_FOLDER}${PROJECT_NAME} folder for all children
wget http://anonsvn.jboss.org/repos/jbosstools/trunk/build/util/cleanup/jbosstools-cleanup.sh --no-check-certificate
chmod +x jbosstools-cleanup.sh
./jbosstools-cleanup.sh --dirs-to-scan "updates/${BUILD_TYPE}/${TARGET_PLATFORM}/${PARENT_FOLDER}${PROJECT_NAME}" --regen-metadata-only
rm -f jbosstools-cleanup.sh
