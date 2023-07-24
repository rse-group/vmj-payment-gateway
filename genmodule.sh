product=$1
feature=$2

echo -e "building the module: $feature"
javac -d cclasses --module-path $product $(find src/$feature -name "*.java") src/$feature/module-info.java
jar --create --file $product/$feature.jar -C cclasses .
rm -r cclasses