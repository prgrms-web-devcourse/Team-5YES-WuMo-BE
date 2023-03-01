#!/usr/bin/env bash
REPOSITORY=/home/ec2-user/app
cd $REPOSITORY
APP_NAME=wumo
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

cp /home/ec2-user/env/spring.env $REPOSITORY/spring.env

CURRENT_PID=$(pgrep -f $APP_NAME)

echo "> Kill Previous Process" >> /home/ec2-user/app/deploy.log
if [ -z "$CURRENT_PID" ]
then
 echo "> No Process to Kill" >> /home/ec2-user/app/deploy.log
else
 echo "> Kill $CURRENT_PID" >> /home/ec2-user/app/deploy.log
 kill -15 "$CURRENT_PID" >> /home/ec2-user/app/deploy.log 2>&1
 sleep 5
fi
echo "> $JAR_PATH Deploy"
nohup java -jar $JAR_PATH >> /home/ec2-user/nohup.log 2>&1 &

sudo mkdir -p /etc/nginx/sites-available
sudo cp $REPOSITORY/nginx/wumo.conf /etc/nginx/sites-available/wumo.conf

sudo mkdir -p /etc/nginx/sites-enabled
sudo ln -s /etc/nginx/sites-available/wumo.conf /etc/nginx/sites-enabled

sudo service nginx restart