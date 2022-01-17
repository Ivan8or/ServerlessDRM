# ServerlessDRM
A simple, mid-scale DRM solution

Eager to ensure your software is limited to its intended users?

Don't want to spend time and resources on running a dedicated verification server?

Welcome, Serverless DRM!


This library allows developers to fully offload the heavy lifting of traditional DRM solutions to the client! 

Reliant only on a public registrar to store encrypted keys (such as github), ServerlessDRM will ensure that clients
must have a valid user token in order to pass validation.


**How does it work?**

ServerlessDRM relies on the modern hash algorithm SHA-512 to create a 'hashed' version
of every client token that may be exposed to the outside world.

A 'hashed' token can never be reversed back into the original token, which means that even if a malicious
actor has access to the hash, they will not be able to derive the original key from it.

ServerlessDRM reads a text document hosted on any popular platform of the developers choice (like github) containing
all valid hashed tokens, and after calculating the hash for the token the client supplies; compares the hash
to the hashes in the text document.
If the newly produced hash matches any of the existing ones, the client has been proved to have a valid token
and will be pass validation.

ServerlessDRM performs all http requests and comparisons to verify the client token asynchronously, greatly
reducing the performance implications on the client. 


**Performance**

The document host used in these measurements was github.com

Measurements were taken on the following hardware:
 - a ryzen **5950x** processor (zen 3, rd. 2020) 
 - an intel **x5670** processor (Westmere, rd. 2010)
 
Two algorithms were measured; Greedy and Streamed -

Greedy involves ingesting the entire set of hashed tokens from the document into a hashset,
then checking if the set contains the specified token.

Streamed involves stepping through the hashed tokens in the document as a stream, and comparing each
next hashed token to the specified token. This allows for an early escape if the tokens matched up before
the entire document has been read through. 


**(values are in miliseconds; lower is better)**

5950x (released 2020) summary:

 1k entries: 
 
 hash at:     |top     |middle    |bottom
 
 Greedy:       7        7          7
 
 Streamed:     5        7          7
 
 10k entries: 
 
 hash at:     |top     |middle    |bottom
 
 Greedy:       27       28         26
 
 Streamed:     31       71         30
 
 100k entries: 
 
 hash at:     |top     |middle    |bottom
 
 Greedy:       287      265        285
 
 Streamed:     24       668        280
 
 500k entries: 
 
 hash at:     |top     |middle    |bottom
 
 Greedy:       1172     1655       1332
 
 Streamed:     22       1272       1607
 
 1m entries:
 
 hash at:     |top     |middle    |bottom
 
 Greedy:       2829     2981       2733
 
 Streamed:     25       2064       3229
 
 
**(values are in miliseconds; lower is better)**

x5670 (released 2010) summary:

 1k entries: 
 
 hash at:     |top     |middle    |bottom
 
 Greedy:       22       25         21
 
 Streamed:     21       20         25
 
 10k entries: 
 
 hash at:     |top     |middle    |bottom
 
 Greedy:       109      120        115
 
 Streamed:     217      125        174
 
 100k entries: 
 
 hash at:     |top     |middle    |bottom
 
 Greedy:       845      814        856
 
 Streamed:     33       896        1048
 
 500k entries: 
 
 hash at:     |top     |middle    |bottom
 
 Greedy:       3820     4018       3846
 
 Streamed:     37       2111       4167
 
 1m entries:
 
 hash at:     |top     |middle    |bottom
 
 Greedy:       7687     7618       8493
 
 Streamed:     37       3985       7613
 


*note that the number of cores in a processor should not affect performance results


**Disclaimers**

ServerlessDRM is a fantastic solution for any small to mid-range softwares, with a relatively small amount of active tokens
However, due to its nature there are a few important considerations to make before deciding to use ServerlessDRM in your product.
   
1. ServerlessDRM will not let you track token usage.
Because ServerlessDRM only verifies the authenticity of a token on the client side, and has no central server to report to, there
is no way to track the frequency and amount of clients making use of any particular token.

2. ServerlessDRM will scale relatively poorly with large sets of tokens.
tbd




