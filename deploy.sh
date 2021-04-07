#!/bin/bash
echo '######## build clean and bootJar ....'
gradle clean
gradle bootJar

echo '######## assets, templates zip ....'
gradle assetsZip
gradle templatesZip

echo '######## dev server daemon stop ....'
ssh ubuntu@54.180.99.109 'sudo service devflix stop'

echo '######## delete jar file in server ....'
ssh ubuntu@54.180.99.109 'rm /srv/devflix/devflix-1.0-SNAPSHOT.jar'

echo '######## delete assets folder and templates folder in files ....'
ssh ubuntu@54.180.99.109 'rm -rf /srv/devflix/assets'
ssh ubuntu@54.180.99.109 'rm -rf /srv/devflix/templates'

echo '######## send jar and zip to server ....'
scp build/libs/devflix-1.0-SNAPSHOT.jar ubuntu@54.180.99.109:/srv/devflix
scp build/assets.zip ubuntu@54.180.99.109:/srv/devflix
scp build/templates.zip ubuntu@54.180.99.109:/srv/devflix

echo '######## unzip assets.zip and templates.zip and delete zip  ....'
ssh ubuntu@54.180.99.109 'unzip /srv/devflix/assets.zip -d /srv/devflix/assets'
ssh ubuntu@54.180.99.109 'unzip /srv/devflix/templates.zip -d /srv/devflix/templates'
ssh ubuntu@54.180.99.109 'rm /srv/devflix/assets.zip'
ssh ubuntu@54.180.99.109 'rm /srv/devflix/templates.zip'

echo '######## restart server ....'
ssh ubuntu@54.180.99.109 'sudo service devflix start'