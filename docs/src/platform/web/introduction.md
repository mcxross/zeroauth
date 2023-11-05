# Introduction

ZeroAuth for Web, like other platforms, is designed to manage the entire zkLogin process smoothly.
This includes initiating requests to an OpenID provider, sending salt requests to a remote salting service,
and forwarding proof requests to a proving service. By default, the salting and proving services make remote calls,
specifically to Mysten Labs endpoints for each service. However, this behavior can
be customized to fit your needs, either by changing the endpoints or by replacing the entire process.

Despite the streamlined operation, ZeroAuth ensures you retain full
control over the process. Thanks to its highly decoupled architecture, you have the flexibility to intervene at any
stage and reclaim control as needed.