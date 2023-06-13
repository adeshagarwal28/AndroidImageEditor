pipeline {
    agent any
    
    parameters {
        choice(name: 'FLAVOUR', choices: ['Staging', 'Production'], description: 'Select the build flavor')
    }
    
    stages {
        stage('Build APKs') {
            steps {
                script {
                    // Determine the build variant based on the selected flavor
                    def buildVariant = (params.FLAVOUR == 'Production') ? 'release' : 'debug'
                    
                    // Build the APKs
                    sh "./gradlew assemble${buildVariant} -Pinclude64BitApk"
                    sh "./gradlew assemble${buildVariant} -Pinclude32BitApk"
                    sh "./gradlew assemble${buildVariant}"
                }
            }
        }
        
        stage('Upload APKs') {
            when {
                expression { params.FLAVOUR == 'Production' }
            }
            steps {
                script {
                    // Upload the APKs to a storage location (e.g., AWS S3, Google Cloud Storage)
                    // Replace the placeholders with the actual upload commands for your chosen storage service
                    // sh 'aws s3 cp app/build/outputs/apk/release/app-armeabi-v7a-release.apk s3://your-bucket-name/'
                    // sh 'aws s3 cp app/build/outputs/apk/release/app-arm64-v8a-release.apk s3://your-bucket-name/'
                    // sh 'aws s3 cp app/build/outputs/apk/release/app-universal-release.apk s3://your-bucket-name/'
                    
                    // Generate public download URLs for the uploaded APKs
                    // def apkUrl32Bit = "https://your-bucket-name/app-armeabi-v7a-release.apk"
                    // def apkUrl64Bit = "https://your-bucket-name/app-arm64-v8a-release.apk"
                    // def apkUrlUniversal = "https://your-bucket-name/app-universal-release.apk"
                    
                    // Share the APK URLs via Slack or email
                    // Replace the placeholders with your Slack or email notification logic
                    if (env.CHANNEL_NAME) {
                        slackSend(channel: env.CHANNEL_NAME, message: "Production APKs are ready for download:\n\n32-bit: ${apkUrl32Bit}\n64-bit: ${apkUrl64Bit}\nUniversal: ${apkUrlUniversal}")
                    } else {
                        emailext(
                            to: 'adesh.agarwal@rebelfoods.com',
                            subject: 'Production APKs are ready for download',
                            body: "32-bit: ${apkUrl32Bit}\n64-bit: ${apkUrl64Bit}\nUniversal: ${apkUrlUniversal}",
                            attachLog: true
                        )
                    }
                }
            }
        }
        
        stage('Email APKs') {
            when {
                expression { params.FLAVOUR == 'Staging' }
            }
            steps {
                // Email the APKs to the select group
                // Replace the placeholders with your email logic
                emailext(
                    to: 'adesh.agarwal@rebelfoods.com',
                    subject: 'Staging APKs',
                    body: 'Please find the attached Staging APKs.',
                    attachmentsPattern: 'app/build/outputs/apk/**/*-release.apk'
                )
            }
        }
    }
}
