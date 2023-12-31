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
                    sh 'mvn sonar:sonar'
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

        stage('Deliver') {
            steps {
                script {
                    echo '=== Delivering the Application ==='
                    // Tambahkan langkah-langkah deliver sesuai kebutuhan Anda
                    // Misalnya, membuat artefak yang akan didistribusikan
                    sh './jenkins/scripts/deliver.sh'
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    echo '=== Deploying the Application ==='
                    // Tambahkan langkah-langkah deploy sesuai kebutuhan Anda
                    // Misalnya, mengunggah aplikasi ke server atau platform tujuan
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
            emailext body: "Jenkins Build Failed: ${currentBuild.fullDisplayName}",
                    recipientProviders: [culprits(), developers()],
                    subject: "Build Failure - ${currentBuild.fullDisplayName}"
        }
    }
}
