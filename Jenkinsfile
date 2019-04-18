pipeline {
  agent any
  stages {
    stage('Checkout') {
      steps {
        git(url: 'https://github.com/Mshardul/Aikyam.git', changelog: true, poll: true)
      }
    }
    
    stage('Build') {
      steps {
        build 'Build'
      }
    }
  }
}
