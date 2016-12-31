#!/usr/bin/env bash
remote=$1
originUrl='';
if [ "$remote" == '1' ]
then
  echo "switch to oschina"
  originUrl=git@git.oschina.net:DeWork.com/Banner.git;

elif [ "$remote" == '2' ]
then
  echo "Switch to gitHub"
  originUrl=git@github.com:xiexiang89/Android-Auto-BannerView.git
fi
  git remote set-url origin $originUrl
  git remote -v