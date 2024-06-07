echo SELECT 'CREATE DATABASE paymentgateway_product_basic' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_basic') \gexec | psql "postgresql://postgres:postgres@localhost"
for %%G in (sql/*.sql) do psql -a -f sql/%%G "postgresql://postgres:postgres@localhost/paymentgateway_product_basic"

java -cp paymentgateway.product.basic --module-path paymentgateway.product.basic -m paymentgateway.product.basic