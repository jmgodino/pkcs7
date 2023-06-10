openssl genrsa -des3 -out ca/rootCA.key 4096
openssl req -x509 -new -nodes -key ca/rootCA.key -sha256 -days 1024 -out ca/rootCA.crt
openssl genrsa -out user/paucel.key 2048
openssl req -new -key user/paucel.key -out user/paucel.csr
openssl x509 -req -in user/paucel.csr -CA ca/rootCA.crt -CAkey ca/rootCA.key -CAcreateserial -out user/paucel.crt -days 500 -sha256
