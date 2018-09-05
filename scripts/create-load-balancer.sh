vmName=nginx-lb

echo "CREATING GCP VM"
gcloud beta compute --project=psg15-1718v-1534010601683 instances create $vmName --zone=europe-west2-c --machine-type=f1-micro --subnet=default --network-tier=PREMIUM --maintenance-policy=MIGRATE --service-account=573161398457-compute@developer.gserviceaccount.com --scopes=https://www.googleapis.com/auth/devstorage.read_only,https://www.googleapis.com/auth/logging.write,https://www.googleapis.com/auth/monitoring.write,https://www.googleapis.com/auth/servicecontrol,https://www.googleapis.com/auth/service.management.readonly,https://www.googleapis.com/auth/trace.append --tags=http-server,https-server --image=centos-7-v20180523 --image-project=centos-cloud --boot-disk-size=10GB --boot-disk-type=pd-standard

echo "INSTALLING NGINX"
gcloud compute ssh $vmName --command "sudo yum -y update"
gcloud compute ssh $vmName --command "sudo yum install epel-release"
gcloud compute ssh $vmName --command "sudo yum install nginx"

echo "CONFIGURING NGINX"
gcloud compute scp conf/nginx.conf $vmName:~
gcloud compute ssh $vmName --command "sudo mv nginx.conf /etc/nginx/nginx.conf"

echo "CONFIGURING NGINX SERVICE"
gcloud compute scp conf/nginx.service $vmName:~
gcloud compute ssh $vmName --command "sudo mv nginx.conf /etc/systemd/system/nginx.service"

gcloud compute ssh $vmName --command "sudo systemctl enable nginx"
gcloud compute ssh $vmName --command "sudo systemctl start nginx"
gcloud compute ssh $vmName --command "sudo setsebool -P httpd_can_network_connect 1"
