node {
    def mvnHome
    def appDockerImage
    def publicIp
    stage('Preparation') {
        checkout([
            $class: 'GitSCM',
            branches: scm.branches,
            doGenerateSubmoduleConfigurations: false,
            extensions: scm.extensions + [[$class: 'SubmoduleOption', parentCredentials: true, recursiveSubmodules: true]],
            submoduleCfg: [],
            userRemoteConfigs: scm.userRemoteConfigs
        ])
        mvnHome = tool 'M3'
    }
    stage('Build and Unit Tests') {
        sh "'${mvnHome}/bin/mvn' clean install"
    }
    stage('Static Code Analysis') {
        withSonarQubeEnv('SonarQube') {
            sh "'${mvnHome}/bin/mvn' sonar:sonar"
        }
    }
    stage('Deploy Java Artifacts') {
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'ossrh', usernameVariable: 'OSSRH_USER', passwordVariable: 'OSSRH_PASSWORD']]) {
            sh "'${mvnHome}/bin/mvn' -s .travis.settings.xml source:jar deploy -DskipTests=true"
        }
    }
    stage('Build Docker Image') {
        appDockerImage = docker.build("esignbr/logistics")
    }
    stage('Deploy Docker Image') {
        docker.withRegistry("", "dockerhub") {
            appDockerImage.push()
        }
    }
    stage('Deploy Logistics to AWS') {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
            sh "ansible-galaxy install codeyourinfra.docker_compose"
            ansiblePlaybook(playbook: 'deploy-to-aws.yml')
            publicIp = sh(
                script: "awk '/aws:/ {getline; print}' inventory.yml | grep -oE '([0-9]{1,3}\\.){3}[0-9]{1,3}'",
                returnStdout: true,
            )
        }
    }
    stage('Tests against AWS') {
        parallel 'UI Tests': {
            stage('UI Tests') {
                sh "'${mvnHome}/bin/mvn' -f test-selenium test -Dwebdriver.gecko.driver=/usr/bin/geckodriver/geckodriver -Dheadless=true -Dpage.url=http://${publicIp}:8080/logistics"
            }
        }, 'API Tests': {
            stage('API Tests') {
                sh "'${mvnHome}/bin/mvn' -f test-restassured test -Dserver.port=8080 -Dserver.host=http://${publicIp}"
            }
        }, 'Integration Tests': {
            stage('Integration Tests') {
                sh "'${mvnHome}/bin/mvn' -f test-arquillian test -Dhost=${publicIp}"
            }
        }
    }
    stage('Undeploy Logistics fom AWS') {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
            ansiblePlaybook(playbook: 'undeploy-from-aws.yml')
        }
    }
    stage('Deploy Logistics to esign.com.br') {
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'esign.com.br', usernameVariable: 'ESIGN_USER', passwordVariable: 'ESIGN_PASSWORD']]) {
            def remote = [:]
            remote.name = "deploy-logistics"
            remote.host = "esign.com.br"
            remote.user = "${env.ESIGN_USER}"
            remote.password = "${env.ESIGN_PASSWORD}"
            remote.allowAnyHosts = true
            sshPut remote: remote, from: 'logistics-ear/target/logistics-ear-1.0-SNAPSHOT.ear', into: 'appservers/wildfly-10.0.0.Final/standalone/deployments'
        }
    }
    stage('Results') {
        junit '**/target/surefire-reports/TEST-*.xml'
        archiveArtifacts '**/target/logistics-*.*ar'
    }
}
