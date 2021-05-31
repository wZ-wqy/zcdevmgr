#!/bin/sh
d=`date "+%Y_%m_%d_%H_%M_%S"`
deploydir="deploy${d}"
mkdir $deploydir
cd $deploydir
wget https://gitee.com/lank/zcdevmgr/raw/master/deploy.sh
file=`ls -rtl |tail -1|awk '{print $NF}'`
chmod +x $file
sh $file
exit 0
