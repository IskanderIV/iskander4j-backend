#!/bin/bash

war_path=$(<project_war_path.dat)
#removelast '\r' character
war_path=${war_path/$'\r'/}
echo "war_path = $war_path"
host_url=$(<tomcat_host_url.dat)
host_url=${host_url/$'\r'/}
echo "host_url = $host_url"
#curl -X PUT --data-binary "@(< project-war-path.dat)cleverhauseServer-1.0-SNAPSHOT.war" -u cleverhause:cleverhause http://localhost:8080/manager/text/deploy?path=/clever

curl_args=(
#  -H 'accept:application/json'
  -u cleverhause:cleverhause
)

curl_data="$war_path/cleverhauseServer-1.0-SNAPSHOT.war"
echo "curl_data = $curl_data"

curl_put() {
  local url
  url="$host_url/${1#/}"
  echo "url = $url"
  shift || return # function should fail if we weren't passed at least one argument
  echo "curl_args = ${curl_args[@]}"
  curl -X PUT "${curl_args[@]}" "$url" "$@"
}

curl_put deploy?path=/clever --data-binary "@$curl_data"

#echo $command

#response=`$command`
#echo $response