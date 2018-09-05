vms=2
for i in `seq 1 $vms`
do
    vmName=web-api-$i 
    echo "CREATING VMs FOR WEB API"
    gcloud beta compute --project=psg15-1718v-1534010601683 instances create $vmName --zone=europe-west2-c --machine-type=f1-micro --subnet=default --network-tier=PREMIUM --maintenance-policy=MIGRATE --service-account=573161398457-compute@developer.gserviceaccount.com --scopes=https://www.googleapis.com/auth/devstorage.read_only,https://www.googleapis.com/auth/logging.write,https://www.googleapis.com/auth/monitoring.write,https://www.googleapis.com/auth/servicecontrol,https://www.googleapis.com/auth/service.management.readonly,https://www.googleapis.com/auth/trace.append --image=centos-7-v20180523 --image-project=centos-cloud --boot-disk-size=10GB --boot-disk-type=pd-standard

    echo "INSTALLING JAVA 8"
    gcloud compute ssh $vmName --command "sudo yum -y update"
    gcloud compute ssh $vmName --command "sudo yum -y install java-1.8.0-openjdk"

    echo "CONFIGURING AND INSTALLING WEB API"
    gcloud compute ssh $vmName --command "sudo useradd spring"
    gcloud compute ssh $vmName --command "sudo mkdir /var/spring"
    gcloud compute scp ../build/libs/web_api.jar $vmName:~
    gcloud compute ssh $vmName --command "sudo mv web_api.jar /var/spring"
    gcloud compute ssh $vmName --command "sudo chown spring:spring /var/spring"

    gcloud compute scp conf/spring.service $vmName:~
    gcloud compute ssh $vmName --command "sudo mv spring.service /etc/systemd/system/spring.service"
    gcloud compute ssh $vmName --command "sudo systemctl enable spring"
    gcloud compute ssh $vmName --command "sudo systemctl start spring"
done