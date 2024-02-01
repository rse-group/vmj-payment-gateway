product=$1
mainclass=$2

echo -e "building the product: $mainclass"
javac -d cclasses --module-path $product $(find src/$product -name "*.java") src/$product/module-info.java
jar --create --file $product/$mainclass.jar --main-class $product.$mainclass -C cclasses .
rm -r cclasses
echo "Product $mainclass is ready"