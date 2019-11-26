RSATMP=$(mktemp -u)
/usr/bin/ssh-keygen -q -t rsa -f "${RSATMP}" -C '' -N ''
ECDSATMP=$(mktemp -u)
/usr/bin/ssh-keygen -q -t ecdsa -f "${ECDSATMP}" -C '' -N ''
ED25519TMP=$(mktemp -u)
/usr/bin/ssh-keygen -q -t ed25519 -f "${ED25519TMP}" -C '' -N ''

CONFIGFILE=$(mktemp)
echo 'HostKey /etc/ssh/ssh_host_rsa_key
HostKey /etc/ssh/ssh_host_ecdsa_key
HostKey /etc/ssh/ssh_host_ed25519_key
SyslogFacility AUTHPRIV
PermitRootLogin yes
AuthorizedKeysFile	/root/.ssh/authorized_keys
PasswordAuthentication no
ChallengeResponseAuthentication no
GSSAPIAuthentication yes
GSSAPICleanupCredentials no
UsePAM yes
X11Forwarding yes
PrintMotd no
AcceptEnv LANG LC_CTYPE LC_NUMERIC LC_TIME LC_COLLATE LC_MONETARY LC_MESSAGES
AcceptEnv LC_PAPER LC_NAME LC_ADDRESS LC_TELEPHONE LC_MEASUREMENT
AcceptEnv LC_IDENTIFICATION LC_ALL LANGUAGE
AcceptEnv XMODIFIERS
Subsystem	sftp	/usr/libexec/openssh/sftp-server
' > ${CONFIGFILE}

export RSATMP=$(cat $RSATMP| base64 -w 0)
export ECDSATMP=$(cat $ECDSATMP| base64 -w 0)
export ED25519TMP=$(cat $ED25519TMP| base64 -w 0)
export CONFIGFILE=$(cat $CONFIGFILE| base64 -w 0)

rm -f manifest.yaml
for f in manifests/*.yml; do (cat "${f}"; echo) >> manifest.yaml; done
for f in templates/*.yml; do (cat "${f}"| envsubst; echo) >> manifest.yaml; done
