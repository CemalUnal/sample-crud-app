#!/bin/bash

for i in {1..10}; do
    curl 'http://localhost:8090/api/simple-backend' | jq
done
