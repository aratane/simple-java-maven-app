pipeline {
    agent any

    environment {
        M2_HOME = tool 'Maven 3.9.6'
        PATH = "${M2_HOME}/bin:${PATH}"
    }

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
                    // Menggunakan Maven Wrapper untuk build
                    sh './mvnw clean package'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    sh './mvnw test'
                }
            }
            post {
                always {
                    // Publikasikan laporan tes pake JUnit
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Deliver') {
            steps {
                script {
                    sh './jenkins/scripts/deliver.sh'
                }
            }
        }
    }

    post {
        success {
            script {
                echo 'Pipeline ran successfully!'
            }
        }
        unstable {
            script {
                echo 'Pipeline ran with unstable stage(s).'
            }
        }
        failure {
            script {
                echo 'Pipeline failed!'
            }
        }
        cleanup {
            deleteDir()
        }
    }
}
