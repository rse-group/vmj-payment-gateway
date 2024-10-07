echo SELECT 'CREATE DATABASE paymentgateway_product_rustle' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_rustle') \gexec | psql "postgresql://postgres:@localhost"
for %%G in (sql/*.sql) do psql -a -f sql/%%G "postgresql://postgres:@localhost/paymentgateway_product_rustle"

java -cp paymentgateway.product.rustle --module-path paymentgateway.product.rustle -m paymentgateway.product.rustle