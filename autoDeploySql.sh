#!/bin/sh
#自动发布脚本
#crontab
#       30 20 * * * sh /tmp/AutoDeploySql.sh user pwd url
. /Users/lank/.bash_profile
logfile=/tmp/AutoDeploySql.log
user=$1
pwd=$2
url=$3
nt=`date`
echo "Action Start $nt">>$logfile
cd /tmp/
mysqldump -uroot -proot_pwd dt --ignore-table=dt.sys_info>dt.sql
tar zcvf dt.sql.tar.gz ./dt.sql
curl -T  dt.sql.tar.gz -u $user:$pwd "$url/dt.sql.tar.gz?version=latest"
exit 0




