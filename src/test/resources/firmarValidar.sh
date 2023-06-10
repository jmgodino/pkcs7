
#Attached
echo "SMIME ATTACHED DER"
openssl smime -sign -binary -in msgs/texto.txt -signer user/paucel.crt  -inkey user/paucel.key -outform DER  -out msgs/texto_smime.pkcs7 -nodetach
openssl smime -verify -binary -inform DER -in msgs/texto_smime.pkcs7 -noverify

#Attached PEM
echo "SMIME ATTACHED PEM"
openssl smime -sign -binary -in msgs/texto.txt -signer user/paucel.crt  -inkey user/paucel.key -outform PEM  -out msgs/texto_smime_pem.pkcs7 -nodetach
openssl smime -verify -binary -inform PEM -in msgs/texto_smime_pem.pkcs7 -noverify

#Detached
echo "CMS DETACHED DER"
openssl cms -sign -binary -in msgs/texto.txt -signer user/paucel.crt  -inkey user/paucel.key -outform DER  -out msgs/texto_cms.pkcs7
openssl cms -verify -binary -inform DER -in msgs/texto_cms.pkcs7 -content msgs/texto.txt -noverify

#Detached PEM
echo "CMS DETACHED PEM"
openssl cms -sign -binary -in msgs/texto.txt -signer user/paucel.crt  -inkey user/paucel.key -outform PEM  -out msgs/texto_cms_pem.pkcs7
openssl cms -verify -binary -inform PEM -in msgs/texto_cms_pem.pkcs7 -content msgs/texto.txt -noverify
