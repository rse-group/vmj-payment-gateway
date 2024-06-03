#!/bin/bash

cleanup() {
    echo "Exiting script..."
    pkill -P $$
    exit 1
}

trap cleanup SIGINT

read -p "Enter the path to the frontend directory: " frontend_dir

echo "SELECT 'CREATE DATABASE paymentgateway_product_whoosh' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_whoosh') \gexec" | psql "postgresql://postgres:@localhost"
for file in sql/*.sql; do
    psql -a -f "$file" "postgresql://postgres:@localhost/paymentgateway_product_whoosh"
done

java -cp paymentgateway.product.whoosh --module-path paymentgateway.product.whoosh -m paymentgateway.product.whoosh &

cd $frontend_dir && {
    npm install && {
        npm run json:server &
        npm run start &
    }
}

wait