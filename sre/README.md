# SRE Study Topics

This document is a work-in-progress.

Also see [SRE Confluence](https://weareadaptive.atlassian.net/wiki/spaces/SRE).

## Networking Basics

- ISO 7 Layer Model (brief)  
  https://www.cloudflare.com/en-gb/learning/ddos/glossary/open-systems-interconnection-model-osi/

- IPv4 / IPv6 (although IPv6 not in use)  
  https://www.cloudflare.com/en-gb/learning/network-layer/internet-protocol/

- TCP/IP  
  https://www.cloudflare.com/en-gb/learning/ddos/glossary/tcp-ip/

- UDP  
  https://www.cloudflare.com/en-gb/learning/ddos/glossary/user-datagram-protocol-udp/

- TLS/SSL

  - https://www.cloudflare.com/en-gb/learning/ssl/what-is-ssl/
  - https://www.cloudflare.com/en-gb/learning/ssl/transport-layer-security-tls/

- HTTP(S)

  - https://www.cloudflare.com/en-gb/learning/ddos/glossary/hypertext-transfer-protocol-http/
  - https://www.cloudflare.com/en-gb/learning/ssl/what-is-https/

- Certificates

  - SSL Certificates  
    https://www.cloudflare.com/en-gb/learning/ssl/what-is-an-ssl-certificate/
  - PKI  
    https://www.geeksforgeeks.org/public-key-infrastructure/
  - Chains
    - https://venafi.com/blog/how-do-certificate-chains-work/  
      (remember that the client is only supposed to trust the Root Certificate Authority, not the intermediate ones)
    - https://www.keyfactor.com/blog/what-is-a-certificate-revocation-list-crl-vs-ocsp/ (includes stapling)

- Websockets

  - https://en.wikipedia.org/wiki/WebSocket
  - https://sookocheff.com/post/networking/how-do-websockets-work/  
    (you don’t need the binary structure, just understand how the basic protocol works)

- Forward / Reverse proxies

  - https://www.cloudflare.com/en-gb/learning/cdn/glossary/reverse-proxy/
  - https://ipwithease.com/forward-proxy-vs-reverse-proxy/
  - https://en.wikipedia.org/wiki/Proxy_server
  - https://en.wikipedia.org/wiki/TLS_termination_proxy

- Firewalls
  - Real firewalls (not in Alpha)
    - https://networklessons.com/cisco/asa-firewall/introduction-to-firewalls
    - Firewalls are typically stateful - meaning you define a rule representing the ingress (or egress) and the firewall takes care of allowing the return traffic.
  - Network ACLs in use in Alpha
    - https://docs.aws.amazon.com/vpc/latest/userguide/vpc-network-acls.html
    - Network ACLs are not stateful - therefore rules need to be in place for both the ingress and return traffic
  - Security Groups (equivalent to firewalls)
    - https://docs.aws.amazon.com/vpc/latest/userguide/security-groups.html
    - Security groups are stateful
- Messaging systems

  - Aeron
  - Others… (e.g. AWS SNS / SQS) and different protocols (e.g. STOMP)

- Authentication/Authorisation
  - OAuth/OIDC
    - https://developer.okta.com/blog/2019/10/21/illustrated-guide-to-oauth-and-oidc
    - https://wso2.com/blogs/thesource/2019/08/oauth-2-basics/
    - https://developer.okta.com/blog/2017/06/21/what-the-heck-is-oauth
  - SAML
    - https://duo.com/blog/the-beer-drinkers-guide-to-saml
    - https://auth0.com/blog/how-saml-authentication-works/
    - Detailed: https://epi052.gitlab.io/notes-to-self/blog/2019-03-07-how-to-test-saml-a-methodology/

## Containers

- Docker / Podman / Gradle JIB plugin
  - Look at docker documentation (podman is unused at present)
  - OCI compliant container
  - Common Annotations: https://github.com/opencontainers/image-spec/blob/main/annotations.md
  - Gradle JIB plugin: used to generate containers during Gradle builds.  
    https://github.com/GoogleContainerTools/jib/blob/master/jib-gradle-plugin/README.md
- Orchestration
  - Docker Compose  
    Refer to docker documentation.
  - Kubernetes  
    https://kubernetes.io/docs/tutorials/hello-minikube/

## Databases

- Getting Started
  - https://earthly.dev/blog/postgres-docker/
  - Creation of database, account, role etc.  
    https://commandprompt.com/education/how-to-create-user-create-database-grant-privileges-in-postgresql/
  - PostgreSQL - general basic use
    - Select queries: https://www.postgresqltutorial.com/postgresql-tutorial/postgresql-select/
  - Schema management
    - Flyway
      - https://documentation.red-gate.com/fd/quickstart-how-flyway-works-184127223.html
      - https://documentation.red-gate.com/fd/why-database-migrations-184127574.html
    - When / How is it applied  
      https://weareadaptive.atlassian.net/wiki/spaces/SRE/pages/4059529262/Database+Schema+Management+Guidelines+for+Developers+and+SRE

## Hydra

- [Distributed system basics](../java/hydra-training-modules/Module_1_Distributed_system_basics/Module%201.md)
- [Replicated state machines & distributed consensus](../java/hydra-training-modules/Module_2_Replicated_state_machines_and_distributed_consensus/Module%202.md)
- [Determinism & Single-Threaded Performance](../java/hydra-training-modules/Module_3_Determinism_and_single_threaded_performance/Module%203.md)
- [Hub-and-spoke architecture](../java/hydra-training-modules/Module_4_Hub_and_spoke_architecture/Module%204.md)
- Hydra Components
  - Cluster
  - WebSocket Gateways
  - FIX Gateways
  - Other gateways (ESG / Reval etc.)
  - History DB writer
  - Transaction support (cluster rollback)
  - Snapshotting
- Operational tooling
  - Hydra containers
  - Inspecting cluster state
  - FIX admin
  - Monitoring (including divergence)
  - Recovery and investigation
  - Maintenance
  - Seeding
  - Hydra Management tools (Alpha only)
- Performance Optimisation (for SRE)
  - Measuring Latency
  - Fast Networks
  - Fast CPU
  - Fast Storage (maybe!)
  - Kernel Bypass
  - CPU isolation and pinning
  - NUMA

## Infrastructure

- Terraform
- AWS

## Continuous Integration

- CI systems:
  - Jenkins
  - CircleCI
  - Expo EAS (Alpha Mobile)
- Artefact Proxies / Artefact Stores
  - Why do they exist?
    - To host and/or cache artefacts for development and/or deployment so as to minimise, avoid or block (e.g. airgap) traffic to external systems
    - Usually for cost and/or security reasons
    - Artifactory (commercial): used here at Adaptive to expose artefacts for client consumption.
    - Nexus: used here at Adaptive (project specific):
      - proxy/cache external artefacts to minimise outside traffic
        - reduce costs (if used on cloud)
        - can (but doesn’t always) improve speed of builds
        - Can be used to host temporary / easily reproducible artefacts
        - Usually sits behind a forward proxy that has antivirus / anti-malware scanning capabilities - designed to implement a “clean room”
    - Cloud provider specific Artefact repositories:
      - Typically used to host project artefacts
      - Can be used for proxy/caching other artefacts (Cloud Provider specific)
        - Usually has restrictions, hence why Nexus is used
        - Some cannot support private repositories
        - Some only support specific public repositories
      - AWS ECR
        - General OCI compliant registry
        - Has proxy capability for _specific_ public registries and limited to once-per-day updates.
      - AWS S3
        - Can be adapted to host lots of different kinds of artefacts and repository types (as it can be viewed as a website or a file system)
        - Can be used as the backend for Nexus
- NodeJS Build Tools
  - Npm / Yarn / Pnpm (all similar)
    - All run “scripts” inside `package.json`
    - Scripts are structured to represent common
  - Rush stack (FXCM only)
- Java Build Tools
  - Gradle
  - Maven (not used here at Adaptive much any more)
- Testing tools
  - Hydra projects
    - Cucumber
  - Web projects (including Hydra projects with a front-end)
    - Vite
    - Cypress
    - Playwright
  - Mobile Projects
    - Expo + EAS
    - Appium
    - WebdriverIO
