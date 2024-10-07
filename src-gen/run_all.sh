#!/bin/bash
source ~/.zshrc  

cleanup() {
    echo "Exiting script..."
    pkill -P $$
    exit 1
}

trap cleanup SIGINT

echo "Enter the path to the frontend directory: "
read -p "Enter the path to the frontend directory: " frontend_dir

echo "SELECT 'CREATE DATABASE paymentgateway_product_' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_') \gexec" | psql "postgresql://postgres:@localhost"
for file in sql/*.sql; do
    psql -a -f "$file" "postgresql://postgres:@localhost/paymentgateway_product_"
done

java -cp paymentgateway.product. --module-path paymentgateway.product. -m paymentgateway.product. &

cd $frontend_dir && {
    npm install && {
        npm run json:server &
        npm run start &
    }
}

wait