pipeline {
    agent any

    environment {
        MAVEN_OPTS = "-Dmaven.repo.local=.m2/repository"
        PATH = "/opt/homebrew/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin"
    }

    options {
        skipDefaultCheckout(true)
    }

    triggers {
        githubPush()
    }

    stages {
stage('0- Docker Check') {
    steps {
        sh 'echo "PATH=$PATH"'
        sh 'ls -l /opt/homebrew/bin/docker || true'
        sh 'which docker || true'
        sh 'docker --version'
    }
}
        stage('1- Checkout Code') {
            steps {
                git url: 'https://github.com/karacabugra27/hotel-reservation-system-ydg.git',
                    branch: 'master'
            }
        }

        stage('2- Build Project') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean compile'
            }
        }

        stage('3- Unit Tests') {
            steps {
                sh './mvnw -Dtest=**/service/*Test,**/controller/*Test test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('4- Integration Tests') {
            steps {
                sh './mvnw -Dtest=**/integration/*Test,*ApplicationTests test'
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
                sh 'docker compose down -v || true'
                sh 'docker compose up -d --build'
                sh 'sleep 30'
            }
        }

        stage('6- Selenium Smoke Tests') {
            steps {
                sh 'APP_BASE_URL=http://localhost:5173 SELENIUM_HEADLESS=true ./mvnw -DskipTests verify -Dit.test=SeleniumSmokeSuite'
            }
            post {
                always {
                    junit '**/target/failsafe-reports/*.xml'
                }
            }
        }

        stage('7- Selenium Reservation Tests') {
            steps {
                sh 'APP_BASE_URL=http://localhost:5173 SELENIUM_HEADLESS=true ./mvnw -DskipTests verify -Dit.test=SeleniumReservationSuite'
            }
            post {
                always {
                    junit '**/target/failsafe-reports/*.xml'
                }
            }
        }

        stage('8- Selenium Lookup Tests') {
            steps {
                sh 'APP_BASE_URL=http://localhost:5173 SELENIUM_HEADLESS=true ./mvnw -DskipTests verify -Dit.test=SeleniumLookupSuite'
            }
            post {
                always {
                    junit '**/target/failsafe-reports/*.xml'
                }
            }
        }
    }

    post {
    always {
        sh 'docker compose down || true'
    }
    success {
        echo '✅ CI/CD başarıyla tamamlandı'
    }
    failure {
        echo '❌ Pipeline başarısız'
    }
}
}
