# my-email-service

### Prerequisites

[Gradle](https://gradle.org/) is the build tool of choice, and only requires a Java JDK or JRE version 7 
or higher to be installed. 

This project makes use of [The Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html). The
following command will run a full build of the project.

    ./gradlew clean build


### Running the application

Simply use gradle Wrapper to run this SpringBoot application. 
    
    ./gradlew bootrun -Pargs="--server.port=5000"
    
### The request params and body
    1.Url:
        POST http://localhost:5000/email -- local
        POST https://Emailsender-env.vk3tmmppum.ap-southeast-2.elasticbeanstalk.com:5000/email
     
    2.Header: 
        no need in this design phase
        
    3. Body:
    {
        "from" : "mingjian.sun.career@gmail.com",
        "subject"  : "yo@a.com",
        "content": "hehe@a.com",
        "to" :[ "mingjian.sun.career@gmail.com","dafa@gmail.com"],
        "cc" : ["mingjian.su@gmail.com"],
        "bcc" : ["lala@gmail.com", "etds@gmail.com"]
    }
    
    recipients, ccRecipients, bccRecipients are arrays.
    
### Handle Validation and Exception
    
    1. If Emai is invalid or empty, then the system would reture like this with 422
    
        {
            "reason": "Some email address are Invalid, please check",
            "reasonCode": "invalid_email_address"
        },
        {
            "reason": "'to' parameter is not a valid address",
            "reasonCode": "invalid_email_address"
        }
    
    2. If some mandatory fileds are missing like subject, content, or to, then the system would reture like this with 422

        {
            "reason": "subject field is mandatory",
            "reasonCode": "missing_information"
        },
        {
            "reason": "content field is mandatory",
            "reasonCode": "missing_information"
        },
        {
            "reason": "To field is mandatory, and there should be At least one recipient",
            "reasonCode": "missing_information"
        }
        
    3. If some duplicate emails are found between cc, bcc to, then the system would reture like this with 422
        
        {
            "reason": "Each email address between to, cc, and bcc field should be unique ",
            "reasonCode": "email_address_duplicate"
        }
        
    4. For MailGun, it is a little bit tricky to use, because it only accepts the verified real email address,
    so if you want try to send to `abc@gmail.com` which is not verified, then api would return like 
        
        {
            "reason": "Some email addresses are not verified in MailGun, for testing purpose, please choose one of [pythonsun1234@gmail.com|ianmingsun1234@gmail.com]",
            "reasonCode": "invalid_email_address"
        }
    We will fix that in the next release.
    
    5. If one of the service provider is down, then we could switch to another without affections

### Problems and solutions

    1. The way to integrate two API into one, different library, different classes and methods.
    2. How to indicate one of the server is down.
    3. Valildation and error handling
    4. Integration Test
    5. DB is needed or not?
    
    Solutions:
    1. Customize a common emailDto, response payload and error message dto shared between apis and to facilitate info carrier.
    2. Based on response http code(Maybe that is buggy, probably we can call their {emailProvider}/health endpoint?)
    3. Input validation before business logic(data duplication or missiong), catch exceptioin thrown by them and throw a new customized one
    4. Use SpringBootTest, and rest call. 
    5. Well, if we need to store some attchment, well, it is needed. In this phase, we don't support that for time limit.

### Deployment

    Deploy service to AWS ElasticBeanstalk(url:"Emailsender-env.vk3tmmppum.ap-southeast-2.elasticbeanstalk.com")

### For e2e test
    For end to end test, there is a new endpoint enabling to name a provider to send email alone
    POST http://localhost:5000/email/mailgun
    POST http://localhost:5000/email/sendgrid
    
    This is only for testing single service provider
    
### Next release
    1.Support Attachment
    2.API user authentication(oauth2) and authoriztion
    3.Deployment Pipeline by Jenkins
    4.Dockerize serive and orchestrated by K8S