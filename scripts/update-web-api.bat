@ECHO OFF

REM vms=2
REM for i in `seq 1 $vms`
REM do
REM     vmName=web-api-$i
REM     gcloud compute scp ../build/libs/web_api.jar $vmName:~
REM     gcloud compute ssh $vmName --command "sudo mv web_api.jar /var/spring"
REM     gcloud compute ssh $vmName --command "sudo systemctl restart spring"
REM done

SETLOCAL ENABLEDELAYEDEXPANSION

set vms=2
for /l %%x in (1, 1, %vms%) do (
   set vmName=web-api-%%x
   echo Updating !vmName!
   
   gcloud compute scp ../build/libs/web_api.jar !vmName!
   gcloud compute ssh !vmName! --command "sudo mv web_api.jar /var/spring"
   gcloud compute ssh !vmName! --command "sudo systemctl restart spring"
)