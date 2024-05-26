# java-spring-s3-reference

Demonstrate uploading a file to an S3 storage bucket using the AWS SDK for Java.

## Prerequisites

- [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
- [AWS SDK for Java](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html)

## Setup

1. Create an S3 bucket:

   ```shell
   aws s3api create-bucket --bucket <bucket-name> --region <region>
   ```
   
2. Create an IAM Identity Center user:

   ```shell
    aws iam create-user --user-name <user-name> --profile <profile-name>
    ```
   
3. Attach the `AmazonS3FullAccess` policy to the user:

   ```shell
   aws iam attach-user-policy --policy-arn arn:aws:iam::aws:policy/AmazonS3FullAccess --user-name <user-name> --profile <profile-name>
   ```
   
### Create a Permission Set in IAM Identity Center using the AWS Management Console

1. Sign in to the AWS Management Console.
2. Open the [IAM Identity Center](https://console.aws.amazon.com/singlesignon/home?region=us-east-1#/users).
   - In the IAM Identity Center dashboard, select "Permission sets" from the left navigation pane.
   - Choose "Create a permission set".
3. Create a Custom Permission Set.
   - Choose "Create a custom permission set."
   - Give your permission set a name and description.
   - Click "Next: Permissions".
4. Attach S3 Policies.
   - Click "Create inline policy."
   - Use the following JSON policy to grant S3 access (`src/main/resources/s3-bucket-access-policy.json`).
   - Click "Create Policy" and then "Next: Tags" (optional), then "Next: Review."
   - Review the permission set and click "Create."
5. Assign the Permission Set to a User.
   - Go back to the IAM Identity Center dashboard.
   - Navigate to "AWS accounts."
   - Select the account you want to assign the permission set to.
   - Click "Assign users."
   - Choose the user you want to assign the permission set to.
   - Select the permission set you created (`S4AccessPermissionSet`).
   - Click "Assign."

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
