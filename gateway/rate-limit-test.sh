#!/bin/bash

for i in {1..10}; do
    curl 'http://localhost:9091/api/demo-backend/customers' | jq
done
