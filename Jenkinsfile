pipeline {

    agent any

    tools {
        maven 'Maven'
    }

    environment {
        SONAR_HOME = tool 'SonarScanner'
        COMPOSE_FILE = "docker-compose.yml"
    }

    stages {

        stage('Clone Repository') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/PUSHPENDRACHAUDHARI/salesofficeworks.git'
            }
        }

        stage('Fix Permissions') {
            steps {
                sh 'chmod +x management/mvnw'
            }
        }

        stage('Build Backend') {
            steps {
                dir('management') {
                    sh './mvnw clean install -DskipTests'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh "$SONAR_HOME/bin/sonar-scanner"
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
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
