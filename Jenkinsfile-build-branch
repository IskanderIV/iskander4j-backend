def project = [
        build: [
                manifest: 'cleverhause-backend/pom.xml'
        ]
]

pipeline {
    agent {
        label {
            'master'
        }
    }
    stages {
        stage('Clean') {
            steps {
                sh(script: "mvn clean -f ${project.build.manifest} -U")
            }
        }

        stage('Test') {
            steps {
                sh(script: "mvn test -f ${project.build.manifest} -U")
            }
        }

        stage('Build') {
            steps {
                sh(script: "mvn package -f ${project.build.manifest} -U")
            }
        }
    }
}