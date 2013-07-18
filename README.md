Rediclus
========

*a learning exercise*

A simple Redis clone written in Clojure.  It implements the `get` and `set`
commands using the Redis TCP protocol.

Running
-------

    $ lein run

    # Or to run on a certain port
    $ lein run 12345

Usage
-----

Start the server and then open the `redis-cli` console and start playing:

    $ redis-cli
    $ redis 127.0.0.1:6379>
    $ redis 127.0.0.1:6379> set name honza
    $ OK
    $ redis 127.0.0.1:6379> get name
    $ "honza"

Benchmark
---------

Non-parallel get requests in ms:

    Iterations:      5000
    95th percentile: 0.241041183472
    99th percentile: 0.285148620605
    min:             0.165939331055
    max:             1.68514251709

License
-------

BSD, short and sweet
