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
                    
                    // Build proyek Maven
                    sh 'mvn -B -DskipTests clean package'
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
                    // Publikasikan laporan tes pake JUnit
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Manual Approval') {
            steps {
                script {
                    // Menampilkan input
                    def userInput = input(
                        id: 'userInput',
                        message: 'Lanjutkan ke tahap Deploy?',
                        parameters: [
                            [$class: 'ChoiceParameterDefinition', 
                             choices: 'Proceed\nAbort', 
                             description: 'Pilih tindakan:', 
                             name: 'ACTION']
                        ]
                    )

                    // Cek pilihan
                    if (userInput == 'Proceed') {
                        echo 'Pengguna memilih untuk melanjutkan ke tahap Deploy.'
                    } else {
                        error 'Pengguna memilih untuk membatalkan eksekusi pipeline.'
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh './jenkins/scripts/deliver.sh'
                }
                // sleep setelah deployment
                sleep(time: 1, unit: 'MINUTES')
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
