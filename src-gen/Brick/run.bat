echo SELECT 'CREATE DATABASE paymentgateway_product_brick' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_brick') \gexec | psql "postgresql://postgres:postgres@localhost"
java -cp paymentgateway.product.brick --module-path paymentgateway.product.brick -m paymentgateway.product.brick