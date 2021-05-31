#!/bin/sh
# Build Docker Image
#docker.io/tomcat          9.0.36-jdk8-openjdk
#docker.io/mysql           5.7
#docker exec -it dt-app bash
#
#
curversion=2.2.24
appdockerfile="AppDockerfile"
dbdockerfile="DbDockerfile"
if [ -f "dt.sql" ];then
  echo "delete"
  #rm -rf dt.sql
fi

if [ -f "dt.war" ];then
  echo "delete"
  #rm -rf dt.war
fi

if [ -f "WEB-INF/classes/config.properties" ];then
  echo "delete"
  rm -rf WEB-INF/classes/config.properties
fi

jar xvf dt.war WEB-INF/classes/config.properties
sed -i "s/127.0.0.1/db/g" WEB-INF/classes/config.properties
jar uvf dt.war WEB-INF/classes/config.properties

########################生成数据库dockerfile####################
echo "">$dbdockerfile
echo "FROM docker.io/algernonking/dtmysql:base ">>$dbdockerfile
echo "MAINTAINER lank                            ">>$dbdockerfile
echo "RUN rm -rf /tmp/dt.sql                     ">>$appdockerfile
echo "COPY dt.sql /tmp/                          ">>$dbdockerfile
echo "EXPOSE 3306                                ">>$dbdockerfile


########################生成应用dockerfile####################
echo "">$appdockerfile
echo "FROM docker.io/algernonking/dtapp:base     ">>$appdockerfile
echo "MAINTAINER lank                                 ">>$appdockerfile
echo "RUN rm -rf /usr/local/tomcat/webapps/dt    \    ">>$appdockerfile
echo "        && rm -rf /usr/local/tomcat/logs/* \    ">>$appdockerfile
echo "        && rm -rf /usr/local/tomcat/temp/*      ">>$appdockerfile
echo "COPY dt.war /usr/local/tomcat/webapps/          ">>$appdockerfile
echo "EXPOSE 8080                                     ">>$appdockerfile

docker build -f DbDockerfile  -t docker.io/algernonking/dtmysql:$curversion .
docker build -f AppDockerfile -t docker.io/algernonking/dtapp:$curversion .
#docker push docker.io/algernonking/dtmysql:$curversion
#docker push docker.io/algernonking/dtapp:$curversion

exit 0

########################################################
#sed -i "s/127.0.0.1/$DB_PORT_3306_TCP_ADDR/g"   /usr/local/tomcat/webapps/dt/WEB-INF/classes/config.properties
#sed -i "s/$DB_PORT_3306_TCP_ADDR/127.0.0.1/g"   /usr/local/tomcat/webapps/dt/WEB-INF/classes/config.properties
docker push docker.io/algernonking/dtmysql:base
docker push docker.io/algernonking/dtapp:base
docker push docker.io/algernonking/dtmysql:2.2.24
docker push docker.io/algernonking/dtapp:2.2.24

docker run --name dt-db -t \
-e MYSQL_USER="dt" \
-e MYSQL_PASSWORD="dt_pwd" \
-e MYSQL_ROOT_PASSWORD=root_pwd \
-v /data/mysql:/var/lib/mysql  \
-p 3306:3306 \
-d docker.io/algernonking/dtmysql:2.2.24 \
--character-set-server=utf8 \
--lower_case_table_names=1


docker run --name dt-app -t \
-v /data/upload:/usr/local/tomcat/webapps/upload \
--link=dt-db:db \
-p 8080:8080  \
-d docker.io/algernonking/dtapp:2.2.24


