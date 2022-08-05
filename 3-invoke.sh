#!/bin/bash
set -eo pipefail
FUNCTION=$(aws cloudformation describe-stack-resource --stack-name zeronorth-problem-solving --logical-resource-id function --query 'StackResourceDetail.PhysicalResourceId' --output text)

while true; do
  aws lambda invoke --function-name $FUNCTION --payload file://samples/request1.json out.json
  cat out.json
  echo ""
  sleep 2
done
