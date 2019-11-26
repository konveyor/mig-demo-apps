# Mig server

POC for providing ssh connectivity between cluster pod, in order to stream data directly in the migration time.

## Installation

## Source

Login to your OpenShift source cluster.

To build:

```bash
./build.sh
```

To deploy:

```bash
./deploy.sh
```

To remove:

```bash
./destroy.sh
```

`manifests` directory contains definitions for different resources required by the app. `manifest.yaml` is just a collection of all those definitions. 

You may edit individual definitions for different app resources. To create an updated `manifest.yml` after updating individual resource definitions :

```bash
./build.sh
```

This will combine all YAMLs and create an updated `manifest.yaml`.

### Usage

After successful installation, the pod should be exposed by the LoadBalancer ingress IP. Wait for status of the LoadBalancer to contain the following:

```yaml
status:
 loadBalancer:
   ingress:
   - ip: x.x.x.x
```

To get the public IP address of the pod:

```bash
oc get service ssh-service -n test-ssh -o jsonpath='{.status.loadBalancer.ingress[0].ip}' # MIG_SRC_IP
```

## Target

Login to the target cluster and deploy following templates in the desired namespace.

pvc:
```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
 name: ssh-client-claim
 labels:
   ssh-client: "true"
spec:
 accessModes:
   - ReadWriteOnce
 resources:
   requests:
     storage: 1Gi
```

pod:

```yaml
apiVersion: v1
kind: Pod
metadata:
 name: ssh-client
 labels:
   ssh-client: "true"
spec:
 containers:
  - image: registry.access.redhat.com/rhel7/rhel-tools
    imagePullPolicy: Always
    name: ssh-client
    volumeMounts:
     - name: data
       mountPath: /var/data # LOCAL_DST_MOUNT_DIR
    command: ["sleep"]
    args: ["infinity"]
 volumes:
   - name: data
     persistentVolumeClaim:
       claimName: ssh-client-claim
```

### Usage

- Configure ssh keys on the destination pod:

```bash
ssh-keygen -qf /root/.ssh/id_rsa -P '' # SSH_PUBLIC_KEY
```

- Manually add the key to the source ssh-server pod

Source pod:
```bash
echo "<SSH_PUBLIC_KEY>" >> /root/.ssh/authorized_keys
```

- Perform migration and wait for it to finish

Destination pod:

```bash
rsync -avx -e ssh --progress root@<MIG_SRC_IP>:<SRC_MOUNT_DIR> <LOCAL_DST_MOUNT_DIR>
```








