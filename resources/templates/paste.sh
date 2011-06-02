FILE=$1
KEY=$2
URL=<url>/$FILE

curl -s -XPOST \
 -H "Content-type: text/plain" \
 -H "X-Key: ${KEY}" \
 --data-binary @- \
 $URL

