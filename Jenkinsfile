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
 
        stage('Build Backend') {

            steps {

                dir('management') {

                    sh 'mvn clean install'

                }

            }

        }
 
        stage('SonarQube Analysis') {

            steps {

                withSonarQubeEnv('SonarQube') {   // ⚠️ name same hona chahiye Jenkins config jaisa

                    sh '''

                    $SONAR_HOME/bin/sonar-scanner \

                    -Dsonar.projectKey=salesofficeworks \

                    -Dsonar.projectName=SalesOfficeWorks \

                    -Dsonar.sources=. \

                    -Dsonar.java.binaries=management/target/classes \

                    -Dsonar.sourceEncoding=UTF-8

                    '''

                }

            }

        }
 
        stage('Quality Gate') {

            steps {

                timeout(time: 3, unit: 'MINUTES') {

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
 
