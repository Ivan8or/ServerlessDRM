# ServerlessDRM
A simple, mid-scale DRM solution

Eager to ensure your software is limited to its intended users?

Don't want to spend time and resources on running a dedicated verification server?

Welcome, Serverless DRM!


This library allows developers to fully offload the heavy lifting of traditional DRM solutions to the client! 

Reliant only on a public registrar to store encrypted keys (such as github), ServerlessDRM will ensure that clients
must have a valid user token in order to pass validation.


# How does it work?

ServerlessDRM relies on the modern hash algorithm SHA-512 to derive a 'hash', a unique attribute
of a string (or any other form of data) which is safe to expose to the outside world.

>*A hash cannot be reversed to extract the value it was built from, which means that even if a malicious
actor has access to a token hash, they will not be able to derive the original key from it.*

ServerlessDRM reads a text document hosted on any popular platform of your choice (like github), which contains
a list of hashes of all valid user tokens.

SDRM calculates the hash for the token the client supplies, and then compares the calculated hash to the hashes in the text document.

If the newly produced hash matches any of the existing ones, the client has been proved to have a valid token
and will be pass validation.

>*ServerlessDRM performs all http requests and comparisons to verify the client token asynchronously, greatly
reducing the performance implications on the client.*


# Performance

The document host used in these measurements was `github.com`

Measurements were taken on the following hardware:
 - a ryzen 5950x processor (Zen 3, released 2020) 
 - an intel x5670 processor (Westmere, released 2010)
 
Two algorithms were measured; Greedy and Streamed -

Greedy involves ingesting the entire set of hashed tokens from the document into a hashset,
then checking if the set contains the specified token.

Streamed involves stepping through the hashed tokens in the document as a stream, and comparing each
next hashed token to the specified token. This allows for an early escape if the tokens matched up before
the entire document has been read through. 



## `ryzen 5950x` summary:
**(values are in miliseconds; lower is better)**
```
 1k token entries: 
 entry at     |top     |middle    |bottom
 Greedy:       7        7          7
 Streamed:     5        7          7
 
 10k token entries: 
 entry at     |top     |middle    |bottom
 Greedy:       27       28         26
 Streamed:     31       71         30
 
 100k token entries: 
 entry at     |top     |middle    |bottom
 Greedy:       287      265        285
 Streamed:     24       668        280
 
 500k token entries: 
 entry at     |top     |middle    |bottom
 Greedy:       1172     1655       1332
 Streamed:     22       1272       1607
 
 1m token entries:
 entry at     |top     |middle    |bottom
 Greedy:       2829     2981       2733
 Streamed:     25       2064       3229
```
 

## `xeon x5670` summary:
**(values are in miliseconds; lower is better)**

```
 1k token entries: 
 entry at     |top     |middle    |bottom
 Greedy:       22       25         21
 Streamed:     21       20         25
 
 10k token entries: 
 entry at     |top     |middle    |bottom
 Greedy:       109      120        115
 Streamed:     39       125        174
 
 100k token entries: 
 entry at     |top     |middle    |bottom
 Greedy:       845      814        856
 Streamed:     33       896        1048
 
 500k token entries: 
 entry at     |top     |middle    |bottom
 Greedy:       3820     4018       3846
 Streamed:     37       2111       4167
 
 1m token entries:
 entry at     |top     |middle    |bottom
 Greedy:       7687     7618       8493
 Streamed:     37       3985       7613
 ```


*note that the number of cores in a processor should not affect performance results


**Disclaimers**

ServerlessDRM is a fantastic solution for any small to mid-range softwares, with a relatively small amount of active tokens
However, due to its nature there are a few important considerations to make before deciding to use ServerlessDRM in your product.
   
1. ServerlessDRM will not let you track token usage.
Because ServerlessDRM only verifies the authenticity of a token on the client side, and has no central server to report to, there
is no way to track the frequency and amount of clients making use of any particular token.

2. ServerlessDRM will scale relatively poorly with large sets of tokens.
tbd




