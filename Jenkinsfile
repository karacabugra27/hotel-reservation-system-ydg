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
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('4- Integration Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Cleanup') {
            steps {
                sh '''
                docker rm -f hotel-postgres || true
                docker rm -f hotel-app || true
                '''
            }
        }

        stage('5- Run System (Docker)') {
            steps {
                sh 'docker-compose down || true'
                sh 'docker-compose up -d --build'
                sh 'sleep 20'
            }
        }

        stage('DEBUG - Inside Container') {
            steps {
                sh 'docker exec hotel-app ps aux'
                sh 'docker exec hotel-app netstat -tulpn || true'
            }
        }

        stage('HEALTH') {
            steps {
            sh 'curl -v http://localhost:8080/health || true'
            sh 'curl -v http://localhost:8080/actuator/health || true'
            }
        }


        stage('6- Selenium Test 1 - Available Rooms') {
            steps {
                sh '''
                docker exec hotel-app mvn test -Dtest=AvailableRoomsSeleniumTest
                '''
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