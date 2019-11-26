#!/bin/bash

set -e

user=root

# make sure the ssh dir exists
sshdir="/${user}/.ssh"
mkdir "${sshdir}"

keyfile="${sshdir}/authorized_keys"
touch "${keyfile}"
chmod 600 "${keyfile}"


mkdir /var/run/sshd
/usr/sbin/sshd -D
