# Introduction

ZeroAuth on Android, as with other platforms, aims to seamlessly manage the entire zkLogin flow.
This typically involves initiating a request to an **OpenID provider**, generating a salt request (especially when dealing
with a remote salting service),
and forwarding a proof request to a proving service, which is often remote. Despite the streamlined operation, ZeroAuth
ensures you retain full
control over the process. Thanks to its highly decoupled architecture, you have the flexibility to intervene at any
stage and reclaim control as needed.