# product=$1
# feature=$2

# echo -e "building the module: $feature"
# javac -d cclasses --module-path $product $(find src/$feature -name "*.java") src/$feature/module-info.java
# jar --create --file $product/$feature.jar -C cclasses .
# rm -r cclasses

function external_module()
{
    cp -r winvmj-libraries/* $modulename/
    # echo "$(pwd)"
    echo "add external module"
}

function build_module()
{
    echo "build module $1"
    javac -d classes --module-path $1 $(find modules/$1 -name "*.java") modules/$1/module-info.java
    jar --create --file $1/$1.jar -C classes .
    rm -r classes
    echo "module $1 ready"
}

function build_module_requirement(){
  module=$1
  targetpath="src/$module/module-info.java"
  req=$(cat $targetpath | grep "requires \( transitive | static \)\?"| awk '{print $2}' | cut -d';' -f 1 )


  external_module
  for reqprod in $req; do
  echo -e "building requirement for $module: $reqprod"
  javac -d classes --module-path midtrans $(find modules/$reqprod -name "*.java") modules/$1/module-info.java
  # echo "this is $1"
  # echo "product name $product"
  jar --create --file $product/$1.jar -C classes .
  rm -r classes
  done
  echo "requirement building done"
  build_module $module
}

modulename=$1
if [ -d "$1" ]; then rm -r $1; fi
if [ -d "classes" ]; then rm -r classes; fi
mkdir $1
echo " -- checking requirement -- "
build_module_requirement $modulename
echo "Build time: $SECONDS seconds"