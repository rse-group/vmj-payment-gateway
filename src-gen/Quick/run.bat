echo SELECT 'CREATE DATABASE paymentgateway_product_quick' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_quick') \gexec | psql "postgresql://postgres:postgres@localhost"
java -cp paymentgateway.product.quick --module-path paymentgateway.product.quick -m paymentgateway.product.quick