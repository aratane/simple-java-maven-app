pipeline {
    agent any

    options {
        skipStagesAfterUnstable()
    }

    environment {
        // Simpan nilai PATH
        originalPath = sh(script: 'echo $PATH', returnStdout: true).trim()
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
                    
                    // Cache dependensi Maven
                    cache(type: 'Maven', path: "${HOME}/.m2/repository") {
                        // Build proyek Maven
                        sh 'mvn -B -DskipTests clean package'
                    }
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    // Setel ulang PATH
                    env.PATH = "${originalPath}"
                    sh 'mvn test'
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
                    // Setel ulang PATH
                    env.PATH = "${originalPath}"
                    sh './jenkins/scripts/deliver.sh'
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
