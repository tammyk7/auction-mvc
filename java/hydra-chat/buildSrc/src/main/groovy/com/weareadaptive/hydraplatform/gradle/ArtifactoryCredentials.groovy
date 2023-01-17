package com.weareadaptive.hydraplatform.gradle

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import org.gradle.api.Project

@Canonical
@CompileStatic
class ArtifactoryCredentials {

  String username
  String password

  /**
   * Attempts to load credentials from the CI environment using the variables 'ARTIFACTORY_USER'  / 'ARTIFACTORY_USR'
   *  and 'ARTIFACTORY_PWD' / 'ARTIFACTORY_PSW'
   *
   * If the CI credentials are not set then we attempt to load credentials from the project properties
   * 'adaptive_username' and 'adaptive_password'
   *
   * @param project Project to load credentials from
   * @return Artifactory credentials for the project
   */
  static ArtifactoryCredentials load(Project project) {
    def envmap = System.getenv()

    String username = envmap.containsKey('ARTIFACTORY_USER') ? envmap['ARTIFACTORY_USER'] :
            envmap.containsKey('ARTIFACTORY_USR') ? envmap['ARTIFACTORY_USR'] :
                    project.properties['adaptive_username']
    String password = envmap.containsKey('ARTIFACTORY_PWD') ? envmap['ARTIFACTORY_PWD'] :
            envmap.containsKey('ARTIFACTORY_PSW') ? envmap['ARTIFACTORY_PSW'] :
                    project.properties['adaptive_password']

    new ArtifactoryCredentials(username, password)
  }
}
