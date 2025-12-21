pipeline {
    agent any

    environment {
        MAVEN_OPTS = "-Dmaven.repo.local=.m2/repository"
    }

    stages {

        stage('1- Checkout Code') {
            steps {
                git url: 'https://github.com/karacabugra27/hotel-reservation-system-ydg.git',
                    branch: 'master'
            }
        }

        stage('2- Build Project') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('3- Unit Tests') {
            steps {
                sh 'mvn test -DskipITs=true'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('4- Integration Tests') {
            steps {
                sh 'mvn test -DskipITs=false'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('5- Run System (Docker)') {
            steps {
                sh 'docker-compose down || true'
                sh 'docker-compose up -d --build'
                sh 'sleep 25'
            }
        }

        stage('6- Selenium Test 1 - Available Rooms') {
            environment {
                APP_BASE_URL = 'http://localhost:8080'
            }
            steps {
                sh 'mvn test -Dtest=AvailableRoomsSeleniumTest'
            }
        }


    }

    post {
        always {
            sh 'docker-compose down'
        }
        success {
            echo '✅ CI/CD başarıyla tamamlandı'
        }
        failure {
            echo '❌ Pipeline başarısız'
        }
    }
}