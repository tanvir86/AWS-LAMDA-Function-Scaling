# Project Structure, Deployment and Testing

![Architecture](images/sample-java-basic.png)

The project source includes function code and supporting resources:
- `src/main` - A Java Function in `handler`.
- `src/test` - A unit test and helper classes.
- `notebokes` - Regression Model from given data source.
- `samples` - Given data sample and request format.
- `template.yml` - An AWS CloudFormation template that creates an application.
- `build.gradle` - A Gradle build file.
- `pom.xml` - A Maven build file.
- `1-create-bucket.sh`, `2-deploy.sh`, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.
- `ZN coding challege.pdf` - Problem specification.

Use the following instructions to deploy the sample application.

# Requirements
- [Java 8 runtime environment (SE JRE)](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Gradle 5](https://gradle.org/releases/) or [Maven 3](https://maven.apache.org/docs/history.html)
- The Bash shell. For Linux and macOS, this is included by default. In Windows 10, you can install the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows-integrated version of Ubuntu and Bash.
- [The AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) v1.17 or newer.

If you use the AWS CLI v2, add the following to your [configuration file](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-files.html) (`~/.aws/config`):

```
cli_binary_format=raw-in-base64-out
```

This setting enables the AWS CLI v2 to load JSON events from a file, matching the v1 behavior.

# Setup
Download or clone this repository.

    $ git clone https://github.com/tanvir86/zeronorth-problem-solving.git
    $ cd zeronorth-problem-solving

To create a new bucket for deployment artifacts, run `1-create-bucket.sh`.

    zeronorth-problem-solving$ ./1-create-bucket.sh
    make_bucket: lambda-artifacts-a5e4xmplb5b22e0d

# Deploy
To deploy the application, run `2-deploy.sh`.

    zeronorth-problem-solving$ ./2-deploy.sh
    BUILD SUCCESSFUL in 1s
    Successfully packaged artifacts and wrote output template to file out.yml.
    Waiting for changeset to be created..
    Successfully created/updated stack - zeronorth-problem-solving

This script uses AWS CloudFormation to deploy the Lambda functions and an IAM role. If the AWS CloudFormation stack that contains the resources already exists, the script updates it with any changes to the template or function code.

You can also build the application with Maven. To use maven, add `mvn` to the command.

    zeronorth-problem-solving$ ./2-deploy.sh mvn
    [INFO] Scanning for projects...
    [INFO] -----------------------< com.zeronorth:zeronorth-problem-solving >-----------------------
    [INFO] Building zeronorth-problem-solving 1.0-SNAPSHOT
    [INFO] --------------------------------[ jar ]---------------------------------
    ...

# Test
To invoke the function, run `3-invoke.sh`.

    zeronorth-problem-solving$ ./3-invoke.sh
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }
    [{"fuelConsumptionInMetricTon":422.64571056674174,"co2EmissionInMetricTon":1316.1187773685406,"eco":true},{"fuelConsumptionInMetricTon":441.351698984272,"co2EmissionInMetricTon":1374.36922683492,"eco":false},{"fuelConsumptionInMetricTon":445.80025455298573,"co2EmissionInMetricTon":1388.2220292407474,"eco":false}]

Let the script invoke the function a few times and then press `CRTL+C` to exit.

The application uses AWS X-Ray to trace requests. Open the [X-Ray console](https://console.aws.amazon.com/xray/home#/service-map) to view the service map.

![Service Map](images/service-map.png)

Choose a node in the main function graph. Then choose **View traces** to see a list of traces. Choose any trace to view a timeline that breaks down the work done by the function.

![Trace](images/trace.png)


# Cleanup
To delete the application, run `4-cleanup.sh`.

    java-basic$ ./4-cleanup.sh
