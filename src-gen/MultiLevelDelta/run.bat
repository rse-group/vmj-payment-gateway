echo SELECT 'CREATE DATABASE paymentgateway_product_multileveldelta' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'paymentgateway_product_multileveldelta') \gexec | psql "postgresql://postgres:postgres@localhost"
for %%G in (sql/*.sql) do psql -a -f sql/%%G "postgresql://postgres:postgres@localhost/paymentgateway_product_multileveldelta"

java -cp paymentgateway.product.multileveldelta --module-path paymentgateway.product.multileveldelta -m paymentgateway.product.multileveldelta