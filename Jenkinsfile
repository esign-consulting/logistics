node {
    def mvnHome
    def appDockerImage
    stage('Preparation') {
        git 'https://github.com/esign-consulting/logistics.git'
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
    stage('Results') {
        junit '**/target/surefire-reports/TEST-*.xml'
        archiveArtifacts '**/target/logistics-*.*ar'
    }
}
