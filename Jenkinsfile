pipeline {
    agent any

    tools {
        sonarQube 'sonar-scanner'
    }

    environment {
        COMPOSE_FILE = "docker-compose.yml"
    }

    stages {

        stage('Clone Repository') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/PUSHPENDRACHAUDHARI/salesofficeworks.git'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh 'sonar-scanner'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Stop Old Containers') {
            steps {
                sh '''
                docker-compose down || true
                docker rm -f management-mysql || true
                '''
            }
        }

        stage('Build Docker Images') {
            steps {
                sh 'docker-compose build'
            }
        }

        stage('Deploy Application') {
            steps {
                sh 'docker-compose up -d'
            }
        }
    }

    post {
        success {
            echo "✅ Deployment Successful with Code Quality Check!"
        }
        failure {
            echo "❌ Pipeline Failed (Check SonarQube or Build Logs)"
        }
    }
}
