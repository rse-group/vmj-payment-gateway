echo SELECT 'CREATE DATABASE paymentgateway_product_crinkle' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_crinkle') \gexec | psql "postgresql://postgres:crown123@localhost"
java -cp paymentgateway.product.crinkle --module-path paymentgateway.product.crinkle -m paymentgateway.product.crinkle