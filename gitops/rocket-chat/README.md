## Rocket Shop

### Deploying to Kubernetes/OpenShift

Based on version of your OpenShift cluster, choose the most specific `overlay` available for your environment. If an overlay is not present for your environment, use the manifests from `base` directory. For instance, when deploying on an OpenShift 3.11.x cluster, use the overlay in directory `./overlays/3.x` as `3.x` is the most specific overlay available for 3.11.272. When deploying on an OpenShift 4.6 cluster, you can simply use the base directory as none of the overlays apply to OpenShift 4.6.

Use kubectl with `--kustomize` or `-k` flag to apply the application manifests on your cluster:

```sh
kubectl apply -k <overlay_or_base>
```

### Deploying using ArgoCD

This example assumes that you have setup a pipeline using ArgoCD in OpenShift GitOps Operator deployed in `openshift-gitops` namespace.

Create an `Application` CR to point to this application:

```yml
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: rocket-chat-local
  namespace: openshift-gitops
spec:
  destination:
    namespace: <namespace_in_which_you_want_to_deploy_this_app>
    server: <target_cluster_to_deploy_to>
  ignoreDifferences:
  - group: apps.openshift.io
    jsonPointers:
    - /spec/template/spec/containers/0/resources
    - /spec/template/spec/containers/0/terminationMessagePath
    - /spec/template/spec/containers/0/terminationMessagePolicy
    - /spec/template/spec/containers/1/image
    - /spec/template/spec/containers/1/ports/0/protocol
    - /spec/template/spec/containers/1/terminationMessagePath
    - /spec/template/spec/containers/1/terminationMessagePolicy
    kind: DeploymentConfig
  project: default
  source:
    path: <most_specific_overlay_or_base_for_your_environment>
    repoURL: https://github.com/konveyor/mig-demo-apps
    targetRevision: HEAD
  syncPolicy:
    syncOptions:
    - CreateNamespace=true
```

Make sure you set the right `spec.source.path` based on applicable overlay. For instance, to deploy on an OpenShift 3.11 cluster set `spec.source.path` to `gitops/rocket-chat/overlays/3.x`.