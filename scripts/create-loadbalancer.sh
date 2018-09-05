echo "CREATING VMs FOR WEB API"
gcloud beta compute --project=psg15-1718v-1534010601683 instances create load-balancer --zone=europe-west2-c --machine-type=f1-micro --subnet=default --network-tier=PREMIUM --maintenance-policy=MIGRATE --service-account=573161398457-compute@developer.gserviceaccount.com --scopes=https://www.googleapis.com/auth/devstorage.read_only,https://www.googleapis.com/auth/logging.write,https://www.googleapis.com/auth/monitoring.write,https://www.googleapis.com/auth/servicecontrol,https://www.googleapis.com/auth/service.management.readonly,https://www.googleapis.com/auth/trace.append --image=centos-7-v20180523 --image-project=centos-cloud --boot-disk-size=10GB --boot-disk-type=pd-standard

echo "UPDATING AND INSTALLING NGINX"
gcloud compute ssh load-balancer --command "sudo yum -y update"
gcloud compute ssh load-balancer --command "sudo yum -y install nginx"

echo "CONFIGURING NGINX"
gcloud compute scp conf/employbox.conf load-balancer:~
gcloud compute ssh load-balancer --command "sudo mv employbox.conf /etc/nginx/conf.d"
gcloud compute ssh load-balancer --command "sudo systemctl enable nginx"
gcloud compute ssh load-balancer --command "sudo systemctl start nginx"

gcloud compute ssh load-balancer --command "sudo setsebool -P httpd_can_network_connect 1"