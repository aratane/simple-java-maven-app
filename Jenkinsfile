pipeline {
    agent any

    options {
        skipStagesAfterUnstable()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    def mavenHome = tool 'Maven 3.9.6'
                    env.PATH = "${mavenHome}/bin:${env.PATH}"
                    
                    cache(lookup: 'maven', paths: ['$HOME/.m2/repository']) {
                        sh 'mvn -B -DskipTests clean package'
                    }
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    sh 'mvn test'
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh './jenkins/scripts/deploy.sh'
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline ran successfully!'
        }
        unstable {
            echo 'Pipeline ran with unstable stage(s).'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
