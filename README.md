# java-spring-s3-reference

Demonstrate uploading a file to an S3 storage bucket using the AWS SDK for Java.

### Use the AWS CLI with IAM Identity Center User

1. Ensure you have configured your AWS CLI to use SSO:
   
   ```shell
   aws configure sso
   ```

2. Authenticate using AWS SSO:

   ```shell
    aws sso login --profile <profile-name>
   ```
   
3. Verify access:

   Try uploading a file to your S3 bucket:

   ```bash
   aws s3 cp <file> s3://<bucket-name> --profile <profile-name>
   ```
