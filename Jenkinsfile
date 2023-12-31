pipeline {
    agent any

    options {
        skipStagesAfterUnstable()
    }

    stages {
        stage('Security Checks') {
            steps {
                script {
                    // Lakukan pemeriksaan keamanan
                    sh 'mvn dependency-check:check'
                }
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Validate Dependencies') {
            steps {
                script {
                    // Validasi dan otomatisasi pembaruan dependensi
                    sh 'mvn dependency:analyze'
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    // Menggunakan image Maven untuk build
                    def mavenHome = tool 'Maven 3.9.6'
                    env.PATH = "${mavenHome}/bin:${env.PATH}"
                    
                    // Build proyek Maven
                    sh 'mvn -B -DskipTests clean package'
                }
            }
        }

        stage('Code Analysis') {
            steps {
                script {
                    // Analisis kode dan laporan kualitas
                    Contoh: sh 'mvn sonar:sonar'
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

        stage('Deliver') {
            steps {
                script {
                    echo '=== Delivering the Application ==='
                    sh './jenkins/scripts/deliver.sh'
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    echo '=== Deploying the Application ==='
                    sh './jenkins/scripts/deploy.sh'
                }
            }
        }

        stage('Integration Test') {
            steps {
                script {
                    // Pastikan aplikasi telah di-deploy sebelum menjalankan uji integrasi
                    // (Asumsi bahwa 'Deploy' telah berhasil dieksekusi sebelumnya)
                    echo '=== Running Integration Tests ==='
                    sh 'mvn verify -Pintegration-tests'
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
            emailext body: "Jenkins Build Failed: ${currentBuild.fullDisplayName}",
                    recipientProviders: [culprits(), developers()],
                    subject: "Build Failure - ${currentBuild.fullDisplayName}"
        }
    }
}
