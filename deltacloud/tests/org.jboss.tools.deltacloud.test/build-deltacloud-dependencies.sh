#!/bin/sh

java -jar jruby-complete-1.5.3.jar -S gem install -i ./deltacloud-dependencies \
activesupport \
amazon-ec2 \
daemons \
eventmachine \
haml \
highline hoe \
http_connection \
json \
json_pure \
mime-types \
nokogiri \
rack \
rack-accept \
rerun \
rest-client \
right_aws \
right_http_connection \
rubyforge \
sinatra \
steamcannon-deltacloud-core \
uuidtools \
xml-simple --no-rdoc --no-ri

jar cf deltacloud-dependencies.jar -C deltacloud-dependencies .

