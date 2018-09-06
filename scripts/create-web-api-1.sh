echo "INSTALLING JAVA 8"
gcloud compute ssh $1 --command "sudo yum -y update"
gcloud compute ssh $1 --command "sudo yum -y install java-1.8.0-openjdk"

echo "CONFIGURING AND INSTALLING WEB API"
gcloud compute ssh $1 --command "sudo useradd spring"
gcloud compute ssh $1 --command "sudo mkdir /var/spring"
gcloud compute scp ../web_api/build/libs/web_api.jar $1:~
gcloud compute ssh $1 --command "sudo mv web_api.jar /var/spring"
gcloud compute ssh $1 --command "sudo chown spring:spring /var/spring"

gcloud compute scp conf/spring.service $1:~
gcloud compute ssh $1 --command "sudo mv spring.service /etc/systemd/system/spring.service"
gcloud compute ssh $1 --command "sudo systemctl enable spring"
gcloud compute ssh $1 --command "sudo systemctl start spring"