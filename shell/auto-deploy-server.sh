#!/bin/bash
#
# MIT License
# Copyright 2024-present cht
#
# Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
#

###############################
#	执行步骤：1.备份 backJar
#			  2.停止 stop
#			  3.替换 deploy
#			  4.启动 start
#			  5.检查 check
###############################

#0.配置文件名称
config_properties=cht.ini

#1.项目名称
project_name=`cat ${config_properties} |grep -w @PROJECT_NAME|awk -F '=' '{printf $2}'`

#2.备份的源地址
app_code_source=`cat ${config_properties} |grep -w @APP_CODE_SOURCE|awk -F '=' '{printf $2}'`

#3.取备份目的目录（如/home/APP_BAK/cht）
app_code_bak=`cat ${config_properties} |grep -w @APP_CODE_BAK|awk -F '=' '{printf $2}'`

#4.取备份命令（如tar -zcvf）
app_bak_command=`cat ${config_properties} |grep -w @APP_BAK_COMMAND|awk -F '=' '{printf $2}'`

#5.取文件备份后的后缀名（如.tar.gz）
app_bak_suffix=`cat ${config_properties} |grep -w @APP_BAK_SUFFIX|awk -F '=' '{printf $2}'`

#6.发版包所在目录
send_code_source=`cat ${config_properties} |grep -w @APP_ADD_CODE|awk -F '=' '{printf $2}'`

#7.程服务名称，用户检查服务状态
project_simple=`cat ${config_properties} |grep -w @PROJECT_SIMPLE|awk -F '=' '{printf $2}'`

#8.健康检查地址
url=`cat ${config_properties} |grep -w @CHECK_URL|awk -F '=' '{printf $2}'`

#9.健康检查地址
wait_time=`cat ${config_properties} |grep -w @WAIT_TIME|awk -F '=' '{printf $2}'`

active=`cat ${config_properties} |grep -w @ACTIVE|awk -F '=' '{printf $2}'`

#全量备份jar
backJar(){
	sleep 1s
	echo ""
	echo "#do backup operation..."
	#判断备份目录是否存在
	if [ ! -d "$app_code_bak" ];then
		echo "#"$app_code_bak"directory is not exist, building..."
		mkdir "$app_code_bak"
		echo "#build success"
	fi
	sleep 2s
	cd ${app_code_source}
	echo "cd "`pwd`
	echo ${app_bak_command} ${app_code_bak}'/'$(date "+%Y%m%d%H%M%S")'_'${project_name}'_APPge'${app_bak_suffix} ${project_name}
	${app_bak_command} ${app_code_bak}'/'$(date "+%Y%m%d%H%M%S")'_'${project_name}'_APPge'${app_bak_suffix} ${project_name} >/dev/null

	if [ $? -eq 0 ];then
		sleep 2s
		echo "#backup---success"
	else
		sleep 2s
		echo "#backup---fail"
		exit 1
	fi
}

#停止服务
stop(){
	sleep 1s
	echo ""
	echo "#Stop service begin"

	echo "#check if the service "${project_simple}" exists"
	sleep 2s
	if ps -ef|grep $project_simple | egrep -v grep >/dev/null
	then
		echo "# "${project_simple}" service is exists,do stop opreate."
		if kill -9 `cat cht.pid`
		then
			sleep 1s
			echo "#stop---success"
		else
			echo "#stop---fail!"
			exit 1
		fi
	else
		echo "#"${project_simple}" service is not exist"
	fi
}

#发布时替换jar
replaceJar(){
	sleep 1s
	echo ""
	echo "#do deploy operation"
	sleep 1s

	echo "#deploy begin"
	temp1=${send_code_source}'/'${project_name}
	echo cp -r $temp1 $app_code_source
	if `cp -r $temp1 $app_code_source`
	then
		echo "#deploy---success"
	else
		echo "#deploy---fail"
		exit 1
	fi
}


#启动服务
start(){
	echo ""
	echo "#Start service begin"
	if `nohup java -jar -Dspring.profiles.active=$active $app_code_source'/'$project_name >/dev/null 2>&1 & echo $! > cht.pid`
	then
		echo "#Starting..."
		sleep 5s

		#判断服务是否启动成功
		if ps -ef|grep $project_simple | egrep -v grep >/dev/null
		then
			echo "#Start---success"
		else
			echo "#Start---fail"
			exit 1
		fi	
	else
		echo "#Start---fail"
		exit 1
	fi
}

#健康检查
successflag=0
check_http(){
	for((i=1;i<=3 && $successflag != 1;i++))
	do
        sleep $wait_time
	status_code=$(curl -m 5 -s -o /dev/null -w %{http_code} $url)
	
	if [ "200" = "${status_code}" ];then
		successflag=1;
		sleep 2s
		echo "#healthcheck---success":"$i"
	else
		sleep 2s
		echo "#healthcheck---fail":"$i"
		if [ i == "3" ];then
		exit 1
		fi
	fi
	done
}

#######################################################处理主逻辑#######################################################
params=$1

if [ ! -n "$params" ];then
	echo ""
	echo "#missing params（example: sh XXX.sh backup|stop|deploy|start|check）"
	echo ""
	exit 1
else
	if [ "backup" = "${params}" ]
	then
		#备份
		backJar
	elif [ "stop" = "${params}" ]
	then
		#停止
		stop
	elif [ "deploy" = "${params}" ]
	then
		#替换
		replaceJar
	elif [ "start" = "${params}" ]
	then
		#启动
		start
	elif [ "check" = "${params}" ]
	then
		#检查
		check_http	
	else
		echo "#parameter not supported（now support backup|stop|deploy|start|check）"
		exit 1
	fi
	
fi