set -x
set -e

currdir=$(cd $(dirname $0); pwd);
cp ${currdir}/../../mes-application/target/mes-application-1.0.0-SNAPSHOT.jar ${currdir}/app.jar
cp ${currdir}/../../mes-application/target/mes-application-1.0.0-SNAPSHOT.jar ${currdir}/mes-application-1.0.0-SNAPSHOT.jar