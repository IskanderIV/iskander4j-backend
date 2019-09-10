#!/bin/bash

file="project_war_path.dat"

host_url=$(<tomcat_host_url.dat)
host_url=${host_url/$'\r'/}
echo "host_url = $host_url"

curl_args=(
#  -H 'accept:application/json'
  -u cleverhause:cleverhause
)

function curl_put() {
  local url
  url="$host_url/${1#/}"
  echo "url = $url"
  shift || return # function should fail if we weren't passed at least one argument
  echo "curl_args = ${curl_args[@]}"
  curl -X PUT "${curl_args[@]}" "$url" "$@"
}

function save_war_path() {
    local war_path=${1/$'\n'/}
    # echo "war_path = $war_path"
    warfile=($(ls "$war_path"/*.war ))

    if [ -e $warfile ] 
    then 
        # do here what you want
        local deploy_path=$(basename "${warfile%.*}")
        # echo $deploy_path
        curl_put deploy?path=/"$deploy_path" --data-binary "@$warfile"
        # echo "${warfile[@]}"
    else
        echo 'war file not exist'
        exit
    fi
}

function read_file() {
    while IFS= read -r line || [[ -n "$line" ]]
    do
        echo line = "$line"
        save_war_path "$line"
    done < "$1"
}

read_file "${file}"