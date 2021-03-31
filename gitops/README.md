## OpenShift GitOps Compatible Apps

The demo applications present in this directory are compatible with OpenShift GitOps deployments. The base applications can be found under [../apps/](../apps/) directory. 

### General guidelines

These guidelines apply to all applications present in this directory. If a more specific guideline is present for an app in its own directory, please also follow that guideline.

Namespace definitions are not included in any of the apps to make it possible to deploy the app in a namespace of your choice.

#### Deploy using Kustomize

All the apps present in this directory are packaged using `kustomize`. Different _kustomizations_ are provided wherever applicable for different versions of OpenShift. Use the most specific _kustomization_ available for the version of OpenShift you're using. For instance, if an overlay directory `3.x` is present for an application, all 3.x versions should use this. If a more specific overlay directory is present, use that version for your needs. For instance, if an overlay is availble for `3.11`, use the `3.11` directory instead of `3.x`. If no overlays are present for your platform, simply use the application from `base` directory.

Use `--kustomize` or `-k` flag to apply application manifests:

```sh
kubectl apply -k <base_or_overlay_directory_for_your_env>
```

#### Deploy using ArgoCD

Any application can be deployed using ArgoCD installed through OpenShift GitOps Operator. 

Create an `Application` CR to point to the application you want:

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
    - /spec/template/spec/containers/0/image
    - /spec/template/spec/containers/0/ports/0/protocol
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

### Adding overlays for your environment

You can add overlays for your environment if existing `base` application doesn't work for you and a specific overlay is not present for your environment. We have only added overlays for OpenShift versions we frequently deal with. Please name the directory according to the OpenShift versions your overlay is compatible with. Common overlays are present in `common` directory. Use them as a reference to create your own overlays. If adding an overlay for Kubernetes, please use `kube-` as a prefix to the name of the overlay directory. Open a PR and we will happily include your overlay here! 