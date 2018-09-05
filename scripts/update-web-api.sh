vms=2
for i in `seq 1 $vms`
do
    vmName=web-api-$i
    gcloud compute scp ../build/libs/web_api.jar $vmName:~
    gcloud compute ssh $vmName --command "sudo mv web_api.jar /var/spring"
    gcloud compute ssh $vmName --command "sudo systemctl restart spring"
done
