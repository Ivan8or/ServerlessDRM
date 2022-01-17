# ServerlessDRM
## A simple, mid-scale DRM solution.

Eager to ensure your software is limited to its intended users?

Don't want to spend time and resources on running a dedicated verification server?

Welcome, to Serverless DRM!

ServerlessDRM allows developers to fully offload the heavy lifting of traditional DRM solutions to the client; reducing costs and improving performance at the same time!

# Features!

> ServerlessDRM is accessible.

ServerlessDRM has one of the lowest overheads of any DRM solution. **It does not require you to set up and host any sort of database or authentication server**, resulting in a very low barrier to entry for any software developers who wish to license their products. 

> ServerlessDRM has the power of the entire public cloud behind it.

Because SDRM references a text document that can be stored on any fitting public platform (github, etc.), it inherits the same redundancy and multimillion dollar infrastructure those platforms use. **Clients all across the world will be able to authenticate themselves with low latency and nearly no risk of downtime** because of these platforms' expansive networks of servers, outshining any self-hosted solution the developer on a budget can achieve.

This also makes ServerlessDRM essentially immune to being oversaturated with requests, allowing for an unprecedented amount of concurrent user verifications that self-hosted DRM solutions can never reach. 

> ServerlessDRM can be scaled to fit most needs.

ServerlessDRM can support tens of thousands of active user tokens while maintaining an extremely low latency thanks to the wide net of the public cloud. Example performance measurements can be seen in a lower section, where SDRM shows **sub-second performance even with hundreds of thousands of user tokens.**

*If at any point a developer encounters a situation where they find ServerlessDRM does not manage enough user tokens to meet their needs, they can ponder their next choice of DRM manager while taking a leisurely drive in their Ferrari.*

> ServerlessDRM is more secure.

While ALL DRM solutions are avoidable by an experienced developer with the product in hand, ServerlessDRM eliminates a massive (and the most critical) risk factor. With no central server to target, malicious actors will never be able to find exploits or bugs as is possible with any traditional DRM. Additionally, **DDOS attacks are not a concern for ServerlessDRM, unlike other DRM solutions** where even a moderately sized denial of service attack is a constant threat to the functionality of every product they support.

# How does it work?

ServerlessDRM relies on the modern hash algorithm SHA-512 to derive a 'hash', a unique attribute
of a string (or any other form of data) which is safe to expose to the outside world.

>*A hash cannot be reversed to extract the value it was built from, which means that even if a malicious
actor has access to a token hash, they will not be able to derive the original key from it.*

ServerlessDRM reads a text document hosted on any popular platform of your choice (like github), which contains
a list of all active user token hashes.

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
 
Two token parsing algorithms are measured; Greedy and Streamed -

Greedy involves ingesting the entire set of hashed tokens from the document into a hashset,
then checking if the set contains the specified token.

Streamed involves stepping through the hashed tokens in the document as a stream, and comparing each
next hashed token to the specified token. This allows for an early escape if a matching token is found before
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

ServerlessDRM is a fantastic solution for any small to mid-range products.
However, due to its nature there are a few important considerations to make before deciding to use ServerlessDRM in your product.
   
>ServerlessDRM will not let you track token utilization.

Because ServerlessDRM only verifies the authenticity of a token on the client side, and has no central server to report to, there
is no way to track the frequency and amount of clients making use of any particular token.

>ServerlessDRM will scale relatively poorly with large sets of tokens.

ServerlessDRM operates in O(n) time, which means that if the number of tokens increases tenfold then so will the average time it takes to validate a token.
Due to this, the delay to validate a token may rise to unnacceptable levels if the amount of active tokens passes a certain amount. The recommended upper limit for tokens is between 10k and 100k to maintain sub-100ms delays, depending on the expected client hardware.

>ServerlessDRM is not designed for extreme low-latency products.

ServerlessDRM was designed with affordability and accessibility in mind - not for maximum performance. 
While there is a serious effort made to produce satisfying performance, some products have demands that SDRM simply cannot meet.

> DRM tools are always susceptible to tampering.

ServerlessDRM and any other DRM tool can be avoided by an experienced developer. Luckily, the unique serverless design of ServerlessDRM does not increase the attack surface by any significant amount (it actually reduces it), which means that it is just as robust as traditional DRM tools when it comes to avoiding being exploited. 


