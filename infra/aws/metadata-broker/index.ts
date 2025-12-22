import { App, Stack } from 'aws-cdk-lib';
import { Runtime } from 'aws-cdk-lib/aws-lambda';
import { NodejsFunction } from 'aws-cdk-lib/aws-lambda-nodejs';

export class MetadataBroker extends Stack {
  constructor(scope: App, id: string) {
    super(scope, id);
    let lambdaFunction = new NodejsFunction(this, 'metadata-broker-fn', {
      functionName: 'metadata-broker-fn',
      runtime: Runtime.NODEJS_LATEST,
      handler: 'index.handler',
    });
  }
}
