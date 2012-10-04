#!/bin/bash
# Hudson script used to promote a nightly snapshot build to development milestone or stable release.

# these should not need to change
DESTINATION=tools@filemgmt.jboss.org:/downloads_htdocs/tools/
OPERATION=COPY

if [[ $# -lt 1 ]]; then
  echo "Usage  : $0 -BUILD_TYPE build_type -TARGET_PLATFORM target_platform -PROJECT_NAME project_name -TARGET_FOLDER target_folder -SOURCE_PATH source_path"
  echo "Example: $0 -BUILD_TYPE integration -TARGET_PLATFORM juno -PROJECT_NAME base -TARGET_FOLDER as_4.0.juno -SOURCE_PATH jbosstools-4.0_stable_branch.component--as/all/repo"
  echo "Example: $0 -BUILD_TYPE integration -TARGET_PLATFORM juno -PROJECT_NAME base -TARGET_FOLDER archives_4.0.juno -SOURCE_PATH jbosstools-4.0_stable_branch.component--archives/all/repo"
  echo "Example: $0 -BUILD_TYPE integration -TARGET_PLATFORM juno -PROJECT_NAME base -TARGET_FOLDER jmx_4.0.juno -SOURCE_PATH jbosstools-4.0_stable_branch.component--jmx/all/repo"
  echo "Example: $0 -BUILD_TYPE development -TARGET_PLATFORM juno -PROJECT_NAME modeshape -TARGET_FOLDER 3.3.0.Beta3 -SOURCE_PATH modeshape-tools-continuous/all/repo"
  echo "Example: $0 -BUILD_TYPE stable -TARGET_PLATFORM indigo -PROJECT_NAME switchyard -TARGET_FOLDER 0.5.0.Final -SOURCE_PATH SwitchYard-Tools/eclipse"
  exit 1
fi

# read commandline args
while [[ "$#" -gt 0 ]]; do
  case $1 in
    '-SOURCE_PATH') SOURCE_PATH="$2"; shift 1;; # jbosstools-4.0_stable_branch.component--as/all/repo, modeshape-tools-continuous/all/repo, SwitchYard-Tools/eclipse

    '-BUILD_TYPE') BUILD_TYPE="$2"; shift 1;; # integration, develoment or stable
    '-TARGET_PLATFORM') TARGET_PLATFORM="$2"; shift 1;; # indigo, juno, kepler, ...
    '-PARENT_FOLDER') PARENT_FOLDER="$2"; shift 1;; # soa-tooling, core
    '-PROJECT_NAME') PROJECT_NAME="$2"; shift 1;; # switchyard, modeshape, droolsjbpm, ...
    '-TARGET_FOLDER') TARGET_FOLDER="$2"; shift 1;; # 0.5.0.Beta3, 0.6.0.Final, ...
  esac
  shift 1
done

if [[ ${PROJECT_NAME} ]]; then
  echo "mkdir ${BUILD_TYPE}" | sftp tools@filemgmt.jboss.org:/downloads_htdocs/tools/updates/
  echo "mkdir ${BUILD_TYPE}/${TARGET_PLATFORM}" | sftp tools@filemgmt.jboss.org:/downloads_htdocs/tools/updates/
  echo "mkdir ${BUILD_TYPE}/${TARGET_PLATFORM}/${PARENT_FOLDER}" | sftp tools@filemgmt.jboss.org:/downloads_htdocs/tools/updates/
  echo "mkdir ${BUILD_TYPE}/${TARGET_PLATFORM}/${PARENT_FOLDER}/${PROJECT_NAME}" | sftp tools@filemgmt.jboss.org:/downloads_htdocs/tools/updates/
  if [[ ${OPERATION} ==  "MOVE" ]]; then
    echo -e "rename builds/staging/${SOURCE_PATH} updates/${BUILD_TYPE}/${TARGET_PLATFORM}/${PARENT_FOLDER}/${PROJECT_NAME}/${TARGET_FOLDER}" | sftp ${DESTINATION}
  else
    # purge existing workspace folder to ensure we're not combining releases
    if [[ ${WORKSPACE} ]] && [[ -d ${WORKSPACE}/${JOB_NAME} ]]; then rm -fr ${WORKSPACE}/${JOB_NAME}/; fi
    rsync -arzq --protocol=28 ${DESTINATION}/builds/staging/${SOURCE_PATH}/* ${WORKSPACE}/${JOB_NAME}/
    rsync -arzq --protocol=28 --delete ${WORKSPACE}/${JOB_NAME}/* ${DESTINATION}/updates/${BUILD_TYPE}/${TARGET_PLATFORM}/${PARENT_FOLDER}/${PROJECT_NAME}/${TARGET_FOLDER}/
  fi
  echo "Site promoted by ${OPERATION} to: http://download.jboss.org/jbosstools/updates/${BUILD_TYPE}/${TARGET_PLATFORM}/${PARENT_FOLDER}/${PROJECT_NAME}/${TARGET_FOLDER}/"
fi

# JBIDE-12662: regenerate composite metadata in updates/${BUILD_TYPE}/${TARGET_PLATFORM}/${PARENT_FOLDER}/${PROJECT_NAME} folder for all children
wget http://anonsvn.jboss.org/repos/jbosstools/trunk/build/util/cleanup/jbosstools-cleanup.sh --no-check-certificate
chmod +x jbosstools-cleanup.sh
./jbosstools-cleanup.sh --dirs-to-scan "updates/${BUILD_TYPE}/${TARGET_PLATFORM}/${PARENT_FOLDER}" --regen-metadata-only
rm -f jbosstools-cleanup.sh
