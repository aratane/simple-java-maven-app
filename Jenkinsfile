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
                    def APP_NAME = "my-app"
                    def JAR_FILE = "/var/jenkins_home/workspace/submission-cicd-pipeline-sanjidev/target/${APP_NAME}-1.0-SNAPSHOT.jar"
                    def DESTINATION_DIR = "/var/jenkins_home/workspace/submission-cicd-pipeline-sanjidev"
                    def LOG_FILE = "${DESTINATION_DIR}/${APP_NAME}.log"

                    if (!fileExists(JAR_FILE)) {
                        error "File JAR tidak ditemukan: ${JAR_FILE}"
                    }

                    if (fileExists("${DESTINATION_DIR}/${APP_NAME}.jar")) {
                        sh "mv ${DESTINATION_DIR}/${APP_NAME}.jar ${DESTINATION_DIR}/${APP_NAME}-backup-\$(date +'%Y%m%d%H%M%S').jar"
                    }

                    sh "cp ${JAR_FILE} ${DESTINATION_DIR}/${APP_NAME}.jar || exit 1"
                    sh "echo 'Deploy sukses pada: \$(date)' >> /var/jenkins_home/workspace/submission-cicd-pipeline-sanjidev/my-app.log"
                    echo "Deploy sukses! Aplikasi berjalan di ${DESTINATION_DIR}/${APP_NAME}.jar"
                }
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
