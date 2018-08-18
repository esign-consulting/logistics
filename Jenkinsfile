node {
    def mvnHome
    def app
    stage('Preparation') {
        git 'https://github.com/esign-consulting/logistics.git'
        mvnHome = tool 'M3'
    }
    stage('Build') {
        sh "'${mvnHome}/bin/mvn' clean install -Dmaven.test.skip=true"
    }
    stage('Unit Tests') {
        sh "'${mvnHome}/bin/mvn' test"
    }
    stage('SonarQube') {
        withSonarQubeEnv('SonarQube') {
            sh "'${mvnHome}/bin/mvn' sonar:sonar"
        }
    }
    stage('Build Docker Image') {
        app = docker.build("esignbr/logistics")
    }
    stage('Results') {
        junit '**/target/surefire-reports/TEST-*.xml'
        archive 'target/*.jar'
    }
}
