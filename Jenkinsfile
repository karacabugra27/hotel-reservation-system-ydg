pipeline {
  agent any

  stages {
    stage('1- Checkout Code') {
      steps {
        checkout scm
      }
    }

    stage('2- Build Project') {
      steps {
        sh 'mvn -U clean package -DskipTests'
      }
    }

    stage('3- Unit Tests') {
      steps {
        sh 'mvn -U test -Punit'
      }
    }

    stage('4- Integration Tests') {
      steps {
        sh 'mvn -U test -Pintegration'
      }
    }

    stage('5- Run System (Docker)') {
      steps {
        sh 'docker-compose up -d --build'
        sh 'sleep 10'
      }
    }

    stage('6- Selenium Test 1 - Available Rooms') {
      steps {
        sh 'mvn -U test -Pselenium -Dtest=AvailableRoomsSeleniumTest'
      }
    }

    stage('7- Selenium Test 2 - Reservation Flow') {
      steps {
        sh 'mvn -U test -Pselenium -Dtest=ReservationFlowSeleniumTest'
      }
    }

    stage('8- Selenium Test 3 - Payment Flow') {
      steps {
        sh 'mvn -U test -Pselenium -Dtest=PaymentFlowSeleniumTest'
      }
    }
  }

  post {
    always {
      sh 'docker-compose down || true'
    }
  }
}