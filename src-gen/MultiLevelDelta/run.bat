echo SELECT 'CREATE DATABASE paymentgateway_product_multileveldelta2' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_multileveldelta2') \gexec | psql "postgresql://postgres:admin@localhost"
for %%G in (sql/*.sql) do psql -a -f sql/%%G "postgresql://postgres:admin@localhost/paymentgateway_product_multileveldelta2"

java -cp paymentgateway.product.multileveldelta --module-path paymentgateway.product.multileveldelta -m paymentgateway.product.multileveldelta