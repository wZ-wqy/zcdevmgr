#!/bin/sh
##############################################################
#推荐RedHat 7.X 版本或同版本的发行版
#Java:1.8
#Mysql:5.7
#Tomcat:9
#Tool Need:unzip zip
#					 Modify By Lank
#                    2020-02-27
##############################################################
####################################################
#Deploy Mysql57 Function
####################################################
scriptsdir=`pwd`
deployMysql57(){
	echo "start to deploy mysql"
	mysqldir="/usr/local"
	if [[ -n $1 ]];then
		mysqldir=$1
	fi
	soft="soft.tar.gz"
	if [[ -n $2 ]];then
		soft=$2
	fi
	mysqlpwd="Root@aKb@Root"
	if [[ -n $3 ]];then
		mysqlpwd=$3
	fi
	if [[ ! -f $soft ]];then
		echo "ERROR|Deply Soft Match Failed"
		return 0
	fi
	glibcnt=`strings /lib64/libc.so.6|grep GLIBC|grep 2.12|wc -l`
	if [[ $glibcnt -eq 0 ]];then
		echo "ERROR|GLIBC 2.12 Match Failed"
		return 0
	fi
	ifx86=`cat /proc/version|grep x86_64|wc -l`
	if [[ $ifx86 -eq 0 ]];then
		echo "ERROR|Linux x86_64 Match Failed"
		return 0
	fi
	if [[ -d "${mysqldir}/mysql/data" ]];then
		echo "ERROR|${mysqldir}/mysql/data Deploy Failed,Please remove it first!"
		return 0
	fi
	groupadd mysql
	useradd -r -g mysql mysql
	mkdir -p ${mysqldir}/mysql/data
	chown -R mysql:mysql /usr/local/mysql
	if [[ -f "/etc/my.cnf" ]];then
		d=`date "+%Y_%m_%d_%H_%M_%S"`
		mv /etc/my.cnf /etc/my.cnf.$d
	fi
	mycnftmp="/tmp/my.cnf.tmp"
	smysqldir="/usr/local"
	cat > $mycnftmp <<EOF
[mysqld]
lower_case_table_names=1
basedir=/usr/local/mysql
datadir=/usr/local/mysql/data
pid-file=/usr/local/mysql/mysqld.pid
socket=/usr/local/mysql/mysql.sock
log-error=/usr/local/mysql/mysqld.log
lc-messages=en_US
lc-messages-dir=/usr/local/mysql/share
symbolic-links=0
port=3309
sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION'
[client]
default-character-set=utf8
[mysql]
default-character-set=utf8
[mysqld]
log-bin=mysql-bin
binlog-format=ROW
server_id=1
max_connections=1000
init_connect='set collation_connection = utf8_unicode_ci'
init_connect='set names utf8'
character-set-server=utf8
collation-server=utf8_unicode_ci
skip-character-set-client-handshake
EOF
	cat $mycnftmp |sed "s:${smysqldir}:${mysqldir}:g">/etc/my.cnf
	cp $soft ${mysqldir}/mysql
	cd $mysqldir/mysql
	tar xvf $soft
	onedir=`ls -rtl |tail -1 |awk '{print $NF}'`
	mv $onedir/* .
	chown -R mysql:mysql /usr/local/mysql
	rm -rf /tmp/mysql.sock
	ln -s $mysqldir/mysql/mysql.sock /tmp/mysql.sock
	$mysqldir/mysql/bin/mysqld --initialize-insecure --user=mysql --basedir=$mysqldir/mysql--datadir=$mysqldir/mysql/data
	echo "start mysql command list:"
	echo "nohup $mysqldir/mysql/bin/mysqld_safe &"
	nohup $mysqldir/mysql/bin/mysqld_safe &
	sleep 10
	echo "use mysql;">init.sql
	echo "update mysql.user set authentication_string=password('$mysqlpwd') where user='root' and Host = 'localhost';">>init.sql
	echo "grant all privileges on *.* to 'root'@'%' identified by '$mysqlpwd' WITH GRANT OPTION;">>init.sql
	echo "flush privileges;">>init.sql
	$mysqldir/mysql/bin/mysql -uroot <init.sql
	rm -rf init.sql
}
####################################################
#Deploy Tomcat Function
####################################################
deployTomcat(){
	tomcatdir="/opt/deploy/tomcat"
	if [[ -n $1 ]];then
		tomcatdir=$1
	fi
	soft="soft.tar.gz"
	if [[ -n $2 ]];then
		soft=$2
	fi
	if [[ ! -f $soft ]];then
		echo "ERROR|Deploy Soft Match Failed"
		return 0
	fi
	if [[ -d $tomcatdir ]];then
		echo "ERROR|${tomcatdir} Deploy Failed,Please remove it first!"
		return 0
	fi
	mkdir -p $tomcatdir
	cp $soft ${tomcatdir}/
	cd $tomcatdir
	tar xvf $soft
	onedir=`ls -rtl |tail -1 |awk '{print $NF}'`
	mv $onedir/* .
	echo "start tomcat command list:"
	echo "sh $tomcatdir/bin/startup.sh"
}
####################################################
#Deploy DtApplication Function
####################################################
deployDtApplication(){
	dtwar="dt.war"
	dtsql="dt.sql"
	tomcatdir="/opt/deploy/tomcat"
	mysqldir="/usr/local/mysql"
	mysqlipwd="root"
	if [[ -n $1 ]];then
		dtwar=$1
	fi
	if [[ -n $2 ]];then
		dtsql=$2
	fi
	if [[ -n $3 ]];then
		tomcatdir=$3
		if [[ ! -d $tomcatdir ]];then
			echo "ERROR|dt Application deploy tomcat not exist"
			return 1
		fi
	fi
	if [[ -n $4 ]];then
		mysqldir=$4
		if [[ ! -d $mysqldir ]];then
			echo "ERROR|dt Application deploy mysql not exist"
			return 1
		fi
	fi
	if [[ -n $5 ]];then
		mysqlipwd=$5
	fi
	#deploy dt.war
	rm -rf $tomcatdir/temp/*
	rm -rf $tomcatdir/work/*
	rm -rf $tomcatdir/webapps/${dtwar}
	cp $dtwar $tomcatdir/webapps/
	#deploy dt.sql
	crsfile="/tmp/crs2.sql"
	echo "CREATE DATABASE IF NOT EXISTS dt default charset utf8 COLLATE utf8_general_ci">$crsfile
	$mysqldir/bin/mysql -uroot -p$mysqlipwd -e "source $crsfile"
	$mysqldir/bin/mysql -uroot -p$mysqlipwd dt -e "source $dtsql"
	rm -rf $crsfile
}
###############################################################
###############################################################
#wget https://mirrors.bfsu.edu.cn/apache/tomcat/tomcat-9/v9.0.43/bin/apache-tomcat-9.0.43.tar.gz
#wget https://cdn.mysql.com//Downloads/MySQL-5.7/mysql-5.7.32-linux-glibc2.12-x86_64.tar.gz
#curl --fail -L -u XXXXX "https://applicationtd-generic.pkg.coding.net/assets/assetswar/dt.sql.tar.gz?version=latest" -o dt.sql.tar.gz
#curl --fail -L -u XXXXX "https://applicationtd-generic.pkg.coding.net/assets/assetswar/dt.war?version=latest" -o dt.war
#wget https://ccess/products.oss-cn-hangzhou.aliyuncs.com/dt/dt.sql.tar.gz
#wget https://ccess/products.oss-cn-hangzhou.aliyuncs.com/dt/dt.war
#tar xvf dt.sql.tar.gz
mysqlsoft="mysql-5.7.32-linux-glibc2.12-x86_64.tar.gz"
mysqldir="/usr/local"
mysqlpwd="Root@aKb@Root"
tomcatsoft="apache-tomcat-9.0.43.tar.gz"
tomcatdir="/opt/deploy/tomcat"
dtwar="dt.war"
dtsql="dt.sql"
dtversion="2.2.24"
########################################
#Deploy Mysql
########################################
cd $scriptsdir
if [[ -f $mysqlsoft ]];then
	deployMysql57 $mysqldir $mysqlsoft $mysqlpwd
else
	echo "ERROR|Mysql Soft Not Exists"
fi
########################################
#Deploy Tomcat
########################################
cd $scriptsdir
if [[ -f $tomcatsoft ]];then
    which java
    javares=`echo $?`
    if [[ $javares -eq 0 ]];then
        deployTomcat $tomcatdir $tomcatsoft
    else
        echo "ERROR|java command not exist"
    fi
else
	  echo "ERROR|Tomcat soft not exist"
fi
########################################
#Deploy Dt Application
########################################
cd $scriptsdir
if [[ -f $dtwar && -f $dtsql ]];then
  which unzip
  unzipres=`echo $?`
  which zip
  zipres=`echo $?`
  if [[ $unzipres -eq 0 && $zipres -eq 0 ]];then
      if [[ -f "WEB-INF/classes/config.properties" ]];then
         rm -rf WEB-INF*
      fi
      unzip dt.war WEB-INF/classes/config.properties
      sed -i "s/127.0.0.1/localhost/g" WEB-INF/classes/config.properties
      sed -i "s/3306/3309/g" WEB-INF/classes/config.properties
      sed -i "s/jdbc.password/#jdbc.password/g" WEB-INF/classes/config.properties
      sed -i "s/tool.mysqldump/#tool.mysqldump/g" WEB-INF/classes/config.properties
      echo "jdbc.password=$mysqlpwd">> WEB-INF/classes/config.properties
      echo "tool.mysqldump=$mysqldir/mysql/bin/mysqldump">>WEB-INF/classes/config.properties
      zip -u dt.war WEB-INF/classes/config.properties
      deployDtApplication $dtwar $dtsql $tomcatdir $mysqldir/mysql $mysqlpwd
      sh $tomcatdir/bin/startup.sh
  else
      echo "ERROR|unzip or zip command not exist"
  fi
else
	echo "ERROR|Dt Application war or sql not exist"
fi
exit 0


