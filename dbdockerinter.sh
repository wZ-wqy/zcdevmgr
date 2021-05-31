#!/bin/bash
dtdbcnt=`mysqlshow -uroot -proot_pwd|grep dt|wc -l`
if [ $dtdbcnt -eq "0" ] ; then
	mysql -uroot -p$MYSQL_ROOT_PASSWORD <<EOF
	CREATE DATABASE IF NOT EXISTS dt default charset utf8 COLLATE utf8_general_ci;
	set names utf8;
	use dt;
	source /tmp/dt.sql
EOF
else
	echo "db is exits"
fi
exit 0
